package honeycrm.server;

import honeycrm.server.domain.Account;
import honeycrm.server.domain.Contact;

import java.util.Random;

// TODO add test data for international characters (e.g. japanese/cyrillic/korean/chinese names)
// TODO provide test data for all modules that can be used during testing
// TODO letter sz causes problems during bootstrap, see #5
// TODO this should be done within a plugin. until we find a good one we have to do this within this
// class.
public class DemoDataProvider {
	private final static Random r = new Random();

	public static Account account() {
		Account a = new Account();

		a.name = DemoDataHolder.getRandomName();
		a.annualRevenue = r.nextLong() % 1000000000;
		a.billingAddress = DemoDataHolder.getRandomAddress();
		a.shippingAddress = DemoDataHolder.getRandomAddress();
		
		return a;
	}

	public static Contact contact() {
		Contact c = new Contact();

		c.name = (DemoDataHolder.getRandomName());
		c.email = (DemoDataHolder.getRandomEmail());
		c.city = (DemoDataHolder.getRandomAddress());
		c.phone = (DemoDataHolder.getRandomPhoneNumber());

		return c;
	}

	public static String getRandomString() {
		int length = Math.abs(r.nextInt() % (Integer.MAX_VALUE / 100000));
		String str = "";

		for (int i = 0; i < length; i++) {
			// TODO which range of ascii table contains digits and letters?
			char randomChar = (char) (62 + (r.nextInt() % 58));
			str += randomChar;
		}

		return str;
	}

	/*
	 * public static Contract contract() { Contract c = new Contract();
	 * 
	 * c.setDeadline(DemoDataHolder.getRandomDate()); c.setEndDate(DemoDataHolder.getRandomDate()); c.setStartDate(DemoDataHolder.getRandomDate()); c.setName(DemoDataHolder.getRandomContractName());
	 * 
	 * return c; }
	 * 
	 * public static User user() { User u = new User();
	 * 
	 * u.setName(DemoDataHolder.getRandomName()); u.setFirstname(DemoDataHolder.getRandomName());
	 * 
	 * return u; }
	 * 
	 * public static Service service() { Service s = new Service();
	 * 
	 * s.setName(DemoDataHolder.getRandomServiceName()); s.setCancellableInMonths(r.nextInt(100)); s.setMonthsGuaranteed(r.nextInt(100)); s.setDiscount(r.nextInt(100)); s.setDiscountType(DemoDataHolder.getRandomDiscountType()); s.setPrice(r.nextDouble()); s.setWithVat(r.nextBoolean()); s.setQuantity(r.nextInt());
	 * 
	 * return s; }
	 */
}
