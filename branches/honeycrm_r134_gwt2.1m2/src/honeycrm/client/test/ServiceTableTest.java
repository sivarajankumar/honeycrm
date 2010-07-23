package honeycrm.client.test;

import honeycrm.client.dto.Dto;
import honeycrm.client.view.ServiceTableWidget;
import honeycrm.client.view.AbstractView.View;

import java.util.LinkedList;
import java.util.List;

public class ServiceTableTest extends AbstractClientTest {
	public void testSetData() {
		final List<Dto> list = new LinkedList<Dto>();

		final Dto s = new Dto();
		s.set("name", "a");
		s.set("price", 1.0);
		s.set("quantity", 23);

		list.add(s);

		final ServiceTableWidget w = new ServiceTableWidget(View.EDIT);
		w.setData(list);
		w.getData();
	}
}
