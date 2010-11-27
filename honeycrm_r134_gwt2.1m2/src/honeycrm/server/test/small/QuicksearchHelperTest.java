package honeycrm.server.test.small;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.Callback;
import honeycrm.client.misc.QuicksearchHelper;
import junit.framework.TestCase;

public class QuicksearchHelperTest extends TestCase {
	public void testGetQS() {
		QuicksearchHelper.getQuickSearchMarkup(null, "productID", new Callback<Dto>() {
			@Override
			public void callback(Dto arg) {
				System.out.println(arg.get("productCode"));
			}
		});
	}
}
