package honeycrm.server.test.medium;

import honeycrm.server.domain.Offering;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

/**
 * Generate a random collection of offerings with services, calculate the sum and compare this with the sum calculated by the report class.
 */
public class OfferingReportsTest extends TestCase {
	private static final Random random = new Random();

	public void testOfferingReport() {
		/*final Dto[] offerings = new LinkedList<Offering>();
		final Double expectedSum = getOfferings(offerings);

		final Map<Integer, Double> report = new OfferingReports().getAnnuallyOfferingVolumes(offerings);

		Double actualSum = 0.0;
		for (final Integer year : report.keySet()) {
			actualSum += report.get(year);
		}

		assertTrue(Math.abs(expectedSum - actualSum) < 0.1);
		System.out.println("Sum is " + actualSum);*/
	}

	private Double getOfferings(final List<Offering> offerings) {
		Double sum = 0.0;

		final int offeringsCount = 2 + (random.nextInt() % 100);
		for (int i = 0; i < offeringsCount; i++) {
			final Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, random.nextInt() % 3 + 100);

			Offering o = new Offering();
			o.deadline = (c.getTime());
			// TODO will see how to do that
			//o.services_objects = (new LinkedList<UniqueService>());
			//sum += getServices(o.services_objects);

			offerings.add(o);
		}

		return sum;
	}
}
