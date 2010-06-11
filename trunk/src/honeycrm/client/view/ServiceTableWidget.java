package honeycrm.client.view;

import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.DtoService;
import honeycrm.client.view.AbstractView.View;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ServiceTableWidget extends ITableWidget {
	private static final int HEADER_ROWS = 1;
	private final FlexTable table = new FlexTable();
	private final AbstractDto dto = new DtoService();
	private final View view;

	public ServiceTableWidget(final View view) {
		this.view = view;

		final VerticalPanel panel = new VerticalPanel();

		for (int x = 0; x < dto.getListViewColumnIds().length; x++) {
			table.setWidget(0, x, new Label(dto.getFieldById(dto.getListViewColumnIds()[x]).getLabel()));
		}
		panel.add(table);

		if (view != View.DETAIL) {
			// only insert the add button if we are not in detail view
			final Button addBtn = new Button("Add");
			addBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					final int rows = table.getRowCount();

					for (int x = 0; x < dto.getListViewColumnIds().length; x++) {
						final int index = dto.getListViewColumnIds()[x];
						table.setWidget(rows, x, addChangeEvents(index, dto.getFieldById(index).getWidget(view, dto.getFieldValue(index))));
					}
				}
			});

			panel.add(addBtn);
		}

		initWidget(panel);
	}

	private Widget addChangeEvents(final int index, final Widget widget) {
		// only attach a change event for text boxes
		if (widget instanceof TextBox) {
			if (DtoService.INDEX_PRICE == index || DtoService.INDEX_QUANTITY == index || DtoService.INDEX_DISCOUNT == index) {
				((TextBox) widget).addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
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
	public List<? extends AbstractDto> getData() {
		final List<DtoService> services = new LinkedList<DtoService>();

		for (int y = HEADER_ROWS; y < table.getRowCount(); y++) {
			final DtoService s = new DtoService();

			for (int x = 0; x < dto.getListViewColumnIds().length; x++) {
				if (table.getCellCount(y) > x) {
					if (table.getWidget(y, x) instanceof TextBox) {
						// TODO do this for other widgets as well
						s.setFieldValue(dto.getListViewColumnIds()[x], ((TextBox) table.getWidget(y, x)).getText());
					} else {
						throw new RuntimeException("Cannot yet handle widget type " + table.getWidget(y, x).getClass());
					}
				}
			}

			s.setSum((s.getPrice() - s.getDiscount()) * s.getQuantity());
			services.add(s);
		}

		return services;
	}

	@Override
	public void setData(List<? extends AbstractDto> data) {
		if (!data.isEmpty()) {
			if (data.get(0) instanceof DtoService) {
				final boolean wasTableAlreadyFilled = (table.getRowCount() == HEADER_ROWS + data.size());
				
				for (int y = 0; y < data.size(); y++) {
					for (int x = 0; x < data.get(y).getListViewColumnIds().length; x++) {
						final int index = data.get(y).getListViewColumnIds()[x];
						
						if (wasTableAlreadyFilled) {
							// update widget because it already exists
							if (table.getWidget(HEADER_ROWS + y, x) instanceof TextBox) {
								((TextBox) table.getWidget(HEADER_ROWS + y, x)).setText(data.get(y).getFieldValue(index).toString());
							}
						} else {
							// add a new widget and new click handler
							// TODO reuse existing widgets and handlers if possible instead of recreating them all the time
							table.setWidget(HEADER_ROWS + y, x, data.get(y).getFieldById(index).getWidget(view, data.get(y).getFieldValue(index)));
							addChangeEvents(index, table.getWidget(HEADER_ROWS + y, x));
						}
					}
				}
			} else {
				throw new RuntimeException("Expected DtoService received " + data.get(0).getClass());
			}
		}
	}
}
