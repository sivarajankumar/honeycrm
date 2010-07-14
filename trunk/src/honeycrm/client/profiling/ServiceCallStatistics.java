package honeycrm.client.profiling;

import java.io.Serializable;

public class ServiceCallStatistics implements Serializable {
	private static final long serialVersionUID = -9095096806226632747L;
	private String serviceName;
	private long calls;
	private long executionTimeAvg;
	private long executionTimeMin;
	private long executionTimeMax;

	public ServiceCallStatistics() {
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public long getCalls() {
		return calls;
	}

	public void setCalls(long calls) {
		this.calls = calls;
	}

	public long getExecutionTimeAvg() {
		return executionTimeAvg;
	}

	public void setExecutionTimeAvg(long executionTimeAvg) {
		this.executionTimeAvg = executionTimeAvg;
	}

	public long getExecutionTimeMin() {
		return executionTimeMin;
	}

	public void setExecutionTimeMin(long executionTimeMin) {
		this.executionTimeMin = executionTimeMin;
	}

	public long getExecutionTimeMax() {
		return executionTimeMax;
	}

	public void setExecutionTimeMax(long executionTimeMax) {
		this.executionTimeMax = executionTimeMax;
	}
}
