package honeycrm.client.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import honeycrm.client.basiclayout.Initializer;
import honeycrm.client.misc.NumberParser;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.view.ModuleAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;

public class ReportSuggester extends Composite implements ValueChangeHandler<String> {
	final Panel panel = new HorizontalPanel();

	public ReportSuggester() {
		initWidget(new ScrollPanel(panel));
		History.addValueChangeHandler(this);

		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				if (!Initializer.SKIP_LOADING_VISUALISATIONS) {
					showReport(0);
				}
			}

			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not run code asynchronously");
			}
		});
	}

	protected void showReport(final int startingReport) {
		ServiceRegistry.reportService().getReport(startingReport, new AsyncCallback<ReportData<Map<Integer, Map<String, Integer>>>>() {
			@Override
			public void onSuccess(ReportData<Map<Integer, Map<String, Integer>>> reportData) {
				final ReportMetaData[] allMeta = reportData.getMeta();
				final ReportMetaData reportMeta = ReportMetaData.getReportById(startingReport, allMeta);

				panel.clear();
				panel.add(new LineChart(getAbstractTable(reportData, reportMeta), getAreaOptions(reportMeta)));
				panel.add(getRelatedReports(reportMeta.getRelatedReports(allMeta)));
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Cannot get report data");
			}
		});
	}

	private Options getAreaOptions(final ReportMetaData report) {
		Options options = Options.create();
		options.setEnableTooltip(true);
		options.setTitleX("Year"); // TODO generalize
		options.setTitle("EUR"); // TODO generalize
		options.setWidth(600);
		options.setHeight(240);
		options.setMin(0.0);
		options.setTitle(report.getTitle());
		return options;
	}

	private AbstractDataTable getAbstractTable(final ReportData<Map<Integer, Map<String, Integer>>> report, final ReportMetaData reportMeta) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Year"); // TODO generalize

		for (final String column : reportMeta.getColumns()) {
			data.addColumn(ColumnType.NUMBER, column);
		}

		data.addRows(report.getValues().size());

		int i = 0;
		for (final Map.Entry<Integer, Map<String, Integer>> entry : report.getValues().entrySet()) {
			data.setValue(i, 0, String.valueOf(entry.getKey()));

			final Map<String, Integer> annualValues = entry.getValue();
			final ArrayList<String> sortedKeys = new ArrayList<String>(annualValues.keySet());
			Collections.sort(sortedKeys);

			int column = 1;
			for (final String key : sortedKeys) {
				data.setValue(i, column++, entry.getValue().get(key));
			}

			i++;
		}

		return data;
	}

	protected Widget getRelatedReports(final ReportMetaData[] relatedReports) {
		final VerticalPanel panel = new VerticalPanel();
		panel.add(new Label("Related Reports"));

		if (relatedReports.length == 0) {
			panel.add(new Label("(none found)"));
		} else {
			for (int i = 0; i < relatedReports.length; i++) {
				panel.add(new Hyperlink(relatedReports[i].getTitle(), "report " + relatedReports[i].getId()));
			}
		}

		panel.add(new Label("More Reports"));

		return panel;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String[] token = event.getValue().trim().split("\\s+");

		if (2 <= token.length) {
			final ModuleAction action = ModuleAction.fromString(token[0]);
			
			if (null == action) 
				return;
			
			final int reportId = NumberParser.convertToInteger(token[1]);

			switch (action) {
			case REPORT:
				if (0 <= reportId && reportId <= 1) {
					showReport(reportId);
				} else {
					Window.alert("invalid report id");
				}
			}
		}
	}
}
