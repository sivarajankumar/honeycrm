package honeycrm.client.test;

// TODO this test fails when GWT tries to instantiate the common service implementation, i.e. during
// instantiation of the persistence manager.
public class ServiceTest extends AbstractClientTest {
	// cannot call GWT.create() within constructor / initializer code -> do it per test* method
	// instead

	@Override
	protected void gwtSetUp() throws Exception {
	}

	/*
	 * public void testGetAll() { final CommonServiceAsync commonService = GWT.create(CommonService.class);
	 * 
	 * commonService.getAll(0, 20, new AsyncCallback<ListQueryResult<Dto>>() {
	 * 
	 * @Override public void onSuccess(ListQueryResult<Dto> result) { assertNotNull(result);
	 * 
	 * finishTest(); }
	 * 
	 * @Override public void onFailure(Throwable caught) { fail(); } });
	 * 
	 * delayTestFinish(1000); }
	 * 
	 * public void testAdd() { final CommonServiceAsync commonService = GWT.create(CommonService.class);
	 * 
	 * DtoContact contact = new DtoContact(); contact.setName("Testcontact"); contact.setAccountID(1); contact.setCity("Testcity"); contact.setEmail("a@b.com"); contact.setPhone("123123");
	 * 
	 * commonService.create(IANA.mashal(DtoContact.class), contact, new AsyncCallback<Long>() {
	 * 
	 * @Override public void onFailure(Throwable caught) { fail(); }
	 * 
	 * @Override public void onSuccess(Long result) { finishTest(); } });
	 * 
	 * delayTestFinish(1000); }
	 * 
	 * public void testSearch() { DtoContact contact = new DtoContact(); contact.setName("T");
	 * 
	 * final CommonServiceAsync commonService = GWT.create(CommonService.class);
	 * 
	 * commonService.search(contact, 0, 0, new AsyncCallback<ListQueryResult<Dto>>() {
	 * 
	 * @Override public void onFailure(Throwable caught) { fail(); }
	 * 
	 * @Override public void onSuccess(ListQueryResult<Dto> result) { boolean foundCreatedContact = false;
	 * 
	 * for (final Dto item : result.getResults()) { if (item.get("name").equals("Testcontact")) { foundCreatedContact = true; break; // found } }
	 * 
	 * assertTrue(foundCreatedContact); finishTest(); } });
	 * 
	 * delayTestFinish(1000); }
	 */
}
