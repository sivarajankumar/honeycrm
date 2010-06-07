package honeycrm.client;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates the instantiation of services. Clients should retrieve services from here instead of
 * creating them on their own.
 */
public class ServiceRegistry {
	private static final CommonServiceAsync commonService = GWT.create(CommonService.class);

	public static CommonServiceAsync commonService() {
		return commonService;
	}
}
