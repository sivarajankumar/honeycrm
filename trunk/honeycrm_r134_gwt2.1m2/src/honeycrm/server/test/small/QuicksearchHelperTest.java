package honeycrm.server.test.small;

import honeycrm.client.misc.QuicksearchHelper;
import junit.framework.TestCase;

public class QuicksearchHelperTest extends TestCase {
	public void testGetQS() {
		QuicksearchHelper.getQuickSearchMarkup(null, "productID", null, null);
	}
}
