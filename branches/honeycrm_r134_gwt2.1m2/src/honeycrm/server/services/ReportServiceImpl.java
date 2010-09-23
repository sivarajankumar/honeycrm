package honeycrm.server.services;

import honeycrm.client.reports.ReportData;
import honeycrm.client.reports.ReportMetaData;
import honeycrm.client.services.ReportService;
import honeycrm.server.CommonServiceImpl;
import honeycrm.server.CommonServiceReporter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReportServiceImpl extends RemoteServiceServlet implements ReportService {
	private static final long serialVersionUID = 9188964651846980485L;
	private static final CommonServiceReporter reporter = new CommonServiceReporter();
	private static final CommonServiceImpl commonService = new CommonServiceImpl();
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
		default:
			return new ReportData<Map<Integer, Map<String, Integer>>>();
		}
	}

	@Override
	public ReportMetaData[] getAllReportMetaData() {
		if (null == metaData) {
			metaData = internalGetAllReportMetaData();
		}
		return metaData;
	}
	
	private ReportMetaData[] internalGetAllReportMetaData() {
		final int reportCount = 2;

		final ReportMetaData metaData[] = new ReportMetaData[reportCount];
		int i = 0;
		metaData[i++] = new ReportMetaData(0, "Expenses Per Category Per Year", new String[] { "Expenses for A", "Expenses for B", "Expenses for C" }, new String[] { "expenses", "category", "revenue" });
		metaData[i++] = new ReportMetaData(1, "Revenue Per Product Per Year", new String[] { "Product1", "Product2", "Product3", "Product4", "Product5" }, new String[] { "revenue", "product" });

		return metaData;
	}
}
