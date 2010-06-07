package honeycrm.client.admin;

import honeycrm.client.CommonServiceAsync;
import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;


public class DatabaseWidget extends Composite {
	private static final CommonServiceAsync commonService = ServiceRegistry.commonService();
	private final Button deleteAllBtn = new Button("Delete all items");

	public DatabaseWidget() {
		deleteAllBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				LoadIndicator.get().startLoading();

				commonService.deleteAllItems(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						LoadIndicator.get().endLoading();
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("delete all failed");
						LoadIndicator.get().endLoading();
					}
				});
			}
		});

		initWidget(deleteAllBtn);
	}
}
