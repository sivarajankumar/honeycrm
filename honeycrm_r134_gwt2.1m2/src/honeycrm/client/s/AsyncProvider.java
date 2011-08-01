package honeycrm.client.s;

import honeycrm.client.misc.Callback;
import honeycrm.client.services.CreateService;
import honeycrm.client.services.CreateServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;

public class AsyncProvider {
	private static CreateServiceAsync createService = null;
	
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
}
