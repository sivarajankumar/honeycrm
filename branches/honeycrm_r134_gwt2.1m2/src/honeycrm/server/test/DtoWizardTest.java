package honeycrm.server.test;

import honeycrm.client.dto.ModuleDto;
import honeycrm.server.DtoWizard;

import java.util.Map;

import junit.framework.TestCase;

public class DtoWizardTest extends TestCase {
	public void testCreation() {
		final DtoWizard creator = DtoWizard.instance;
		assertNotNull(creator);

		final Map<String, ModuleDto> list = creator.getDtoConfiguration();
		assertFalse(list.isEmpty());

	}
}
