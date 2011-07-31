package honeycrm.server.test.small;

import honeycrm.client.dto.Configuration;
import honeycrm.client.dto.ModuleDto;
import honeycrm.server.NewDtoWizard;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class NewDtoWizardTest extends TestCase {
	public void testWizard() {
		final Configuration c = NewDtoWizard.getConfiguration();

		final String kind = "Contact";

		final ModuleDto contact = c.getModuleDtos().get(kind);
		assertEquals(kind, contact.getModule());
		assertNotNull(contact.getExtraButtons());
		assertNotNull(contact.getFields());
		assertNotNull(contact.getFieldById("name"));
		assertNotNull(contact.getFieldById("accountId"));
	}

	public void testRelateFieldMappings() {
		final HashMap<String, ModuleDto> moduleDtos = NewDtoWizard.getConfiguration().getModuleDtos();
		
		for (final Map.Entry<String, ModuleDto> entry: moduleDtos.entrySet()) {
			final ModuleDto moduleDto = entry.getValue();

			assertEquals(entry.getKey(), moduleDto.getModule());

			for (final Map.Entry<String, String> relate: entry.getValue().getRelateFieldMappings().entrySet()) {
				final boolean relatedEntityExists = moduleDtos.containsKey(relate.getValue());
				assertTrue(relatedEntityExists);
			}
		}
	}
}
