package crm.server;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DemoDataHolder {
	private final static Random r = new Random();
	private final static List<String> names = new LinkedList<String>();
	private final static List<String> phoneNumber = new LinkedList<String>();
	private final static List<String> address = new LinkedList<String>();
	private final static List<String> emails = new LinkedList<String>();
	private final static List<Date> dates = new LinkedList<Date>();
	private final static List<Integer> ages = new LinkedList<Integer>();
	private final static List<String> hosts = new LinkedList<String>();
	private final static List<String> contractNames = new LinkedList<String>();
	private final static List<String> serviceNames = new LinkedList<String>();
	private final static List<String> serviceDiscountTypes = new LinkedList<String>();

	static {
		serviceDiscountTypes.add("ABS");
		serviceDiscountTypes.add("REL");

		serviceNames.add("Internet");
		serviceNames.add("PC/Laptop");
		serviceNames.add("Mouse");
		serviceNames.add("Maintenance");
		serviceNames.add("Power Supply / USV");
		serviceNames.add("Traing / Teaching");
		serviceNames.add("Consulting");

		contractNames.add("Mission Impossible");
		contractNames.add("Implement IP Telephony");
		contractNames.add("Refactor CRM implementation");
		contractNames.add("Do Auditing for T-Systems");

		hosts.add("yahoo.com");
		hosts.add("googlemail.com");
		hosts.add("t-online.com");
		hosts.add("aol.com");
		hosts.add("hotmail.com");
		hosts.add("web.de");
		hosts.add("freenet.de");

		address.add("La Place Ring");
		address.add("Neue Strasse");
		address.add("Prof. Helmert Strasse");
		address.add("Bisamkiez");
		address.add("An der Alten Zauche");
		address.add("Brandenburger Strasse");
		address.add("Bahnhofstrasse");
		address.add("Strasse der Jugend");

		names.add("Peter");
		names.add("Ingo");
		names.add("Mike");
		names.add("Lion");
		names.add("Peter");
		names.add("Ralf");
		names.add("Erich");
		names.add("Monika");
		names.add("Petra");
		names.add("Sarah");

		// TODO these constraints should be read directly from the domain classes
		final int minPhoneNumber = 10000;
		final int maxPhoneNumber = 9999;

		final long minDate = getMinDateTimestamp();
		final long maxDateTimespan = getMaxDateTimespan(minDate);
		final int minAge = 16;
		final int maxAge = 120;
		final int numberOfRandomValues = 10;

		for (int i = 0; i < numberOfRandomValues; i++) {
			phoneNumber.add(String.valueOf(minPhoneNumber + r.nextInt(maxPhoneNumber)));
			ages.add(minAge + r.nextInt(maxAge - minAge));
			emails.add(getRandomEmailAddress());

			long n = r.nextLong();
			if (n < 0)
				n = -n;

			dates.add(new Date(minDate + n % maxDateTimespan));
		}

	}

	private static long getMinDateTimestamp() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 1990);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		final Date firstDate = c.getTime();
		return firstDate.getTime();
	}

	private static long getMaxDateTimespan(final long firstDateTimestamp) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2020);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH, 1);
		Date lastDate = c.getTime();
		return lastDate.getTime() - firstDateTimestamp;
	}

	private static String getRandomEmailAddress() {
		String name = getRandomName();
		String host = hosts.get(r.nextInt(hosts.size()));
		return (name + "@" + host).toLowerCase();
	}

	public static Date getRandomDate() {
		return dates.get(r.nextInt(dates.size()));
	}

	public static String getRandomEmail() {
		return emails.get(r.nextInt(emails.size()));
	}

	public static Integer getRandomAge() {
		return ages.get(r.nextInt(ages.size()));
	}

	public static String getRandomAddress() {
		return address.get(r.nextInt(address.size()));
	}

	public static String getRandomPhoneNumber() {
		return phoneNumber.get(r.nextInt(phoneNumber.size()));
	}

	public static String getRandomName() {
		return names.get(r.nextInt(names.size()));
	}

	public static String getRandomContractName() {
		return contractNames.get(r.nextInt(contractNames.size()));
	}

	public static String getRandomServiceName() {
		return serviceNames.get(r.nextInt(serviceNames.size()));
	}

	public static String getRandomDiscountType() {
		return serviceDiscountTypes.get(r.nextInt(serviceDiscountTypes.size()));
	}
}
