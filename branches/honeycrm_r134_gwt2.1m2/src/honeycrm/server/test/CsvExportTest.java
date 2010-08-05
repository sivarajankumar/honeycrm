package honeycrm.server.test;

import honeycrm.client.csv.AbstractCsv;
import honeycrm.client.csv.CsvExporter;
import honeycrm.client.dto.Dto;
import honeycrm.client.misc.CollectionHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class CsvExportTest extends TestCase {
	public void testExport() {
		final List<Dto> list = new LinkedList<Dto>();
		final Dto foo = new Dto();
		foo.set("name", "Foo");
		final Dto bar = new Dto();
		bar.set("name", "Bar");
		list.add(foo);
		list.add(bar);

		final CsvExporter exporter = new CsvExporter();
		final String csv = exporter.export(list);

		assertNotNull(csv);

		final String[] lines = csv.split(AbstractCsv.LINE_SEP);
		assertEquals(list.size(), lines.length);

		for (final String line : lines) {
			final String[] cols = line.split(AbstractCsv.COL_SEP);
			assertTrue(cols.length > 1);

			final Set<String> colSet = CollectionHelper.toSet(cols);
			assertTrue(colSet.contains(AbstractCsv.FIELD_DEL + "Foo" + AbstractCsv.FIELD_DEL) || colSet.contains(AbstractCsv.FIELD_DEL + "Bar" + AbstractCsv.FIELD_DEL));
		}
	}
}
