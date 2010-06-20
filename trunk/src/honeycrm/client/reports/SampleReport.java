package honeycrm.client.reports;

import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;

import java.util.Map;

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
	public SampleReport() {
		final VerticalPanel p = new VerticalPanel();
		p.add(new Label("Reports"));
		p.setStyleName("content");
		initWidget(p);

		LoadIndicator.get().startLoading();

		ServiceRegistry.commonService().getAnnuallyOfferingVolumes(new AsyncCallback<Map<Integer, Double>>() {
			@Override
			public void onSuccess(final Map<Integer, Double> result) {
				LoadIndicator.get().endLoading();

				VisualizationUtils.loadVisualizationApi(new Runnable() {
					@Override
					public void run() {
						p.add(new LineChart(getAbstractTable(result), getAreaOptions()));
					}
				}, LineChart.PACKAGE);
			}

			@Override
			public void onFailure(Throwable caught) {
				LoadIndicator.get().endLoading();

			}
		});
	}

	private Options getAreaOptions() {
		Options options = Options.create();
		options.setEnableTooltip(true);
		options.setTitleX("Year");
		options.setTitle("EUR");
		options.setWidth(700);
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
		for (final Integer year: result.keySet()) {
			data.setValue(i, 0, String.valueOf(year));
			data.setValue(i, 1, result.get(year));
			i++;
		}

		return data;
	}
}
