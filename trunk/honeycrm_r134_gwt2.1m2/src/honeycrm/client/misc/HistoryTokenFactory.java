package honeycrm.client.misc;

import honeycrm.client.view.ModuleAction;

/**
 * Little helper creating the history tokens for different actions that may appear. This is used to change the url and propagate change across the application.
 */
public class HistoryTokenFactory {
	public static String get(String module, ModuleAction action, Object... additionalParameter) {
		final String appendix = 0 == additionalParameter.length ? "" : " " + CollectionHelper.join(" ", additionalParameter);
		return module + " " + action.toString().toLowerCase() + appendix;
	}
}
