package honeycrm.client.services;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportServiceAsync {
	void getAnnuallyOfferingVolumes(AsyncCallback<Map<Integer, Double>> callback);
	void getExpensesPerCategoryPerYear(AsyncCallback<Map<Integer, Map<String, Integer>>> callback);
}
