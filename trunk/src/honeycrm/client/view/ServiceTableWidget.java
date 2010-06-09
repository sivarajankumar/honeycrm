package honeycrm.client.view;

import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.DtoService;
import honeycrm.client.view.AbstractView.View;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

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
						table.setWidget(rows, x, WidgetSelector.getWidgetByType(dto.getClass(), dto, dto.getListViewColumnIds()[x], view));
					}
				}
			});
			
			panel.add(addBtn);
		}

		initWidget(panel);
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

			services.add(s);
		}

		return services;
	}

	@Override
	public void setData(List<? extends AbstractDto> data) {
		if (!data.isEmpty()) {
			if (data.get(0) instanceof DtoService) {
				for (int y = 0; y < data.size(); y++) {
					for (int x = 0; x < data.get(y).getListViewColumnIds().length; x++) {
						table.setWidget(HEADER_ROWS + y, x, WidgetSelector.getWidgetByType(data.get(y).getClass(), data.get(y), data.get(y).getListViewColumnIds()[x], view));
					}
				}
			} else {
				throw new RuntimeException("Expected DtoService received " + data.get(0).getClass());
			}
		}
	}
}
