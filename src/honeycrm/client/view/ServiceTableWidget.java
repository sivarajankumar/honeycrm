package honeycrm.client.view;

import honeycrm.client.dto.Dto;
import honeycrm.client.field.AbstractField;
import honeycrm.client.view.AbstractView.View;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ServiceTableWidget extends ITableWidget {
	private static final int HEADER_ROWS = 1;
	private final FlexTable table = new FlexTable();
	private final Dto dto = new Dto();
	private final View view;
	private final Label sum = new Label();

	public ServiceTableWidget(final View view) {
		this.view = view;

		initHeader();

		sum.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		final VerticalPanel panel = new VerticalPanel();
		panel.add(table);
		panel.add(sum);

		if (view != View.DETAIL) {
			// only insert the add button if we are not in detail view
			final Button addBtn = new Button("Add");
			addBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					final int rows = table.getRowCount();

					for (int x = 0; x < dto.getListFieldIds().length; x++) {
						final String index = dto.getListFieldIds()[x];
						table.setWidget(rows, x, addChangeEvents(index, dto.getFieldById(index).getWidget(view, dto.get(index))));
					}
				}
			});

			panel.add(addBtn);
		}

		initWidget(panel);
	}

	private void initHeader() {
		for (int x = 0; x < dto.getListFieldIds().length; x++) {
			final AbstractField field = dto.getFieldById(dto.getListFieldIds()[x]);
			table.setWidget(0, x, field.getWidget(View.LIST_HEADER, field.getLabel()));
		}
	}

	private Widget addChangeEvents(final String index, final Widget widget) {
		// only attach a change event for text boxes
		if (widget instanceof TextBox) {
			if ("price".equals(index) || "quantity".equals(index) || "discount".equals(index)) {
				((TextBox) widget).addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						// Window.alert("change!");
						onPriceOrQuantityUpdate();
					}
				});
			}
		}

		return widget;
	}

	/**
	 * Update the sum for every
	 */
	private void onPriceOrQuantityUpdate() {
		setData(getData());
	}

	@Override
	public List<Dto> getData() {
		final List<Dto> services = new LinkedList<Dto>();

		for (int y = HEADER_ROWS; y < table.getRowCount(); y++) {
			final Dto s = new Dto();

			for (int x = 0; x < dto.getListFieldIds().length; x++) {
				if (table.getCellCount(y) > x) {
					if (table.getWidget(y, x) instanceof TextBox) {
						// TODO do this for other widgets as well
						s.set(dto.getListFieldIds()[x], dto.getFieldById(dto.getListFieldIds()[x]).getData(table.getWidget(y, x)));
					} else {
						throw new RuntimeException("Cannot yet handle widget type " + table.getWidget(y, x).getClass());
					}
				}
			}

			s.set("sum", ((Double) s.get("price") - (Double) s.get("discount")) * (Double) s.get("quantity"));
			services.add(s);
		}

		return services;
	}

	@Override
	public void setData(List<Dto> data) {
		if (null == data)
			return;

		if (!data.isEmpty()) {
			if (data.get(0) instanceof Dto) {
				final boolean wasTableAlreadyFilled = (table.getRowCount() == HEADER_ROWS + data.size());

				for (int y = 0; y < data.size(); y++) {
					for (int x = 0; x < data.get(y).getListFieldIds().length; x++) {
						final String index = data.get(y).getListFieldIds()[x];
						final AbstractField field = data.get(y).getFieldById(index);

						if (wasTableAlreadyFilled) {
							// update widget because it already exists
							if (table.getWidget(HEADER_ROWS + y, x) instanceof TextBox) {
								// TODO this is faaaaar to crappy!
								// TODO this should be done by field currency somehow..
								((TextBox) table.getWidget(HEADER_ROWS + y, x)).setText(((TextBox) field.getWidget(View.EDIT, data.get(y).get(index))).getText());
								// data.get(y).getFieldValue(index).toString());
							}
						} else {
							// add a new widget and new click handler
							table.setWidget(HEADER_ROWS + y, x, data.get(y).getFieldById(index).getWidget(view, data.get(y).get(index)));
							addChangeEvents(index, table.getWidget(HEADER_ROWS + y, x));
						}
					}
				}
			} else {
				throw new RuntimeException("Expected DtoService received " + data.get(0).getClass());
			}
		}

		sum.setText(NumberFormat.getCurrencyFormat("EUR").format(getSum(data)));
	}

	private double getSum(List<Dto> data) {
		double currentSum = 0.0;
		for (Dto service : data) {
			currentSum += ((Double) service.get("price") - (Double) service.get("discount")) * (Double) service.get("quantity");
		}
		return currentSum;
	}
}
