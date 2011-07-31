package honeycrm.server.reports;

import honeycrm.client.dto.Dto;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OfferingReports {
	public Map<Integer, Double> getAnnuallyOfferingVolumes(final Dto[] offerings) {
		final Map<Integer, Double> volumes = new HashMap<Integer, Double>();

		for (final Dto offering : offerings) {
			if (null == offering.get("deadline")) {
				continue; // skip this offering since we cannot analyze it anyway
			}
			
			final Calendar c = Calendar.getInstance();
			c.setTime((Date) offering.get("deadline"));

			final int year = c.get(Calendar.YEAR);

			if (volumes.containsKey(year)) {
				volumes.put(year, 23.0 + volumes.get(year));
			} else {
				volumes.put(year, 23.0);
			}
		}

		return volumes;
	}
}
