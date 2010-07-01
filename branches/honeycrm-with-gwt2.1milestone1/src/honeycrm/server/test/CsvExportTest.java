package honeycrm.server.test;

import honeycrm.client.csv.AbstractCsv;
import honeycrm.client.csv.CsvExporter;
import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.DtoContact;
import honeycrm.client.misc.CollectionHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class CsvExportTest extends TestCase {
	public void testExport() {
		final List<AbstractDto> list = new LinkedList<AbstractDto>();
		list.add(new DtoContact("Foo"));
		list.add(new DtoContact("Bar"));

		final CsvExporter exporter = new CsvExporter(DtoContact.class);
		final String csv = exporter.export(list);

		assertNotNull(csv);

		final String[] lines = csv.split(AbstractCsv.LINE_SEP);
		assertEquals(list.size(), lines.length);

		for (int i = 0; i < lines.length; i++) {
			final String[] cols = lines[i].split(AbstractCsv.COL_SEP);
			assertTrue(cols.length > 1);

			final Set<String> colSet = CollectionHelper.toSet(cols);
			assertTrue(colSet.contains(AbstractCsv.FIELD_DEL + "Foo" + AbstractCsv.FIELD_DEL) || colSet.contains(AbstractCsv.FIELD_DEL + "Bar" + AbstractCsv.FIELD_DEL));
		}
	}
}
