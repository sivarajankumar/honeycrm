package honeycrm.client.mvp.views;

import honeycrm.client.mvp.presenters.ReportsSuggestionPresenter.Display;
import honeycrm.client.reports.ReportData;
import honeycrm.client.reports.ReportMetaData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;

public class ReportsSuggestionView extends Composite implements Display {
	private static ReportsSuggestionViewUiBinder uiBinder = GWT.create(ReportsSuggestionViewUiBinder.class);

	interface ReportsSuggestionViewUiBinder extends UiBinder<Widget, ReportsSuggestionView> {
	}

	public ReportsSuggestionView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		close.setText("Close");
		relatedReportsLbl.setText("Related Reports");
		moreReportsLbl.setText("More Reports");
	}

	@UiField Label relatedReportsLbl;
	@UiField Label moreReportsLbl;
	@UiField FlowPanel chartPanel;
	@UiField VerticalPanel relatedReportsPanel;
	@UiField Button close;
	@UiField DecoratedPopupPanel panel;

	@Override
	public void setChart(ReportData<Map<Integer, Map<String, Integer>>> reportData, ReportMetaData reportMeta) {
		chartPanel.clear();
		chartPanel.add(new LineChart(getAbstractTable(reportData, reportMeta), getAreaOptions(reportMeta)));
	}

	@Override
	public void setRelatedReports(ReportMetaData[] relatedReports) {
		relatedReportsPanel.clear();

		if (0 == relatedReports.length) {
			relatedReportsPanel.add(new Label("(none)"));
		} else {
			for (int i = 0; i < relatedReports.length; i++) {
				relatedReportsPanel.add(new Hyperlink(relatedReports[i].getTitle(), "report " + relatedReports[i].getId()));
			}
		}
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
	
	@UiHandler("close")
	void onClose(ClickEvent event) {
//		panel.(); // <-- no effect..
		panel.getParent().removeFromParent();
	}
}
