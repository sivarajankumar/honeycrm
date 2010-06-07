package honeycrm.client.test;

import honeycrm.client.dto.DtoContact;
import honeycrm.client.view.ListView;

import com.google.gwt.user.client.Timer;


public class ListViewTest extends AbstractClientTest {
	public void testColumnConsistency() {
		final ListView lv = new ListView(DtoContact.class);

		Timer loadTimer = new Timer() {
			@Override
			public void run() {
				assert (0 == lv.currentPage());
				finishTest();
			}
		};
		loadTimer.schedule(250);
	}
}
