package honeycrm.server.test;

import java.util.ArrayList;
import java.util.Collection;

import honeycrm.client.reports.ReportData;
import honeycrm.client.reports.ReportMetaData;
import junit.framework.TestCase;

public class ReportSuggestionTest extends TestCase {
	public void testRelation() {
		final ReportMetaData a = new ReportMetaData(0, "a", new String[] { "a", "b" }, new String[] { "a" });
		final ReportMetaData b = new ReportMetaData(1, "b", new String[] { "c", "d" }, new String[] { "b" });

		assertEquals(0, a.getRelatedReports(new ReportMetaData[] { a, b }).length);

		final ReportMetaData c = new ReportMetaData(1, "b", new String[] { "c", "d" }, new String[] { "a", "b" });

		assertEquals(1, a.getRelatedReports(new ReportMetaData[] { a, c }).length);
	}

	public void testReportData() {
		final Collection<Integer> data = new ArrayList<Integer>();
		data.add(0);
		data.add(1);
		data.add(2);

		final ReportData<Collection<Integer>> r = new ReportData<Collection<Integer>>(data, new ReportMetaData[0]);
	}
}
