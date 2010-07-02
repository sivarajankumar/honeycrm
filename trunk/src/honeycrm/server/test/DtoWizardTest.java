package honeycrm.server.test;

import honeycrm.client.dto.Dto;
import honeycrm.server.DtoWizard;

import java.util.List;

import junit.framework.TestCase;

public class DtoWizardTest extends TestCase {
	public void testCreation() {
		final DtoWizard creator = DtoWizard.instance;
		assertNotNull(creator);
		
		List<Dto> list = creator.getDtoConfiguration();
		assertFalse(list.isEmpty());
		
		
	}
}
