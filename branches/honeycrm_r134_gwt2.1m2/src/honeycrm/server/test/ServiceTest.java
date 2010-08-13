package honeycrm.server.test;

import honeycrm.client.services.CommonService;
import honeycrm.client.services.CommonServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ServiceTest extends GWTTestCase {
	public void testService() {
		final CommonServiceAsync commonService = GWT.create(CommonService.class);

		commonService.deleteAllItems(new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				assertNotNull(result);
				finishTest();
			}

			@Override
			public void onFailure(Throwable caught) {
				fail();
			}
		});

		delayTestFinish(1000);
	}

	@Override
	public String getModuleName() {
		return "honeycrm.Gae";
	}
}
