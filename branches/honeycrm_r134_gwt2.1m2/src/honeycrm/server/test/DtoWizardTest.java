package honeycrm.server.test;

import honeycrm.client.dto.ModuleDto;
import honeycrm.server.DomainClassRegistry;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.transfer.DtoWizard;

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

			for (int x=0; x<module.getListFieldIds().length; x++) {
				checkFieldExistance(nonExistingFields, domainClass, module.getListFieldIds()[x]);
			}
		}
		
		if (!nonExistingFields.isEmpty()) {
			for (final String fieldName: nonExistingFields) {
				System.out.println("Field does not exist: " + fieldName);
			}
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
