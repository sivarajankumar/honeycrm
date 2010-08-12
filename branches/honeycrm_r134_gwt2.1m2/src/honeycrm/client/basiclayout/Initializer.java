package honeycrm.client.basiclayout;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.ServiceRegistry;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.Table;

// TODO bundle all startup specific requests into one single request
public class Initializer extends DockLayoutPanel {
	/**
	 * We need to be online to load visualizations. Allow developers to disable loading to be able to work off-line.
	 */
	public static final boolean SKIP_LOADING_VISUALISATIONS = true;
	private HTML status = new HTML();
	private long lastFinishTime = -1;

	public Initializer() {
		super(Unit.PX);
		status.setSize("400px", "400px");

		status.setStyleName("loading_panel");
		// TODO add nice loading image
		add(status);
		
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
				loadVisualisation();
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not wakeup server side. Please try again later.");
			}
		});
	}

	private void loadVisualisation() {
		if (SKIP_LOADING_VISUALISATIONS) {
			loadConfiguration();
		} else {
			setStatus("Loading visualisation API..");

			// only works online.. cannot test without internet
			VisualizationUtils.loadVisualizationApi(new Runnable() {
				@Override
				public void run() {
					loadConfiguration();
				}
			}, Table.PACKAGE, LineChart.PACKAGE, ColumnChart.PACKAGE);
		}
	}

	private void loadConfiguration() {
		setStatus("Loading configuration..");

		ServiceRegistry.commonService().getDtoConfiguration(new AsyncCallback<Map<String, ModuleDto>>() {
			@Override
			public void onSuccess(final Map<String, ModuleDto> dtoConfiguration) {
				loadRelationships(dtoConfiguration);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not get dto configuration from server side.");
			}
		});
	}

	private void loadRelationships(final Map<String, ModuleDto> dtoConfiguration) {
		setStatus("Loading relationships");

		ServiceRegistry.commonService().getRelationships(new AsyncCallback<Map<String, Map<String, Set<String>>>>() {
			@Override
			public void onSuccess(final Map<String, Map<String, Set<String>>> relationships) {
				initRealUserInterface(dtoConfiguration, relationships);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not get relationship information from server side.");
			}
		});
	}

	private void setStatus(final String statusString) {
		final String timing = (lastFinishTime > 0) ? (" +" + (System.currentTimeMillis() - lastFinishTime) + "ms<br />") : "<br />";
		status.setHTML(status.getHTML() + timing + statusString);
		lastFinishTime = System.currentTimeMillis();
	}

	private void initRealUserInterface(final Map<String, ModuleDto> dtoModuleData, final Map<String, Map<String, Set<String>>> relationships) {
		DtoModuleRegistry.create(dtoModuleData, relationships);

		setStatus("Initiating user interface..");
		remove(status);
		
		addNorth(new Header(), 40);
		add(TabCenterView.instance());
	}
}
