package crm.client.admin;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * Simple logging console allowing arbitrary widgets to log messages.
 */
public class LogConsole extends Composite {
	public static final boolean ENABLED = false;
	private static final LogConsole instance = new LogConsole();
	private final Label logWidget = new Label();

	public static LogConsole get() {
		return instance;
	}

	public static void log(final String str) {
		instance.logInternal(str);
	}

	private void logInternal(final String str) {
		logWidget.setText(logWidget.getText() + "\n" + str);
	}

	private LogConsole() {
		logWidget.setWidth("900px");
		logWidget.setHeight("30px");

		initWidget(logWidget);
	}
}
