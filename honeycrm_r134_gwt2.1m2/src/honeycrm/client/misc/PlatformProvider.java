package honeycrm.client.misc;

import honeycrm.client.mvp.presenters.HeaderPresenter.Display;
import honeycrm.client.mvp.views.Platform;
import honeycrm.client.plugin.IPlatform;

public class PlatformProvider {
	private static IPlatform p;

	private PlatformProvider() {
	}

	public static void registerHeader(final Display headerView) {
		p = new Platform(headerView, null);
	}

	public static IPlatform platform() {
		return p;
	}
}
