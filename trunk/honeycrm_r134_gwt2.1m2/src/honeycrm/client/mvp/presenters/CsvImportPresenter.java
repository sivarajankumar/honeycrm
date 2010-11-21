package honeycrm.client.mvp.presenters;

import honeycrm.client.csv.CsvImporter;
import honeycrm.client.dto.Dto;
import honeycrm.client.mvp.events.UpdateEvent;
import honeycrm.client.services.CreateServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CsvImportPresenter implements Presenter {
	public interface Display {
		HasClickHandlers getCancelBtn();
		HasClickHandlers getImportBtn();
		HasText getStatus();
		HasText getHeader();
		HasValue<String> getTextArea();
		Widget asWidget();
		void center();
		void hide();
	}

	private final CreateServiceAsync createService;
	private final SimpleEventBus eventBus;
	private final Display view;
	private final String module;
	
	public CsvImportPresenter(final CreateServiceAsync createService, final SimpleEventBus eventBus, final Display view, final String module) {
		this.createService = createService;
		this.eventBus = eventBus;
		this.view = view;
		this.module = module;
		bind();
	}

	private void bind() {
		view.getCancelBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.hide();
			}
		});
		view.getImportBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				importDto(getDtoArrayFromText(view.getTextArea().getValue()), 0);
			}
		});
	}

	public Dto[] getDtoArrayFromText(final String text) {
		final CsvImporter importer = CsvImporter.get(module);
		return importer.parse(text);
	}
	
	public void importDto(final Dto[] dtos, final int currentIndex) {
		if (0 == currentIndex) {
			view.getStatus().setText("Status: Started Import");
		}
		
		createService.create(dtos[currentIndex], new AsyncCallback<Long>() {
			@Override
			public void onSuccess(Long result) {
				view.getStatus().setText("Status: Imported " + String.valueOf(currentIndex) + " / " + dtos.length);

				final boolean isImportDone = currentIndex == dtos.length - 1;

				if (isImportDone) {
					view.getStatus().setText("Status: Import completed");
					eventBus.fireEvent(new UpdateEvent(module));
				} else {
					importDto(dtos, 1 + currentIndex);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				view.getStatus().setText("Status: Import failed.");
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		view.center();
	}
}
