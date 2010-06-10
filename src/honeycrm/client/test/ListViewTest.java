package honeycrm.client.test;

import honeycrm.client.dto.DtoContact;
import honeycrm.client.view.ListView;

import com.google.gwt.user.client.Timer;

public class ListViewTest extends AbstractClientTest {
	public void testColumnConsistency() {
		final ListView lv = new ListView(DtoContact.class);
		assertNotNull(lv);
		
		new Timer() {
			@Override
			public void run() {
				assertTrue(1 == lv.currentPage());
				finishTest();
			}
		}.schedule(1000);

		delayTestFinish(Integer.MAX_VALUE);
	}
}
