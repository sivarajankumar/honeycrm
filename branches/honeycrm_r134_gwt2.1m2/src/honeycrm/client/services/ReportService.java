package honeycrm.client.services;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("report")
public interface ReportService extends RemoteService {
	public Map<Integer, Double> getAnnuallyOfferingVolumes();
	public Map<Integer, Map<String, Integer>> getExpensesPerCategoryPerYear();
}
