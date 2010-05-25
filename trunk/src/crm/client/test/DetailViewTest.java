package crm.client.test;

import com.google.gwt.junit.client.GWTTestCase;

import crm.client.dto.DtoContact;
import crm.client.view.DetailView;

public class DetailViewTest extends GWTTestCase {
	public void testA() {
		DetailView d = new DetailView(DtoContact.class);
		assertNotNull(d);
	}

	@Override
	public String getModuleName() {
		return "crm.client.Gae";
	}
}
