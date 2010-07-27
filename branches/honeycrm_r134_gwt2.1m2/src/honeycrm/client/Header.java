package honeycrm.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

public class Header extends Composite {
	public Header() {
		Panel panel = new FlowPanel();
		// use style name honey_header to css name conflict with css code of disclosure panel
		panel.setStyleName("honey_header");

		/*
		 * logo | loadIndicator | header_links | | | | login | profile | help | searchPanel | ------------------------------------------------------------------
		 */

		Label logo = new Label("Honeeeeeeeyyyyy CRM");
		logo.setStyleName("header_logo");

		Panel links = new HorizontalPanel();
		links.addStyleName("header_links");
		// header_links.addStyleName("");

		Hyperlink login = new Hyperlink("Login", "Login");
		login.setStyleName("header_link");

		HTML pipe = new HTML(" | ");
		pipe.setStyleName("header_link");

		Hyperlink profile = new Hyperlink("Profile", "Profile");
		profile.setStyleName("header_link");

		HTML pipe2 = new HTML(" | ");
		pipe2.setStyleName("header_link");

		Hyperlink help = new Hyperlink("Help", "Help");
		help.setStyleName("header_link");

		HTML pipe3 = new HTML("&nbsp; | &nbsp;");
		pipe3.setStyleName("header_link");

		/*FlowPanel searchPanel = new FlowPanel();
		searchPanel.setStyleName("header_search");
*/
		Label searchLabel = new Label("Search");
		searchLabel.setStyleName("header_search_label");

/*		searchPanel.add(searchLabel);
		searchPanel.add(new FulltextSearchWidget());
		searchPanel.add(new HTML("<div class='clear'></div>"));
*/
		// from right to left

		final Panel searchPanel = new FlowPanel();
		searchPanel.addStyleName("header_search");
		searchPanel.add(searchLabel);
		searchPanel.add(new FulltextSearchWidget());
		
		final Panel p = new HorizontalPanel();
		p.setWidth("100%");
		
		p.add(logo);
		p.add(LoadIndicator.get());
		p.add(links);
		
		// p.add(searchPanel);
		
		links.add(help);
		links.add(pipe2);
		links.add(profile);
		links.add(pipe);
		links.add(login);
		links.add(pipe3);
		links.add(searchLabel);
		links.add(new FulltextSearchWidget());
		
		
		
		// header_links.add(new HTML("<div class='clear'></div>"));

		initWidget(p);
		
/*		panel.add(logo);
		panel.add(LoadIndicator.get());
		panel.add(header_links);
		panel.add(new HTML("<div class='clear'></div>"));
		initWidget(panel);
*/	}
}
