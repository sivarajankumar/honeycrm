package honeycrm.client.reports;

import java.util.Map;

import honeycrm.client.misc.ServiceRegistry;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;

public class ReportSuggester extends Composite {
	public ReportSuggester() {
		final Panel panel = new HorizontalPanel();

		/*
		 * GWT.runAsync(new RunAsyncCallback() {
		 * 
		 * @Override public void onSuccess() {
		 */
		final Panel moreReports = new VerticalPanel();
		moreReports.add(new Label("Related Reports"));
		moreReports.add(new Label("More Reports"));

		ServiceRegistry.reportService().getExpensesPerCategoryPerYear(new AsyncCallback<Map<Integer, Map<String, Integer>>>() {
			@Override
			public void onSuccess(Map<Integer, Map<String, Integer>> result) {
				panel.add(new LineChart(getAbstractTable(result), getAreaOptions()));
				panel.add(moreReports);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not get data from report");
			}
		});
		/*
		 * }
		 * 
		 * @Override public void onFailure(Throwable reason) { Window.alert("Could not executed code asynchronously"); } });
		 */

		initWidget(new ScrollPanel(panel));
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

	private AbstractDataTable getAbstractTable(Map<Integer, Map<String, Integer>> result) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Year");
		data.addColumn(ColumnType.NUMBER, "Expenses for 'A'");

		data.addRows(result.keySet().size());

		int i = 0;
		for (final Integer year : result.keySet()) {
			data.setValue(i, 0, String.valueOf(year));
			data.setValue(i, 1, result.get("A").get(year));
			i++;
		}

		return data;
	}

}
