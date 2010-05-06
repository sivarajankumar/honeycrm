package crm.client.test;

import com.google.gwt.user.client.Timer;

import crm.client.dto.DtoContact;
import crm.client.view.ListView;

public class ListViewTest extends AbstractClientTest {
	public void testColumnConsistency() {
		final ListView lv = new ListView(DtoContact.class);

		Timer loadTimer = new Timer() {
			@Override
			public void run() {
				assert(0 == lv.currentPage());
				finishTest();
			}
		};
		loadTimer.schedule(250);
	}
}
