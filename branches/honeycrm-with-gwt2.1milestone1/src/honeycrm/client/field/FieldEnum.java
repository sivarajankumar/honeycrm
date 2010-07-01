package honeycrm.client.field;

import honeycrm.client.misc.CollectionHelper;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldEnum extends AbstractField {
	private static final long serialVersionUID = -4542742508636055819L;
	protected String[] options;

	public FieldEnum() { // for gwt
	}

	public FieldEnum(final int id, final String label, final String... options) {
		super(id, label);
		this.options = options;
	}

	public String[] getOptions() {
		return options;
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		ListBox box = new ListBox();
		final String[] options = getOptions();
		for (int i = 0; i < options.length; i++) {
			box.addItem(options[i]);
		}
		return box;
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		return new Label((null == value) ? "" : value.toString());
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		final Set<String> selectedItems = (null == value || value.toString().isEmpty()) ? new HashSet<String>() : CollectionHelper.toSet(value.toString().split(FieldMultiEnum.SEPARATOR));
		final String[] options = getOptions();
		final ListBox box = new ListBox();

		for (int i = 0; i < options.length; i++) {
			box.addItem(options[i]);
			if (selectedItems.contains(options[i])) { // preselect the item(s) that have
				// been stored in the db
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

		if (box.getSelectedIndex() == -1) {
			// nothing has been selected.
			return "";
		} else {
			return box.getValue(box.getSelectedIndex());
		}
	}
}
