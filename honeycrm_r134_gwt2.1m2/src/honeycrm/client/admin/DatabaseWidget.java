package honeycrm.client.admin;

import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.mvp.views.LoadView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DatabaseWidget extends Composite {
	private final Button deleteAllBtn = new Button("Delete all items");
	public DatabaseWidget() {
		final VerticalPanel panel = new VerticalPanel();
		
		final Button createBulkBtn = new Button("Create Bulk (Creates 100k items)");
		createBulkBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ServiceRegistry.commonService().bulkCreate(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("epic fail");
					}
				});
			}
		});
		
		final Button readBulkBtn = new Button("Read Bulk (does full table scan)");
		readBulkBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ServiceRegistry.commonService().bulkRead(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("epic fail");
					}
				});
			}
		});
		
		panel.add(createBulkBtn);
		panel.add(readBulkBtn);
		panel.add(deleteAllBtn);
		
		deleteAllBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ServiceRegistry.deleteService().deleteAllItems(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("delete all failed");
					}
				});
			}
		});

		initWidget(panel);
	}
}
