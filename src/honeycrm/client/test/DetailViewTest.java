package honeycrm.client.test;

import honeycrm.client.dto.DtoContact;
import honeycrm.client.view.DetailView;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;


public class DetailViewTest extends GWTTestCase {
	public void testA() {
		final DetailView d = new DetailView(DtoContact.class);
		d.refresh(1);
		assertNotNull(d);
		System.out.println("is not null");

		new Timer() {
			@Override
			public void run() {
				assertTrue(d.isVisible());
				System.out.println("is visible");
				finishTest();
			}
		}.schedule(200);

		delayTestFinish(1000);
	}

	@Override
	public String getModuleName() {
		return "honeycrm.Gae";
	}
}
