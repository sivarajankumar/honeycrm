package honeycrm.client.services;

import honeycrm.client.reports.ReportData;
import honeycrm.client.reports.ReportMetaData;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("report")
public interface ReportService extends RemoteService {
	public Map<Integer, Double> getAnnuallyOfferingVolumes();
	public Map<Integer, Map<String, Integer>> getRevenuePerProduct();
	public Map<Integer, Map<String, Integer>> getExpensesPerCategoryPerYear();
	public ReportData<Map<Integer, Map<String, Integer>>> getReport(int id);
	public ReportMetaData[] getAllReportMetaData();
}
