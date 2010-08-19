package honeycrm.server.test;

import honeycrm.server.CachingReflectionHelper;
import honeycrm.server.domain.Contact;
import honeycrm.server.transfer.ReflectionHelper;

import java.lang.reflect.Field;

import junit.framework.TestCase;

public class ReflectionHelperTest extends TestCase {
	private CachingReflectionHelper r;
	
	@Override
	protected void setUp() throws Exception {
		r = new CachingReflectionHelper();
	}
	
	public void testGetFieldsByType() {
		for (final Field field: ReflectionHelper.getFieldsByType(r.getAllFields(Contact.class), String.class)) {
			assertEquals(String.class, field.getType());
		}
	}
}
