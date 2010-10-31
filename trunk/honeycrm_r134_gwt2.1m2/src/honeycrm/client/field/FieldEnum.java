package honeycrm.client.field;

import honeycrm.client.misc.CollectionHelper;
import honeycrm.client.view.AbstractView.View;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldEnum extends AbstractField {
	private static final long serialVersionUID = -4542742508636055819L;
	protected String[] options;

	public FieldEnum() { // for gwt
	}

	public FieldEnum(final String id, final String label, final String... options) {
		super(id, label);
		this.options = options;
	}

	public String[] getOptions() {
		return options;
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
				if (selectedItems.contains(options[i])) { // preselect the item(s) that have
					// been stored in the db
					widget.setItemSelected(i, true);
				}
			}
		}
	}

	@Override
	protected Serializable internalGetData(final Widget w) {
		final ListBox box = (ListBox) w;

		if (box.getSelectedIndex() == -1) {
			// nothing has been selected.
			return "";
		} else {
			return box.getValue(box.getSelectedIndex());
		}
	}

	@Override
	protected Widget editField() {
		return new ListBox();
	}
}
