package honeycrm.server.services;

import honeycrm.client.reports.ReportData;
import honeycrm.client.reports.ReportMetaData;
import honeycrm.client.services.ReportService;
import honeycrm.server.CommonServiceReporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReportServiceImpl extends RemoteServiceServlet implements ReportService {
	private static final long serialVersionUID = 9188964651846980485L;
	private static final CommonServiceReporter reporter = new CommonServiceReporter();
	private static final Random random = new Random(System.currentTimeMillis());
	private ReportMetaData[] metaData;

	@Override
	public Map<Integer, Double> getAnnuallyOfferingVolumes() {
		return reporter.getAnnuallyOfferingVolumes();
	}

	@Override
	public Map<Integer, Map<String, Integer>> getExpensesPerCategoryPerYear() {
		final Map<Integer, Map<String, Integer>> r = new HashMap<Integer, Map<String, Integer>>();

		for (int i = 0; i < 10; i++) {
			final int year = 2000 + i;
			final Map<String, Integer> revenueForOneYear = new HashMap<String, Integer>();

			for (final String category : new String[] { "A", "B", "C" }) {
				revenueForOneYear.put(category, i * i + random.nextInt(50));
			}

			r.put(year, revenueForOneYear);
		}

		return r;
	}

	@Override
	public Map<Integer, Map<String, Integer>> getRevenuePerProduct() {
		final Map<Integer, Map<String, Integer>> revenue = new HashMap<Integer, Map<String, Integer>>();

		for (int i = 0; i < 10; i++) {
			final int year = 2000 + i;
			final Map<String, Integer> revenueForOneYear = new HashMap<String, Integer>();

			for (final String category : new String[] { "Product1", "Product2", "Product3", "Product4", "Product5" }) {
				revenueForOneYear.put(category, i * random.nextInt(100));
			}

			revenue.put(year, revenueForOneYear);
		}

		return revenue;
	}

	@Override
	public ReportData<Map<Integer, Map<String, Integer>>> getReport(final int reportId) {
		switch (reportId) {
		case 0:
			return new ReportData<Map<Integer, Map<String, Integer>>>(getExpensesPerCategoryPerYear(), getAllReportMetaData());
		case 1:
			return new ReportData<Map<Integer, Map<String, Integer>>>(getRevenuePerProduct(), getAllReportMetaData());
		case 2:
			return new ReportData<Map<Integer, Map<String, Integer>>>(getDummyReport(0), getAllReportMetaData());
		default:
			return new ReportData<Map<Integer, Map<String, Integer>>>();
		}
	}

	public Map<Integer, Map<String, Integer>> getDummyReport(final int reportId) {
		final Map<Integer, Map<String, Integer>> data = new HashMap<Integer, Map<String, Integer>>();

		for (double x = 0; x < 1; x += 0.1) {
			final HashMap<String, Integer> here = new HashMap<String, Integer>();

			here.put("a", (int) (100*Math.sin(x)));
			here.put("b", (int) (100*Math.cos(x)));
			here.put("c", (int) (100*Math.tan(x)));

			data.put((int) (x * 10), here);
		}
		return data;
	}

	@Override
	public ReportMetaData[] getAllReportMetaData() {
		if (null == metaData) {
			metaData = internalGetAllReportMetaData();
		}
		return metaData;
	}

	private ReportMetaData[] internalGetAllReportMetaData() {
		final ArrayList<ReportMetaData> metaData = new ArrayList<ReportMetaData>();

		metaData.add(new ReportMetaData(0, "Expenses Per Category Per Year", new String[] { "Expenses for A", "Expenses for B", "Expenses for C" }, new String[] { "expenses", "category", "revenue" }));
		metaData.add(new ReportMetaData(1, "Revenue Per Product Per Year", new String[] { "Product1", "Product2", "Product3", "Product4", "Product5" }, new String[] { "revenue", "product" }));
		metaData.add(new ReportMetaData(2, "Dummy Report", new String[] { "Product1", "Product2", "Product3" }, new String[] { "product", "math" }));

		return metaData.toArray(new ReportMetaData[0]);
	}
}
