package honeycrm.client.mvp.views;

import honeycrm.client.login.User;
import honeycrm.client.misc.WidgetJuggler;
import honeycrm.client.mvp.presenters.HeaderPresenter.Display;
import honeycrm.client.view.FulltextSearchWidget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class HeaderView extends Composite  implements Display {
	private final LoadView loadView;
	
	public HeaderView() {
		/*
		 * logo | loadIndicator | header_links | | | | login | profile | help | searchPanel | ------------------------------------------------------------------
		 */
		final Panel container = (Panel) WidgetJuggler.addStyles(new FlowPanel(), "honey_header", "with_margin");
		final FlowPanel panel = new FlowPanel();
		final Panel p = (Panel) WidgetJuggler.addStyles(new FlowPanel(), "honey_header_left");
		initWidget(container);
		p.add(WidgetJuggler.addStyles(new Label("Honeeeeeeeyyyyy CRM"), "header_logo"));
		p.add(WidgetJuggler.addStyles(new Label("Welcome, " + User.getLogin() + "!"), "welcome_user"));
		p.add(loadView = new LoadView());
		//p.add(new HaveABreakGadget().getWidget());
		
		panel.addStyleName("honey_header with_margin");
		final Panel search_con = (Panel) WidgetJuggler.addStyles(new FlowPanel(), "honey_header_right");
		final Panel search = (Panel) WidgetJuggler.addStyles(new FlowPanel(), "header_search");
		search.add(WidgetJuggler.addStyles(new FulltextSearchWidget(), "header_search_input"));
		
		search_con.add(search);
		search_con.add(getHeaderLinks("Logout |", "Profile |", "Help "));
		
		panel.add(p);
		panel.add(search_con);
		panel.add(new HTML("<div class='clear'></div>"));
		container.add(panel);
		
	}

	private Widget getHeaderLink(final String label) {
		final String historyToken = label.split(" ")[0].toLowerCase();
		return WidgetJuggler.addStyles(new Hyperlink(label, historyToken), "header_link");
	}

	private Widget getHeaderLinks(final String... labels) {
		final Panel p = (Panel) WidgetJuggler.addStyles(new FlowPanel(), "header_links");

		for (int i = 0; i < labels.length; i++) {
			p.add(getHeaderLink(labels[labels.length - i - 1]));

			/*if (i < labels.length - 1) {
				p.add(WidgetJuggler.addStyles(new HTML("&nbsp; | &nbsp;"), "right"));
			}*/
		}

		return p;
	}

	@Override
	public honeycrm.client.mvp.presenters.LoadPresenter.Display getLoadView() {
		return loadView;
	}
}
