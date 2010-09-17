package honeycrm.client;

import honeycrm.client.basiclayout.Initializer;
import honeycrm.client.login.LoginScreen;
import honeycrm.client.misc.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Gae implements EntryPoint {
	@Override
	public void onModuleLoad() {
		final Label s = new Label("status: ");
		RootLayoutPanel.get().add(s);

		// if (false) {
		final LoginScreen login = new LoginScreen(new Callback() {
			@Override
			public void callback() {
				// insert a code splitting point here
				GWT.runAsync(new RunAsyncCallback() {
					@Override
					public void onSuccess() {
						// capture key codes like the following:
						/*
						 * final FocusPanel focus = new FocusPanel(new Initializer()); focus.addKeyDownHandler(new KeyDownHandler() {
						 * 
						 * @Override public void onKeyDown(KeyDownEvent event) { if (event.isShiftKeyDown() && event.getNativeKeyCode() == 16) { // == ?) { LogConsole.log("display help"); } } }); focus.setHeight("100%"); RootLayoutPanel.get().add(focus);
						 */
						RootLayoutPanel.get().add(new Initializer());
					}

					@Override
					public void onFailure(Throwable reason) {
						Window.alert("Could not additional honeycrm code for initialisation");
					}
				});
			}
		});
		RootLayoutPanel.get().add(login);
		login.center();

		/*GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				// only works online.. cannot test without internet
				VisualizationUtils.loadVisualizationApi(new Runnable() {
					@Override
					public void run() {
						s.setText(s.getText() + " viz");
					}
				}, Table.PACKAGE, LineChart.PACKAGE, ColumnChart.PACKAGE);

				ServiceRegistry.commonService().getDtoConfiguration(new AsyncCallback<Map<String, ModuleDto>>() {
					@Override
					public void onSuccess(final Map<String, ModuleDto> dtoConfiguration) {
						s.setText(s.getText() + " dtoconfig");
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Could not get dto configuration from server side.");
					}
				});

				ServiceRegistry.commonService().getRelationships(new AsyncCallback<Map<String, Map<String, Set<String>>>>() {
					@Override
					public void onSuccess(final Map<String, Map<String, Set<String>>> relationships) {
						s.setText(s.getText() + " relationships");
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Could not get relationship information from server side.");
					}
				});
			}

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub

			}
		});*/
		// }
	}
}
