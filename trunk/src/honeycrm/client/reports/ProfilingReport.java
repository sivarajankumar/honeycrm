package honeycrm.client.reports;

import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.profiling.ServiceCallStatistics;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.google.gwt.visualization.client.visualizations.ColumnChart.Options;

public class ProfilingReport extends Composite {
	public ProfilingReport() {
		final VerticalPanel panel = new VerticalPanel();

		LoadIndicator.get().startLoading();

		ServiceRegistry.commonService().getServiceCallStatistics(new AsyncCallback<Collection<ServiceCallStatistics>>() {
			@Override
			public void onSuccess(final Collection<ServiceCallStatistics> result) {
				LoadIndicator.get().endLoading();

				VisualizationUtils.loadVisualizationApi(new Runnable() {
					@Override
					public void run() {
						final ColumnChart chart = new ColumnChart(getDataTable(result), getOptions());
						panel.add(chart);
						//chart.draw(getDataTable(result));
//						panel.add(chart);
					}
				}, ColumnChart.PACKAGE);

			}

			@Override
			public void onFailure(Throwable caught) {
				LoadIndicator.get().endLoading();
			}
		});

		initWidget(panel);
	}

	private Options getOptions() {
		Options options = Options.create();
		options.setEnableTooltip(true);
		options.setWidth(400);
		options.setLegendFontSize(10);
		options.setAxisFontSize(10);
		options.setHeight(400);
		options.setTitle("Service hotspots");
		return options;
	}

	private AbstractDataTable getDataTable(final Collection<ServiceCallStatistics> result) {
		final DataTable table = DataTable.create();

		table.addColumn(ColumnType.STRING, "Service");
		// table.addColumn(ColumnType.NUMBER, "Min");
		// table.addColumn(ColumnType.NUMBER, "Max");
		table.addColumn(ColumnType.NUMBER, "#Calls * Average (ms)");
		// table.addColumn(ColumnType.NUMBER, "#Calls");
		table.addRows(result.size());

		int y = 0;
		for (final ServiceCallStatistics stats : result) {
			// table.setProperty(y, 0, "Calls", String.valueOf(stats.getCalls()));
			table.setValue(y, 0, stats.getServiceName());
			// table.setValue(y, 1, stats.getExecutionTimeMin());
			// table.setValue(y, 2, stats.getExecutionTimeMax());
			table.setValue(y, 1, stats.getCalls() * stats.getExecutionTimeAvg());
			//table.setValue(y, 2, stats.getCalls());
			y++;
		}

		return table;
	}
}
