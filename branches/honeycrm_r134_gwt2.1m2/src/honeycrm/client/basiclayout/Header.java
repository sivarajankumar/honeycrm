package honeycrm.client.basiclayout;

import honeycrm.client.view.FulltextSearchWidget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class Header extends Composite {
	public Header() {
		/*
		 * logo | loadIndicator | header_links | | | | login | profile | help | searchPanel | ------------------------------------------------------------------
		 */

		final Panel p = (Panel) addStyle(new FlowPanel(), "honey_header", "with_margin");

		p.add(addStyle(new Label("Honeeeeeeeyyyyy CRM"), "header_logo"));
		p.add(addStyle(new FulltextSearchWidget(), "header_search"));
		p.add(getHeaderLinks("Login", "Profile", "Help", "Global Search"));
		p.add(LoadIndicator.get());
		p.add(new HTML("<div class='clear'></div>"));

		initWidget(p);
	}

	private Widget getHeaderLink(final String label) {
		return addStyle(new Hyperlink(label, label), "header_link");
	}

	private Widget getHeaderLinks(final String... labels) {
		final Panel p = (Panel) addStyle(new FlowPanel(), "header_links");

		for (int i = 0; i < labels.length; i++) {
			p.add(getHeaderLink(labels[labels.length - i - 1]));

			if (i < labels.length - 1) {
				p.add(addStyle(new HTML("&nbsp; | &nbsp;"), "right"));
			}
		}

		return p;
	}
	
	/**
	 * Add the specified styles to the given widget.
	 */
	protected Widget addStyle(final Widget widget, final String... styles) {
		for (final String style : styles) {
			widget.addStyleName(style);
		}
		return widget;
	}
}
