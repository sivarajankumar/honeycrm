package honeycrm.client.admin;

import honeycrm.client.basiclayout.LoadIndicator;
import honeycrm.client.misc.ServiceRegistry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

class DatabaseWidget extends Composite {
	public DatabaseWidget() {
		final VerticalPanel panel = new VerticalPanel();

		panel.add(getCreateBulkButton());
		panel.add(getReadBulkButton());
		panel.add(getDeleteAllButton());

		initWidget(panel);
	}

	private Button getDeleteAllButton() {
		final Button deleteAllBtn = new Button("Delete all items");
		deleteAllBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				LoadIndicator.get().startLoading();

				ServiceRegistry.commonService().deleteAllItems(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(final Void result) {
						LoadIndicator.get().endLoading();
					}

					@Override
					public void onFailure(final Throwable caught) {
						Window.alert("delete all failed");
						LoadIndicator.get().endLoading();
					}
				});
			}
		});
		return deleteAllBtn;
	}

	private Button getReadBulkButton() {
		final Button readBulkBtn = new Button("Read Bulk (does full table scan)");
		readBulkBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				ServiceRegistry.commonService().bulkRead(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(final Void result) {
					}

					@Override
					public void onFailure(final Throwable caught) {
						Window.alert("epic fail");
					}
				});
			}
		});
		return readBulkBtn;
	}

	private Button getCreateBulkButton() {
		final Button createBulkBtn = new Button("Create Bulk (Creates 100k items)");
		createBulkBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				ServiceRegistry.commonService().bulkCreate(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(final Void result) {
					}

					@Override
					public void onFailure(final Throwable caught) {
						Window.alert("epic fail");
					}
				});
			}
		});
		return createBulkBtn;
	}
}
