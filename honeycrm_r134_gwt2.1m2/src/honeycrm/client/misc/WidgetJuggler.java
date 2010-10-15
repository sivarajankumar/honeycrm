package honeycrm.client.misc;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class WidgetJuggler {
	/**
	 * Add all given child widgets to the container panel.
	 */
	public static void addToContainer(final Panel container, final Widget... children) {
		for (final Widget child: children) {
			container.add(child);
		}
	}
	
	/**
	 * Add the specified styles to the given widget.
	 */
	public static Widget addStyles(final Widget widget, final String... styles) {
		for (final String style : styles) {
			widget.addStyleName(style);
		}
		return widget;
	}
	
	public static void setVisible(final boolean visible, final Widget... widgets) {
		for (final Widget widget: widgets) {
			widget.setVisible(visible);
		}
	}
	
	/**
	 * Returns a button with the specified label, click handler and optional styles.
	 */
	public static Button getButton(final String label, final ClickHandler clickHandler, final String ... styles) {
		final Button button = new Button(label);
		button.addClickHandler(clickHandler);
		return (Button) addStyles(button, styles);
	}
}
