package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.CollectionHelper;

import java.io.Serializable;
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

	public FieldEnum(final String id, final String label, final String... options) {
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
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		final Serializable value = dto.get(fieldId);
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
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}

	@Override
	protected Serializable internalGetData(Widget w) {
		final ListBox box = (ListBox) w;

		if (box.getSelectedIndex() == -1) {
			// nothing has been selected.
			return "";
		} else {
			return box.getValue(box.getSelectedIndex());
		}
	}

	// TODO this is more difficult..
	@Override
	protected Widget editField() {
		return new ListBox();
	}
}
