package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.CollectionHelper;
import honeycrm.client.view.AbstractView.View;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldMultiEnum extends FieldEnum {
	private static final long serialVersionUID = 7371457456391185007L;

	// Separator string sequence between multiple selected enumeration string values.
	// A selection of the values A and B by the user will be marshalled to "A"+ SEPARATOR + "B"
	// and unmarshalled by splitting the stored string on every occurrence of the separator string.
	// NOTE: using ^ as separator is a bad idea because in String.split() it will be interpreted as
	// a regular expression.
	public static final String SEPARATOR = "__23__";

	public FieldMultiEnum() {
	}

	public FieldMultiEnum(final String id, final String label, final String... options) {
		super(id, label, options);
	}
	
	@Override
	protected void internalSetData(final ListBox widget, final Object value, final View view) {
		if (view == View.CREATE) {
			final String[] options = getOptions();
			for (int i = 0; i < options.length; i++) {
				widget.addItem(options[i]);
			}
		} else if (view == View.EDIT) {
			final Set<String> selectedItems = (null == value || value.toString().isEmpty()) ? new HashSet<String>() : CollectionHelper.toSet(value.toString().split(FieldMultiEnum.SEPARATOR));
			final String[] options = getOptions();

			for (int i = 0; i < options.length; i++) {
				widget.addItem(options[i]);
				if (selectedItems.contains(options[i])) {
					// preselect the item(s) that have been stored in the db
					widget.setItemSelected(i, true);
				}
			}
		}
	}

	@Override
	protected void internalSetData(final HTML widget, final Object value, final View view) {
		if (value.toString().isEmpty()) {
		} else {
			String ul = "";
			for (final String selection : value.toString().split(FieldMultiEnum.SEPARATOR)) {
				ul += "<li>" + selection + "</li>";
			}
			widget.setHTML("<ul>" + ul + "</ul>");
		}
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}

	@Override
	protected Serializable internalGetData(final Widget w) {
		final ListBox box = (ListBox) w;
		final Set<String> selectedValues = new HashSet<String>();

		for (int i = 0; i < box.getItemCount(); i++) {
			if (box.isItemSelected(i)) {
				selectedValues.add(box.getValue(i));
			}
		}

		return CollectionHelper.join(selectedValues, FieldMultiEnum.SEPARATOR);
	}
	
	@Override
	protected Widget editField() {
		return new ListBox(true);
	}
	
	@Override
	protected Widget detailField() {
		return new HTML();
	}
}
