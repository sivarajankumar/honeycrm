package honeycrm.client.reports;

import honeycrm.client.basiclayout.LoadIndicator;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.profiling.ServiceCallStatistics;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.google.gwt.visualization.client.visualizations.ColumnChart.Options;

public class ProfilingReport extends Composite {
	public ProfilingReport() {
		final VerticalPanel vpanel = new VerticalPanel();
		final HorizontalPanel panel = new HorizontalPanel();

		vpanel.add(new Label("Profiling is enabled? " + ServiceCallStatistics.PROFILING_ENABLED));
		vpanel.add(panel);

		LoadIndicator.get().startLoading();

		ServiceRegistry.commonService().getServiceCallStatistics(new AsyncCallback<Collection<ServiceCallStatistics>>() {
			@Override
			public void onSuccess(final Collection<ServiceCallStatistics> result) {
				LoadIndicator.get().endLoading();

				final ColumnChart chartCalls = new ColumnChart(getDataTableCalls(result), getOptions("Calls"));
				final ColumnChart chartAvg = new ColumnChart(getDataTableAvg(result), getOptions("Avg"));
				panel.add(chartCalls);
				panel.add(chartAvg);
			}

			@Override
			public void onFailure(final Throwable caught) {
				LoadIndicator.get().endLoading();
			}
		});

		initWidget(vpanel);
	}

	private Options getOptions(final String title) {
		final Options options = Options.create();
		options.setEnableTooltip(true);
		options.setWidth(200);
		options.setLegendFontSize(10);
		options.setAxisFontSize(10);
		options.setHeight(200);
		options.setTitle(title);
		return options;
	}

	private AbstractDataTable getDataTableCalls(final Collection<ServiceCallStatistics> result) {
		final DataTable table = DataTable.create();

		table.addColumn(ColumnType.STRING, "Service");
		table.addColumn(ColumnType.NUMBER, "#Calls");
		table.addRows(result.size());

		int y = 0;
		for (final ServiceCallStatistics stats : result) {
			table.setValue(y, 0, stats.getServiceName());
			table.setValue(y, 1, stats.getCalls());
			y++;
		}

		return table;
	}

	private AbstractDataTable getDataTableAvg(final Collection<ServiceCallStatistics> result) {
		final DataTable table = DataTable.create();

		table.addColumn(ColumnType.STRING, "Service");
		table.addColumn(ColumnType.NUMBER, "Avg");
		table.addRows(result.size());

		int y = 0;
		for (final ServiceCallStatistics stats : result) {
			table.setValue(y, 0, stats.getServiceName());
			table.setValue(y, 1, stats.getExecutionTimeAvg());
			y++;
		}

		return table;
	}
}
