package honeycrm.client.test;

import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.DtoService;
import honeycrm.client.view.ServiceTableWidget;
import honeycrm.client.view.AbstractView.View;

import java.util.LinkedList;
import java.util.List;

public class ServiceTableTest extends AbstractClientTest {
	public void testSetData() {
		final List<AbstractDto> list = new LinkedList<AbstractDto>();

		final DtoService s = new DtoService();
		s.setName("a");
		s.setPrice(1.0);
		s.setQuantity(23);

		list.add(s);

		final ServiceTableWidget w = new ServiceTableWidget(View.EDIT);
		w.setData(list);
		w.getData();
	}
}
