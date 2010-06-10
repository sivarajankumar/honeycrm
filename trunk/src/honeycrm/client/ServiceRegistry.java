package honeycrm.client;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates the instantiation of services. Clients should retrieve services from here instead of creating them on their own.
 */
public class ServiceRegistry {
	private static boolean testingMode = false;
	private static CommonServiceAsync commonService = null;

	public static CommonServiceAsync commonService() {
		// If we are in testing mode assume another commonService instance has been injected for testing. Return the common service instance.
		// If we are not in testing mode and the commonService has not been initialized yet, initialize and return it.
		if (!testingMode && null == commonService) {
			commonService = GWT.create(CommonService.class);
		}
		return commonService;
	}

	/**
	 * This updates the service instance used by all widgets. This is only used for testing when we want to try instantiating the widgets with a custom service implementation.
	 */
	public static void injectCommonService(final CommonServiceAsync newCommonService) {
		ServiceRegistry.commonService = newCommonService;
		ServiceRegistry.testingMode = true;
	}
}
