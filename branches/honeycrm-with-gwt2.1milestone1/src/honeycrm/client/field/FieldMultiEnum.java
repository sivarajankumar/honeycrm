package honeycrm.client.field;

import honeycrm.client.misc.CollectionHelper;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldMultiEnum extends FieldEnum {
	private static final long serialVersionUID = 7371457456391185007L;

	// Separator string sequence between multiple selected enumeration string values.
	// A selection of the values A and B by the user will be marshalled to "A"+ SEPARATOR + "B"
	// and unmarshalled by splitting the stored string on every occurence of the separator string.
	// NOTE: using ^ as separator is a bad idea because in String.split() it will be interpreted as
	// a regular expression.
	public static final String SEPARATOR = "__23__";

	public FieldMultiEnum() {
	}

	public FieldMultiEnum(final int id, final String label, final String... options) {
		super(id, label, options);
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		ListBox box = new ListBox(true);
		final String[] options = getOptions();
		for (int i = 0; i < options.length; i++) {
			box.addItem(options[i]);
		}
		return box;
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		if (value.toString().isEmpty()) {
			return new Label("");
		} else {
			String ul = "";

			for (final String selection : value.toString().split(FieldMultiEnum.SEPARATOR)) {
				ul += "<li>" + selection + "</li>";
			}
			return new HTML("<ul>" + ul + "</ul>");
		}
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		final Set<String> selectedItems = (null == value || value.toString().isEmpty()) ? new HashSet<String>() : CollectionHelper.toSet(value.toString().split(FieldMultiEnum.SEPARATOR));
		final String[] options = getOptions();
		final ListBox box = new ListBox(true);

		for (int i = 0; i < options.length; i++) {
			box.addItem(options[i]);
			if (selectedItems.contains(options[i])) {
				// preselect the item(s) that have been stored in the db
				box.setItemSelected(i, true);
			}
		}

		return box;
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}
	
	@Override
	public Object getData(Widget widgetValue) {
		final ListBox box = (ListBox) widgetValue;
		final Set<String> selectedValues = new HashSet<String>();

		for (int i = 0; i < box.getItemCount(); i++) {
			if (box.isItemSelected(i)) {
				selectedValues.add(box.getValue(i));
			}
		}

		return CollectionHelper.join(selectedValues, FieldMultiEnum.SEPARATOR);
	}
}
