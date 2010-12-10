package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.misc.User;
import honeycrm.client.mvp.presenters.HeaderPresenter.Display;
import honeycrm.client.services.ReadServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HeaderView extends Composite implements Display {
	private static HeaderViewUiBinder uiBinder = GWT.create(HeaderViewUiBinder.class);

	interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
	}

	@UiField Label welcome;
	@UiField LoadView loadView;
	@UiField FlowPanel panel;
	@UiField Hyperlink help;
	@UiField Hyperlink report;
	@UiField Hyperlink misc;
	@UiField Hyperlink profile;
	@UiField Hyperlink logout;
	
	private final ReadServiceAsync readService;
	private final LocalizedMessages constants;

	public HeaderView(final ReadServiceAsync readService, final LocalizedMessages constants) {
		this.readService = readService;
		this.constants = constants;

		initWidget(uiBinder.createAndBindUi(this));
	
		help.setText(constants.help());
		report.setText(constants.reports());
		misc.setText(constants.misc());
		profile.setText(constants.profile());
		logout.setText(constants.logout());
		welcome.setText(constants.welcome(User.getLogin()));
	}

	@UiFactory
	FulltextSearchView makeFulltextSearchWidget() {
		return new FulltextSearchView(readService, constants);
	}
	
	@UiFactory
	LoadView makeLoadView() {
		return new LoadView(constants);
	}

	@Override
	public honeycrm.client.mvp.presenters.LoadPresenter.Display getLoadView() {
		return loadView;
	}

	@Override
	public void attachPluginWidget(Widget w) {
		panel.add(w);
	}
}
