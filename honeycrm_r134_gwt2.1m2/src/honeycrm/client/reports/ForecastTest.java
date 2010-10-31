package honeycrm.client.reports;

import honeycrm.client.misc.NumberParser;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;

public class ForecastTest extends Composite {
	private final TextBox price = new TextBox();
	private LineChart chart = null;
	private static final Map<Integer, Integer> overallRevenue = getOverallRevenue();
	private static final Map<Integer, Double> realExpenses = getRealExpenses();

	public ForecastTest() {
		final VerticalPanel panel = new VerticalPanel();

		price.setText("1");

		price.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(final ChangeEvent event) {
				redrawChart(NumberParser.convertToDouble(price.getText()));
			}
		});

		chart = new LineChart(getAbstractTable(1), getAreaOptions());
		panel.add(chart);
		panel.add(getAdjustmentPanel());

		initWidget(panel);
	}

	protected void redrawChart(final double factor) {
		chart.draw(getAbstractTable(factor), getAreaOptions());
	}

	private Widget getAdjustmentPanel() {
		final FlexTable table = new FlexTable();
		table.setWidget(0, 0, new Label("Service"));
		table.setWidget(0, 1, new TextBox());
		table.setWidget(1, 0, new Label("Price"));
		table.setWidget(1, 1, price);

		return table;
	}

	private Options getAreaOptions() {
		final Options options = Options.create();
		options.setEnableTooltip(true);
		options.setTitleX("Week");
		options.setTitleY("Revenue");
		options.setWidth(800);
		options.setHeight(400);
		options.setSmoothLine(true);
		options.setTitle("Forecast");
		return options;
	}

	private AbstractDataTable getAbstractTable(final double factor) {
		final Map<Integer, Double> result = getSampleData();

		final DataTable data = DataTable.create();

		data.addColumn(ColumnType.NUMBER, "Real revenue created by foo");
		data.addColumn(ColumnType.NUMBER, "Real overall revenue");
		data.addColumn(ColumnType.NUMBER, "Real overall expenses");
		data.addColumn(ColumnType.NUMBER, "Simulated Revenue for foo");
		data.addColumn(ColumnType.NUMBER, "Simulated overall revenue");
		// data.addColumn(ColumnType.NUMBER, "Similated additional revenue");

		data.addRows(result.keySet().size());

		int row = 0;
		for (final Integer x : result.keySet()) {
			int col = 0;
			final double realRevenue = overallRevenue.get(x) - realExpenses.get(x);
			final double realRevenueFromFoo = Math.floor(result.get(x));
			final double simulatedRevenueFromFoo = factor * realRevenueFromFoo;
			final double additionalRevenue = simulatedRevenueFromFoo - realRevenueFromFoo;

			data.setValue(row, col++, realRevenueFromFoo);
			data.setValue(row, col++, realRevenue);
			data.setValue(row, col++, realExpenses.get(x));
			data.setValue(row, col++, simulatedRevenueFromFoo);
			data.setValue(row, col++, realRevenue + additionalRevenue);
			// data.setValue(row, col++, additionalRevenue);

			row++;
		}

		return data;
	}

	private Map<Integer, Double> getSampleData() {
		final Map<Integer, Double> data = new HashMap<Integer, Double>();
		for (int x = 0; x < 52; x++) {
			data.put(x, Math.sqrt(x));
		}
		return data;
	}

	private static Map<Integer, Integer> getOverallRevenue() {
		final Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		for (int x = 0; x < 52; x++) {
			data.put(x, 50 - Random.nextInt() % 10);
		}
		return data;
	}

	private static Map<Integer, Double> getRealExpenses() {
		final Map<Integer, Double> data = new HashMap<Integer, Double>();
		for (int x = 0; x < 52; x++) {
			data.put(x, 100.0 / (x + 1));
		}
		return data;
	}
}
