package honeycrm.client.misc;

import honeycrm.client.services.AuthService;
import honeycrm.client.services.AuthServiceAsync;
import honeycrm.client.services.CommonService;
import honeycrm.client.services.CommonServiceAsync;
import honeycrm.client.services.ConfigService;
import honeycrm.client.services.ConfigServiceAsync;
import honeycrm.client.services.CreateService;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.DeleteService;
import honeycrm.client.services.DeleteServiceAsync;
import honeycrm.client.services.PluginService;
import honeycrm.client.services.PluginServiceAsync;
import honeycrm.client.services.ReadService;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.ReportService;
import honeycrm.client.services.ReportServiceAsync;
import honeycrm.client.services.UpdateService;
import honeycrm.client.services.UpdateServiceAsync;
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
	private static CreateServiceAsync createService = null;
	private static ReadServiceAsync readService = null;
	private static UpdateServiceAsync updateService = null;
	private static DeleteServiceAsync deleteService = null;
	private static PluginServiceAsync pluginService = null;

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

	public static ReadServiceAsync readService() {
		if (null == readService) {
			readService = GWT.create(ReadService.class);
		}
		return readService;
	}

	/**
	 * This updates the service instance used by all widgets. This is only used for testing when we want to try instantiating the widgets with a custom service implementation.
	 */
	public static void injectCommonService(final CommonServiceAsync newCommonService) {
		ServiceRegistry.commonService = newCommonService;
		ServiceRegistry.testingMode = true;
	}

	public static UpdateServiceAsync updateService() {
		if (null == updateService) {
			updateService = GWT.create(UpdateService.class);
		}
		return updateService;
	}

	public static CreateServiceAsync createService() {
		if (null == createService) {
			createService = GWT.create(CreateService.class);
		}
		return createService;
	}

	public static DeleteServiceAsync deleteService() {
		if (null == deleteService) {
			deleteService = GWT.create(DeleteService.class);
		}
		return deleteService;
	}

	public static PluginServiceAsync pluginService() {
		if (null == pluginService) {
			pluginService = GWT.create(PluginService.class);
		}
		return pluginService;
	}
}
