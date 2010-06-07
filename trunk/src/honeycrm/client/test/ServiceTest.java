package honeycrm.client.test;

import honeycrm.client.CommonService;
import honeycrm.client.CommonServiceAsync;
import honeycrm.client.IANA;
import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.DtoAccount;
import honeycrm.client.dto.DtoContact;
import honeycrm.client.dto.ListQueryResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


// TODO this test fails when GWT tries to instantiate the common service implementation, i.e. during
// instantiation of the persistence manager.
public class ServiceTest extends AbstractClientTest {
	// cannot call GWT.create() within constructor / initializer code -> do it per test* method
	// instead

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

		commonService.create(IANA.mashal(DtoContact.class), contact, new AsyncCallback<Long>() {
			@Override
			public void onFailure(Throwable caught) {
				fail();
			}

			@Override
			public void onSuccess(Long result) {
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
