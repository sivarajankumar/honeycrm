package honeycrm.client.view;

import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.DtoService;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class ServiceTableWidget extends ITableWidget {
	private final FlexTable table = new FlexTable();

	public ServiceTableWidget() {
		final String[] header = new String[] { "Name", "Price", "Qty", "Discount", "Sum" };
		
		for (int x = 0; x < header.length; x++) {
			table.setWidget(0, x, new Label(header[x]));
		}
		
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < header.length; x++) {
				final TextBox box = new TextBox();
				box.setText(header[x] + " " + Integer.toString(x * y));
				box.setWidth("50px");
				table.setWidget(1 + y, x, box);
			}
		}
		
		initWidget(table);
	}

	@Override
	public List<? extends AbstractDto> getData() {
		final List<DtoService> services = new LinkedList<DtoService>();
		for (int i = 0; i < 5; i++) {
			final DtoService s = new DtoService();
			s.setName("Service " + i);
			s.setPrice(i);
			services.add(s);
		}
		return services;
	}

	@Override
	public void setData(List<? extends AbstractDto> data) {

	}
}
