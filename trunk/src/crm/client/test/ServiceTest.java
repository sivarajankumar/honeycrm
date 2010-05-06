package crm.client.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import crm.client.CommonService;
import crm.client.CommonServiceAsync;
import crm.client.IANA;
import crm.client.dto.DtoAccount;
import crm.client.dto.ListQueryResult;
import crm.client.dto.Viewable;

public class ServiceTest extends AbstractClientTest {
	public void testService() {
		final CommonServiceAsync commonService = GWT.create(CommonService.class);

		commonService.getAll(IANA.mashal(DtoAccount.class), 0, 20, new AsyncCallback<ListQueryResult<? extends Viewable>>() {
			@Override
			public void onSuccess(ListQueryResult<? extends Viewable> result) {
				assertNotNull(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fail();
			}
		});
	}
}
