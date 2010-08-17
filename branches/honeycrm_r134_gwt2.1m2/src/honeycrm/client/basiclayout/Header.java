package honeycrm.client.basiclayout;

import honeycrm.client.login.User;
import honeycrm.client.misc.WidgetJuggler;
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

		final Panel p = (Panel) WidgetJuggler.addStyles(new FlowPanel(), "honey_header", "with_margin");

		p.add(WidgetJuggler.addStyles(new Label("Honeeeeeeeyyyyy CRM"), "header_logo"));
		p.add(WidgetJuggler.addStyles(new FulltextSearchWidget(), "header_search"));
		p.add(getHeaderLinks("Profile", "Help", "Global Search"));
		p.add(WidgetJuggler.addStyles(new Label("Welcome, " + User.getLogin() + "!"), "right"));
		p.add(LoadIndicator.get());
		p.add(new HTML("<div class='clear'></div>"));

		initWidget(p);
	}

	private Widget getHeaderLink(final String label) {
		return WidgetJuggler.addStyles(new Hyperlink(label, label), "header_link");
	}

	private Widget getHeaderLinks(final String... labels) {
		final Panel p = (Panel) WidgetJuggler.addStyles(new FlowPanel(), "header_links");

		for (int i = 0; i < labels.length; i++) {
			p.add(getHeaderLink(labels[labels.length - i - 1]));

			if (i < labels.length - 1) {
				p.add(WidgetJuggler.addStyles(new HTML("&nbsp; | &nbsp;"), "right"));
			}
		}

		return p;
	}
}
