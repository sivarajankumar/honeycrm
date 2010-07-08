package honeycrm.client.forecast;

import honeycrm.client.misc.NumberParser;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;

public class ForecastTest extends Composite {
	private final TextBox price = new TextBox();

	public ForecastTest() {
		final VerticalPanel panel = new VerticalPanel();
		panel.add(new Label("Forecast"));

		// only works online.. cannot test without internet (?)
		VisualizationUtils.loadVisualizationApi(new Runnable() {
			@Override
			public void run() {
				panel.add(new LineChart(getAbstractTable(), getAreaOptions()));
				panel.add(getAdjustmentPanel());
			}

		}, LineChart.PACKAGE);

		initWidget(panel);
	}

	private Widget getAdjustmentPanel() {
		final HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Label("Price"));
		panel.add(price);
		return price;
	}

	private Options getAreaOptions() {
		Options options = Options.create();
		options.setEnableTooltip(true);
		options.setTitleX("X");
		options.setTitle("Y");
		options.setWidth(400);
		options.setHeight(240);
		options.setTitle("Forecast");
		return options;
	}

	private AbstractDataTable getAbstractTable() {
		Map<Integer, Double> result = getSampleData();

		DataTable data = DataTable.create();
		data.addColumn(ColumnType.NUMBER, "X");
		data.addColumn(ColumnType.NUMBER, "Y");

		data.addRows(result.keySet().size());

		int i = 0;
		for (final Integer x : result.keySet()) {
			data.setValue(i, 0, x);
			data.setValue(i, 1, result.get(x));
			i++;
		}

		return data;
	}

	private Map<Integer, Double> getSampleData() {
		final Map<Integer, Double> data = new HashMap<Integer, Double>();

		for (int x = 0; x < 100; x++) {
			data.put(x, NumberParser.convertToDouble(x));
		}

		return data;
	}
}
