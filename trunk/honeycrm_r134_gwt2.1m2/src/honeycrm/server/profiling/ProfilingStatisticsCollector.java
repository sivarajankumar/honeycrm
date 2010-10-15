package honeycrm.server.profiling;

import honeycrm.client.profiling.ServiceCallStatistics;
import honeycrm.server.PMF;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;

public class ProfilingStatisticsCollector {
	private static final PersistenceManager m = PMF.get().getPersistenceManager();

	public Collection<ServiceCallStatistics> get() {
		return calcStatistics(getExecutionTimes());
	}

	private List<ServiceCallStatistics> calcStatistics(final Map<String, List<Long>> executionTimes) {
		final List<ServiceCallStatistics> stats = new LinkedList<ServiceCallStatistics>();

		for (final String serviceName : executionTimes.keySet()) {
			final List<Long> execTimes = executionTimes.get(serviceName);

			ServiceCallStatistics stat = new ServiceCallStatistics();

			stat.setCalls(execTimes.size());
			stat.setServiceName(serviceName);
			stat.setExecutionTimeMax(Collections.max(execTimes));
			stat.setExecutionTimeMin(Collections.min(execTimes));
			stat.setExecutionTimeAvg(getAvg(execTimes));
			// TODO calc and set avg

			stats.add(stat);
		}
		return stats;
	}

	private long getAvg(List<Long> execTimes) {
		if (execTimes.isEmpty()) {
			return 0;
		} else {
			long sum = 0;
			
			for (Long time : execTimes) {
				sum += time;
			}

			return sum / execTimes.size();
		}
	}

	private Map<String, List<Long>> getExecutionTimes() {
		final Map<String, List<Long>> executionTimes = new HashMap<String, List<Long>>();

		for (final ServiceCall call : (Collection<ServiceCall>) m.newQuery(ServiceCall.class).execute()) {
			if (!executionTimes.containsKey(call.getServiceName())) {
				executionTimes.put(call.getServiceName(), new LinkedList<Long>());
			}

			executionTimes.get(call.getServiceName()).add(call.getExecutionTime());
		}

		return executionTimes;
	}
}
