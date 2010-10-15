package honeycrm.client.reports;

import java.io.Serializable;

public class ReportData<T> implements Serializable {
	private static final long serialVersionUID = 6311901742577358316L;
	private T values;
	private ReportMetaData[] meta;

	public ReportData() {
	}

	public ReportData(final T values, final ReportMetaData[] meta) {
		this.values = values;
		this.meta = meta;
	}

	public boolean isReportNotFound() {
		return null == values;
	}

	public T getValues() {
		return values;
	}
	
	public ReportMetaData[] getMeta() {
		return meta;
	}
}
