package honeycrm.client.reports;

import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.forecast.ForecastTest;

import java.util.Date;
import java.util.Map;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;

public class SampleReport extends Composite {
	private LineChart offeringsReport;

	public SampleReport() {
		final Label status = new Label("Status: ");
		final VerticalPanel p = new VerticalPanel();
		p.setStyleName("content");
		p.add(status);
		p.add(new ForecastTest());

		initWidget(p);
		
		new Timer() {
			@Override
			public void run() {
				LoadIndicator.get().startLoading();

				ServiceRegistry.commonService().getAnnuallyOfferingVolumes(new AsyncCallback<Map<Integer, Double>>() {
					@Override
					public void onSuccess(final Map<Integer, Double> result) {
						LoadIndicator.get().endLoading();

						status.setText("Status: Last refreshed at " + new Date(System.currentTimeMillis()));

						if (null == offeringsReport) {
							VisualizationUtils.loadVisualizationApi(new Runnable() {
								@Override
								public void run() {
									p.add(offeringsReport = new LineChart(getAbstractTable(result), getAreaOptions()));
								}
							}, LineChart.PACKAGE);
						} else {
							offeringsReport.draw(getAbstractTable(result), getAreaOptions());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						LoadIndicator.get().endLoading();

					}
				});
			}
		}.schedule(10*1000);
}

	private Options getAreaOptions() {
		Options options = Options.create();
		options.setEnableTooltip(true);
		options.setTitleX("Year");
		options.setTitle("EUR");
		options.setWidth(400);
		options.setHeight(240);
		options.setTitle("Annually Opportunity Volumes");
		return options;
	}

	private AbstractDataTable getAbstractTable(Map<Integer, Double> result) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Year");
		data.addColumn(ColumnType.NUMBER, "Annually Volume");

		data.addRows(result.keySet().size());

		int i = 0;
		for (final Integer year : result.keySet()) {
			data.setValue(i, 0, String.valueOf(year));
			data.setValue(i, 1, result.get(year));
			i++;
		}

		return data;
	}
}
