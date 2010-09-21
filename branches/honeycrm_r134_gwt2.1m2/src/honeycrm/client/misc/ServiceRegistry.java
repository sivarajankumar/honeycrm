package honeycrm.client.misc;

import honeycrm.client.services.AuthService;
import honeycrm.client.services.AuthServiceAsync;
import honeycrm.client.services.CommonService;
import honeycrm.client.services.CommonServiceAsync;
import honeycrm.client.services.ConfigService;
import honeycrm.client.services.ConfigServiceAsync;
import honeycrm.client.services.ReportService;
import honeycrm.client.services.ReportServiceAsync;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates the instantiation of services. Clients should retrieve services from here instead of creating them on their own.
 */
public class ServiceRegistry {
	private static boolean testingMode = false;
	private static CommonServiceAsync commonService = null;
	private static AuthServiceAsync authService = null;
	private static ConfigServiceAsync configService = null;
	private static ReportServiceAsync reportService = null;
	
	public static CommonServiceAsync commonService() {
		// If we are in testing mode assume another commonService instance has been injected for testing. Return the common service instance.
		// If we are not in testing mode and the commonService has not been initialized yet, initialize and return it.
		if (!testingMode && null == commonService) {
			commonService = GWT.create(CommonService.class);
		}
		return commonService;
	}
	
	public static ConfigServiceAsync configService() {
		if (null == configService) {
			configService = GWT.create(ConfigService.class);
		}
		return configService;
	}
	
	public static ReportServiceAsync reportService() {
		if (null == reportService) {
			reportService = GWT.create(ReportService.class);
		}
		return reportService;
	}
	
	public static AuthServiceAsync authService() {
		if (null == authService) {
			authService = GWT.create(AuthService.class);
		}
		return authService;
	}

	/**
	 * This updates the service instance used by all widgets. This is only used for testing when we want to try instantiating the widgets with a custom service implementation.
	 */
	public static void injectCommonService(final CommonServiceAsync newCommonService) {
		ServiceRegistry.commonService = newCommonService;
		ServiceRegistry.testingMode = true;
	}
}
