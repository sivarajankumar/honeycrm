package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.mvp.presenters.ModulePresenter.Display;
import honeycrm.client.services.ReadServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ModuleView extends Composite implements Display {
	private static ModuleViewUiBinder uiBinder = GWT.create(ModuleViewUiBinder.class);
	interface ModuleViewUiBinder extends UiBinder<Widget, ModuleView> {
	}

	@UiField
	ListView list;
	@UiField
	honeycrm.client.mvp.views.DetailView detail;
	@UiField
	ModuleButtonBarView moduleButtonBar;

	final String module;
	private final ReadServiceAsync readService;
	private final LocalizedMessages constants;

	public ModuleView(final String module, final ReadServiceAsync readService, final LocalizedMessages constants) {
		this.readService = readService;
		this.constants = constants;
		this.module = module; // IMPORTANT: set module _before_ all other initialisation
		this.moduleButtonBar = new ModuleButtonBarView();

		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public ListView getList() {
		return list;
	}
	
	@Override
	public honeycrm.client.mvp.views.DetailView getDetail() {
		return detail;
	}

	@UiFactory
	honeycrm.client.mvp.views.DetailView makeDetailView() {
		return detail = new honeycrm.client.mvp.views.DetailView(module, readService, constants);
	}

	@UiFactory
	ListView makeListView() {
		return new ListView(module, readService);
	}

	@Override
	public honeycrm.client.mvp.presenters.ModuleButtonBarPresenter.Display getModuleButtonBar() {
		return moduleButtonBar;
	}
}
