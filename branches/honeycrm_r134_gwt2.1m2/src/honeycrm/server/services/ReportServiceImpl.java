package honeycrm.server.services;

import honeycrm.client.services.ReportService;
import honeycrm.server.CommonServiceReporter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReportServiceImpl extends RemoteServiceServlet implements ReportService {
	private static final long serialVersionUID = 9188964651846980485L;
	private static final CommonServiceReporter reporter = new CommonServiceReporter();
	private static final Random random = new Random(System.currentTimeMillis());

	@Override
	public Map<Integer, Double> getAnnuallyOfferingVolumes() {
		return reporter.getAnnuallyOfferingVolumes();
	}

	@Override
	public Map<Integer, Map<String, Integer>> getExpensesPerCategoryPerYear() {
		final Map<Integer, Map<String, Integer>> r = new HashMap<Integer, Map<String, Integer>>();

		for (int year = 2000; year < 2010; year++) {
			final Map<String, Integer> rYear = new HashMap<String, Integer>();

			for (final String category : new String[] { "A", "B", "C" }) {
				rYear.put(category, year + random.nextInt(100));
			}

			r.put(year, rYear);
		}

		return r;
	}

	public Map<Integer, Map<String, Integer>> getRevenuePerProduct() {
		final Map<Integer, Map<String, Integer>> revenue = new HashMap<Integer, Map<String,Integer>>();
		
		for (int i=0; i<10; i++) {
			final int year = 2000 + i;
			final Map<String, Integer> revenueForOneYear = new HashMap<String, Integer>();

			for (final String category : new String[] { "Product1", "Product2", "Product3", "Product4", "Product5" }) {
				revenueForOneYear.put(category, i * random.nextInt(100));
			}

			revenue.put(year, revenueForOneYear);
		}
		
		return revenue;
	}
}
