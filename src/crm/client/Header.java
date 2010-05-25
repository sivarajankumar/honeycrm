package crm.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;

public class Header extends Composite {
	public Header() {
		FlowPanel panel = new FlowPanel();
		panel.setStyleName("header");

		/*
		 * logo | loadIndicator | header_links | | | | login | profile | help | searchPanel | ------------------------------------------------------------------
		 */

		Label logo = new Label("Honeeeeeeeyyyyy CRM");
		logo.setStyleName("header_logo");

		FlowPanel header_links = new FlowPanel();
		header_links.setStyleName("header_links");

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
		
		FlowPanel searchPanel = new FlowPanel();
		searchPanel.setStyleName("header_search");

		Label searchLabel = new Label("Search");
		searchLabel.setStyleName("header_search_label");

		searchPanel.add(searchLabel);
		searchPanel.add(new FulltextSearchWidget());
		searchPanel.add(new HTML("<div class='clear'></div>"));

		// from right to left
		header_links.add(searchPanel);
		header_links.add(pipe3);
		header_links.add(help);
		header_links.add(pipe2);
		header_links.add(profile);
		header_links.add(pipe);
		header_links.add(login);
		header_links.add(new HTML("<div class='clear'></div>"));

		panel.add(logo);
		panel.add(LoadIndicator.get());
		panel.add(header_links);
		panel.add(new HTML("<div class='clear'></div>"));
		initWidget(panel);
	}
}
