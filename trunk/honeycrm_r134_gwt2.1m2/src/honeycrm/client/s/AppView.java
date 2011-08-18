package honeycrm.client.s;

import honeycrm.client.misc.Callback;
import honeycrm.client.s.AppPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
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
	@UiField Button logout;
	@UiField Label loading;
	
	private honeycrm.client.s.ModulePresenter.Display contactsView;
	private honeycrm.client.s.ModulePresenter.Display productView;
	private honeycrm.client.s.ModulePresenter.Display proposalsView; 

	public AppView(final honeycrm.client.s.ModulePresenter.Display contactsView, honeycrm.client.s.ModulePresenter.Display productView, honeycrm.client.s.ModulePresenter.Display proposalView) {
		this.contactsView = contactsView;
		this.productView = productView;
		this.proposalsView = proposalView;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		loading.setText(constants.loading());
		logout.setText(constants.logout());
		panel.add(new Label("dashboard content"), constants.moduleDashboard());
		panel.add(contactsView.asWidget(), constants.moduleContacts());
		panel.add(productView.asWidget(), constants.moduleProducts());
		panel.add(proposalView.asWidget(), constants.moduleProposals());
	}

	@Override
	public HasKeyPressHandlers getFocus() {
		return focus;
	}

	@UiFactory
	SimplePager makePager() {
		SimplePager p = new SimplePager(TextLocation.CENTER);
		return p;
	}
	
	@Override
	public HasClickHandlers getLogout() {
		return logout;
	}

	@Override
	public void selectTab(String module) {
		if ("Dashboard".equals(module)) {
			panel.selectTab(0);
		} else if ("Contact".equals(module)) {
			panel.selectTab(1);
		}
	}
	
	@Override
	public void toggleLoading(boolean isLoading) {
		loading.setVisible(isLoading);
	}

	@Override
	public void addTabChangeHandler(final Callback<Module> callback) {
		panel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				switch(event.getItem()) {
				case 0:
					callback.callback(Module.Dashboard);
					break;
				case 1:
					callback.callback(Module.Contact);
					break;
				case 2:
					callback.callback(Module.Product);
					break;
				case 3:
					callback.callback(Module.Proposal);
				}
			}
		});
	}

	@Override
	public void initModule(Module arg) {
		switch (arg) {
		case Contact:
			contactsView.init(null);
			break;
		case Product:
			productView.init(null);
			break;
		case Proposal:
			proposalsView.init(null);
			break;
		}
	}
}
