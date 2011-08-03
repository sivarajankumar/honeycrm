package honeycrm.client.s;

import honeycrm.client.misc.Callback;
import honeycrm.client.services.CreateService;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.ReadService;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateService;
import honeycrm.client.services.UpdateServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;

public class AsyncProvider {
	private static CreateServiceAsync createService = null;
	private static UpdateServiceAsync updateService = null;
	private static ReadServiceAsync readService = null;
	
	public static void getCreateService(final Callback<CreateServiceAsync> callback) {
		if (null == createService) {
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onSuccess() {
					callback.callback(createService = GWT.create(CreateService.class));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.alert(AsyncProvider.class.getName() + " dümdümm");
				}
			});
		} else {
			callback.callback(createService);
		}
	}

	public static void getReadService(final Callback<ReadServiceAsync> callback) {
		if (null == readService) {
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onSuccess() {
					callback.callback(readService = GWT.create(ReadService.class));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.alert(AsyncProvider.class.getName() + " dümdümm");
				}
			});
		} else {
			callback.callback(readService);
		}
	}

	public static void getUpdateService(final Callback<UpdateServiceAsync> callback) {
		if (null == updateService) {
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onSuccess() {
					callback.callback(updateService = GWT.create(UpdateService.class));
				}
				
				@Override
				public void onFailure(Throwable reason) {
				}
			});
		} else {
			callback.callback(updateService);
		}
	}
}
