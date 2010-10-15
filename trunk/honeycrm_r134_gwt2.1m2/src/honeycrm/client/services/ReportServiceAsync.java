package honeycrm.client.services;

import honeycrm.client.reports.ReportData;
import honeycrm.client.reports.ReportMetaData;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportServiceAsync {
	void getAnnuallyOfferingVolumes(AsyncCallback<Map<Integer, Double>> callback);
	void getExpensesPerCategoryPerYear(AsyncCallback<Map<Integer, Map<String, Integer>>> callback);
	void getRevenuePerProduct(AsyncCallback<Map<Integer, Map<String, Integer>>> callback);
	void getReport(int id, AsyncCallback<ReportData<Map<Integer, Map<String, Integer>>>> callback);
	void getAllReportMetaData(AsyncCallback<ReportMetaData[]> callback);
}
