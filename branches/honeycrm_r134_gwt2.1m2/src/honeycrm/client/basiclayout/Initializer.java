package honeycrm.client.basiclayout;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.ServiceRegistry;

import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
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
		
		setStatus("Starting up..");
		loadVisualisation();
	}
	
	private void loadVisualisation() {
		/*GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				VisualizationUtils.loadVisualizationApi(new Runnable() {
					@Override
					public void run() {
						// init reports now because we loaded the charts
						loadConfiguration();
					}
				}, Table.PACKAGE, LineChart.PACKAGE, ColumnChart.PACKAGE);				
			}
			
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not load visualisation api");
			}
		});*/
		
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
