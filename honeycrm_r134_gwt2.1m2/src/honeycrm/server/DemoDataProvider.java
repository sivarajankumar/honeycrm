package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.server.domain.Account;
import honeycrm.server.domain.Contact;
import honeycrm.server.domain.Offering;
import honeycrm.server.domain.Product;
import honeycrm.server.domain.UniqueService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO add test data for international characters (e.g. japanese/cyrillic/korean/chinese names)
// TODO provide test data for all modules that can be used during testing
// TODO letter sz causes problems during bootstrap, see #5
// TODO this should be done within a plugin. until we find a good one we have to do this within this
// class.
public class DemoDataProvider {
	private final static Random r = new Random();

	public static Dto account() {
		final Dto a = new Dto(Account.class.getSimpleName());

		a.set("name", DemoDataHolder.getRandomName());
		a.set("annualRevenue", r.nextLong() % 1000000000);
		a.set("billingAddress", DemoDataHolder.getRandomAddress());
		a.set("shippingAddress", DemoDataHolder.getRandomAddress());

		return a;
	}

	public static Dto contact() {
		final Dto c = new Dto(Contact.class.getSimpleName());

		c.set("name", (DemoDataHolder.getRandomName()));
		c.set("email", (DemoDataHolder.getRandomEmail()));
		c.set("city", (DemoDataHolder.getRandomAddress()));
		c.set("phone", (DemoDataHolder.getRandomPhoneNumber()));

		return c;
	}

	public static String getRandomString() {
		final int length = r.nextInt(1000);
		String str = "";

		for (int i = 0; i < length; i++) {
			final char randomChar = (char) (48 + r.nextInt(58));
			str += randomChar;
		}

		return str;
	}

	public static List<Dto> getProducts(final int count) {
		final List<Dto> products = new ArrayList<Dto>(count);
		final Dto product = new Dto();
		product.setModule(Product.class.getSimpleName());

		for (int i = 0; i < count; i++) {
			product.set("name", DemoDataHolder.getRandomProductName());
			product.set("price", r.nextDouble());
			products.add(product);
		}

		return products;
	}

	public static Double getServices(final List<UniqueService> services) {
		Double sum = 0.0;

		final int servicesCount = 2 + r.nextInt() % 100;

		for (int i = 0; i < servicesCount; i++) {
			final UniqueService s = new UniqueService();
			s.quantity = r.nextInt(100);
			s.price = Math.abs(r.nextDouble() % 10000.0);
			s.discount = r.nextInt(10);
			services.add(s);

			sum += (s.price - s.discount) * s.quantity;
		}

		return sum;
	}

	public static Dto getService(final int name) {
		final Dto service = new Dto();
		service.setModule(UniqueService.class.getSimpleName());
		service.set("name", "service " + name);
		return service;
	}
	
	

	public static Dto getOffering(final int name, final int childCount) {
		final Dto offering = new Dto();
		offering.setModule(Offering.class.getSimpleName());
		offering.set("name", "offering " + name);
		offering.set("uniqueServices", getServices(childCount));
		return offering;
	}

	public static ArrayList<Dto> getServices(final int childCount) {
		final ArrayList<Dto> services = new ArrayList<Dto>(childCount);
		for (int i = 0; i < childCount; i++) {
			services.add(getService(r.nextInt()));
		}
		return services;
	}
}
