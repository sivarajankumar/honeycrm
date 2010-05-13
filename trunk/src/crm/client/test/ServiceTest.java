package crm.client.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import crm.client.CommonService;
import crm.client.CommonServiceAsync;
import crm.client.IANA;
import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.DtoContact;
import crm.client.dto.ListQueryResult;

// TODO this test fails when GWT tries to instantiate the common service implementation, i.e. during instantiation of the persistence manager.
public class ServiceTest extends AbstractClientTest {
	// cannot call GWT.create() within constructor / initializer code -> do it per test* method instead

	@Override
	protected void gwtSetUp() throws Exception {
	}

	public void testGetAll() {
		final CommonServiceAsync commonService = GWT.create(CommonService.class);

		commonService.getAll(IANA.mashal(DtoAccount.class), 0, 20, new AsyncCallback<ListQueryResult<? extends AbstractDto>>() {
			@Override
			public void onSuccess(ListQueryResult<? extends AbstractDto> result) {
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

	public void testAdd() {
		final CommonServiceAsync commonService = GWT.create(CommonService.class);

		DtoContact contact = new DtoContact();
		contact.setName("Testcontact");
		contact.setAccountID(1);
		contact.setCity("Testcity");
		contact.setEmail("a@b.com");
		contact.setPhone("123123");

		commonService.create(IANA.mashal(DtoContact.class), contact, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				fail();
			}

			@Override
			public void onSuccess(Void result) {
				finishTest();
			}
		});

		delayTestFinish(1000);
	}

	public void testSearch() {
		DtoContact contact = new DtoContact();
		contact.setName("T");

		final CommonServiceAsync commonService = GWT.create(CommonService.class);

		commonService.search(IANA.mashal(DtoAccount.class), contact, 0, 0, new AsyncCallback<ListQueryResult<? extends AbstractDto>>() {
			@Override
			public void onFailure(Throwable caught) {
				fail();
			}

			@Override
			public void onSuccess(ListQueryResult<? extends AbstractDto> result) {
				boolean foundCreatedContact = false;

				for (final AbstractDto item : result.getResults()) {
					if (item instanceof DtoContact && ((DtoContact) item).getName().equals("Testcontact")) {
						foundCreatedContact = true;
						break; // found
					}
				}

				assertTrue(foundCreatedContact);
				finishTest();
			}
		});

		delayTestFinish(1000);
	}
}
