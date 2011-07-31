package honeycrm.client.s;

import honeycrm.client.s.AppPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class AppView extends LocalizedView implements Display {

	private static AppViewUiBinder uiBinder = GWT.create(AppViewUiBinder.class);

	interface AppViewUiBinder extends UiBinder<Widget, AppView> {
	}

	@UiField FocusPanel focus;
	@UiField TabLayoutPanel panel;
	
	public AppView() {
		initWidget(uiBinder.createAndBindUi(this));
		panel.add(new Label("dashboard content"), constants.moduleDashboard());
		panel.add(new Label("contacts content"), constants.moduleContacts());
	}

	@Override
	public HasKeyPressHandlers getFocus() {
		return focus;
	}
	
	@UiFactory SimplePager makePager() {
		SimplePager p = new SimplePager(TextLocation.CENTER);
		return p;
	}
}
