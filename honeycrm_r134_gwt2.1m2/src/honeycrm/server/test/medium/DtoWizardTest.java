package honeycrm.server.test.medium;

import honeycrm.client.dto.ModuleDto;
import honeycrm.server.DomainClassRegistry;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.Contract;
import honeycrm.server.domain.Offering;
import honeycrm.server.domain.RecurringService;
import honeycrm.server.domain.UniqueService;
import honeycrm.server.transfer.DtoWizard;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

public class DtoWizardTest extends TestCase {
	public void testCreation() throws ClassNotFoundException {
		assertNotNull(DtoWizard.instance);
	}

	public void testFieldnameValidity() {
		final Set<String> nonExistingFields = new HashSet<String>();

		Map<String, ModuleDto> list = DtoWizard.instance.getDtoConfiguration();
		assertFalse(list.isEmpty());

		for (final Class<? extends AbstractEntity> domainClass : DomainClassRegistry.instance.getDomainClasses()) {
			final String moduleName = domainClass.getSimpleName().toLowerCase();
			final ModuleDto module = list.get(moduleName);

			for (int y = 0; y < module.getFormFieldIds().length; y++) {
				for (int x = 0; x < module.getFormFieldIds()[y].length; x++) {
					checkFieldExistance(nonExistingFields, domainClass, module.getFormFieldIds()[y][x]);
				}
			}

			for (int x = 0; x < module.getListFieldIds().length; x++) {
				checkFieldExistance(nonExistingFields, domainClass, module.getListFieldIds()[x]);
			}
		}

		if (!nonExistingFields.isEmpty()) {
			for (final String fieldName : nonExistingFields) {
				System.out.println("Field does not exist: " + fieldName);
			}
			fail();
		}
	}

	public void testRelateFieldHashCodeAssumptions() {
		try {
			for (int i = 0; i < 10; i++) {
				assertTrue(Contract.class.getField("uniqueServices").hashCode() != Offering.class.getField("uniqueServices").hashCode());
				
				assertNotSame(UniqueService.class.getField("id"), UniqueService.class.getField("id"));
				assertTrue(UniqueService.class.getField("id").hashCode() == RecurringService.class.getField("id").hashCode());

				Set<Field> set = new HashSet<Field>();

				set.add(UniqueService.class.getField("id"));
				set.add(UniqueService.class.getField("id"));
				assertEquals(1, set.size());
				
				set.add(RecurringService.class.getField("id"));
				assertEquals(1, set.size());

				// ok, great. we cannot distinguish between the id field in both classes since it is declared in the upper class and thus appears to be the same field.
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	private void checkFieldExistance(final Set<String> nonExistingFields, final Class<? extends AbstractEntity> domainClass, final String fieldName) {
		try {
			domainClass.getField(fieldName);
		} catch (NoSuchFieldException e) {
			nonExistingFields.add(domainClass.toString() + "." + fieldName);
		}
	}
}
