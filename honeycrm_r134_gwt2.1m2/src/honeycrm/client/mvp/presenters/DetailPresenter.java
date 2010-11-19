package honeycrm.client.mvp.presenters;

import java.util.HashMap;
import java.util.Map.Entry;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.User;
import honeycrm.client.mvp.events.CreateEvent;
import honeycrm.client.mvp.events.CreateEventHandler;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.mvp.events.OpenEventHandler;
import honeycrm.client.mvp.events.UpdateEvent;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class DetailPresenter implements Presenter {
	public interface Display {
		void setData(Dto dto);
		Dto getData();
		Widget asWidget();
		void setPresenter(DetailPresenter modulePresenter);
		void startCreate(HashMap<String, Object> prefilledFields);
		HasClickHandlers getCreateBtn();
		HasClickHandlers getSaveBtn();
		HasClickHandlers getEditBtn();
		HasClickHandlers getCancelBtn();
		void startEdit();
		RelationshipsPresenter.Display getRelationshipsView();
	}

	final Display view;
	final String module;
	final SimpleEventBus eventBus;
	final ReadServiceAsync readService;
	final UpdateServiceAsync updateService;
	final CreateServiceAsync createService;

	public DetailPresenter(final SimpleEventBus eventBus, final String module, final ReadServiceAsync readService, final UpdateServiceAsync updateService, final CreateServiceAsync createService, final Display view) {
		this.view = view;
		this.module = module;
		this.eventBus = eventBus;
		this.readService = readService;
		this.updateService = updateService;
		this.createService = createService;
		
		bind();
	}

	private void bind() {
		// Create a presenter that takes care of the RelationshipsView.
		new RelationshipsPresenter(module, view.getRelationshipsView(), readService, eventBus);

		view.getSaveBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onSave();
			}
		});
		
		view.getCancelBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//view.
			}
		});
		view.getEditBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.startEdit();
			}
		});
		view.getCreateBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.startCreate(getDefaultPrefilledFields());
			}

		});

		eventBus.addHandler(CreateEvent.TYPE, new CreateEventHandler() {
			@Override
			public void onCreate(CreateEvent event) {
				if (module.equals(event.getModule())) {
					final HashMap<String, Object> prefilledFields = getDefaultPrefilledFields();
					
					// add more prefilled fields
					if (null != event.getPrefilledFields()) {
						for (final Entry<String, Object> entry: event.getPrefilledFields().entrySet()) {
							prefilledFields.put(entry.getKey(), entry.getValue());
						}
					}
					
					// kommt an bringt aber nichts.
					view.startCreate(prefilledFields);
				}
			}
		});
		eventBus.addHandler(OpenEvent.TYPE, new OpenEventHandler() {
			@Override
			public void onOpen(OpenEvent event) {
				if (module.equals(event.getDto().getModule())) {
					// get item again from the server.
					// infact we could use the dto object that we received but it may not contain all the necessary fields
					// especially relate fields will not be resolved yet.
					readService.get(event.getDto().getModule(), event.getDto().getId(), new AsyncCallback<Dto>() {
						@Override
						public void onSuccess(Dto result) {
							view.setData(result);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
			}
		});

		view.setPresenter(this);
	}
	
	protected HashMap<String, Object> getDefaultPrefilledFields() {
		final HashMap<String, Object> prefilledFields = new HashMap<String, Object>();
		prefilledFields.put("assignedTo", User.getUserId());
		return prefilledFields;
	}

	public void onSave() {
		final Dto dto = view.getData();

		// id == -1 indicates that there is no id yet.
		// thus if id != -1 we know the id -> we do an update
		final boolean isUpdate = dto.getId() > 0;

		if (isUpdate) {
			updateService.update(dto, new AsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
					eventBus.fireEvent(new UpdateEvent(module));
					eventBus.fireEvent(new OpenEvent(dto));
				}

				@Override
				public void onFailure(Throwable caught) {

				}
			});
		} else {
			createService.create(dto, new AsyncCallback<Long>() {
				@Override
				public void onSuccess(Long result) {
					dto.setId(result);
					eventBus.fireEvent(new UpdateEvent(module));
					eventBus.fireEvent(new OpenEvent(dto));
				}

				@Override
				public void onFailure(Throwable caught) {

				}
			});
		}
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
