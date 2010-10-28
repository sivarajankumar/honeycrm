package honeycrm.server;

import honeycrm.server.domain.Offering;
import honeycrm.server.reports.OfferingReports;

import java.util.Collection;
import java.util.Map;

public class CommonServiceReporter extends AbstractCommonService {
	private static final long serialVersionUID = 6927835291341442947L;

	public CommonServiceReporter() {
	}

	// TODO this has to be rewritten
	public Map<Integer, Double> getAnnuallyOfferingVolumes() {
		return new OfferingReports().getAnnuallyOfferingVolumes((Collection<Offering>) m.newQuery(Offering.class).execute());
	}
}
