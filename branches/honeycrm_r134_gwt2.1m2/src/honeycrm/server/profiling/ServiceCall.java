package honeycrm.server.profiling;

import honeycrm.client.profiling.ServiceCallStatistics;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ServiceCall {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	private long executionTime;
	private long startTimestamp;
	private String serviceName;

	public ServiceCall(final String serviceName) {
		if (ServiceCallStatistics.PROFILING_ENABLED) {
			this.serviceName = serviceName;
			this.startTimestamp = System.currentTimeMillis();
		}
	}

	public ServiceCall end() {
		if (ServiceCallStatistics.PROFILING_ENABLED) {
			this.executionTime = System.currentTimeMillis() - startTimestamp;
		}
		return this;
	}

	public Key getId() {
		return id;
	}

	public void setId(final Key id) {
		this.id = id;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(final long executionTime) {
		this.executionTime = executionTime;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(final long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(final String serviceName) {
		this.serviceName = serviceName;
	}
}
