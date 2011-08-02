package honeycrm.client.s;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.Callback;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.services.CreateServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;

public class ContactsPresenter extends AbstractPresenter {
	public interface Display extends AbstractPresenterDisplay {
		HasClickHandlers getCreate();

		HasClickHandlers getSaveBtn();
		
		Dto getContact();
		
		SelectionModel<Dto> getSelectionHandler();

		Dto getSelectedObject();

		void openView(Dto selectedObject);
	}

	public ContactsPresenter(final SimpleEventBus bus, final Display view) {
		this.view = view;
		view.getSelectionHandler().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				view.openView(view.getSelectedObject());
			}
		});
		view.getSaveBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AsyncProvider.getCreateService(new Callback<CreateServiceAsync>() {
					@Override
					public void callback(CreateServiceAsync arg) {
						arg.create(view.getContact(), new AsyncCallback<Long>() {
							@Override
							public void onSuccess(Long result) {
								Window.alert("success");
							}
							
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("fail");
							}
						});
					}
				});
			}
		});
	}
}
