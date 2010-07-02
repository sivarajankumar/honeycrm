package honeycrm.client;

import honeycrm.client.dto.Dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoadingPanel extends Composite {
	private Label status = new Label();

	public LoadingPanel() {
		final Panel vpanel = new VerticalPanel();
		vpanel.setStyleName("loading_panel");
		// TODO add nice loading image
		vpanel.add(status);
		initWidget(vpanel);

		prefetchImages();
	}

	/**
	 * Pre-fetch all google web toolkit default images
	 */
	private void prefetchImages() {
		final String root = "gae/gwt/standard/images/";
		final Set<String> prefetchImages = new HashSet<String>();
		prefetchImages.add(root + "hborder.png");
		prefetchImages.add(root + "corner.png");
		prefetchImages.add(root + "corner.png");
		prefetchImages.add(root + "corner_ie6.png");
		prefetchImages.add(root + "hborder.png");
		prefetchImages.add(root + "hborder_ie6.png");
		prefetchImages.add(root + "ie6/corner_dialog_topleft.png");
		prefetchImages.add(root + "ie6/corner_dialog_topright.png");
		prefetchImages.add(root + "ie6/hborder_blue_shadow.png");
		prefetchImages.add(root + "ie6/hborder_gray_shadow.png");
		prefetchImages.add(root + "ie6/vborder_blue_shadow.png");
		prefetchImages.add(root + "ie6/vborder_gray_shadow.png");
		prefetchImages.add(root + "splitPanelThumb.png");
		prefetchImages.add(root + "vborder.png");
		prefetchImages.add(root + "vborder_ie6.png");

		int imageCount = 0;

		for (final String url : prefetchImages) {
			setStatus("Loading image #" + (++imageCount) + " of " + prefetchImages.size());
			Image.prefetch(url);
		}

		wakeupServer();
	}

	/**
	 * Do an initial call to the server side to wake it up.
	 */
	private void wakeupServer() {
		setStatus("Waking up server side..");

		ServiceRegistry.commonService().wakeupServer(new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				setStatus("Loading configuration..");
				
				ServiceRegistry.commonService().getDtoConfiguration(new AsyncCallback<List<Dto>>() {
					@Override
					public void onSuccess(List<Dto> result) {
						initRealUserInterface(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Could not get dto configuration from server side.");
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not wakeup server side. Please try again later.");
			}
		});
	}

	private void setStatus(final String statusString) {
		status.setText(statusString);
	}

	private void initRealUserInterface(List<Dto> dtos) {
		DtoRegistry.instance.setDtos(dtos);
		
		setStatus("Initiating user interface..");
		this.setVisible(false);
		// TODO this has no effect. i hope the user has a giant screen because currently scrolling
		// is not working at all.
		Window.enableScrolling(true);
		RootLayoutPanel.get().add(new TabLayout());
	}
}
