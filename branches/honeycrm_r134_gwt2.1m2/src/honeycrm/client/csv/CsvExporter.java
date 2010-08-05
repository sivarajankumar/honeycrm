package honeycrm.client.csv;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.CollectionHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CsvExporter extends AbstractCsv {
	public CsvExporter() {
	}

	public String export(final List<Dto> list) {
		String csv = "";

		for (final Dto entry : list) {
			final Collection<String> line = new LinkedList<String>();

			for (int i = 0; i < entry.getListFieldIds().length; i++) {
				final Object value = entry.get(entry.getListFieldIds()[i]);
				final String valueAsString = null == value ? "null" : value.toString();
				line.add(FIELD_DEL + valueAsString + FIELD_DEL);
			}

			csv += CollectionHelper.join(line, COL_SEP) + LINE_SEP;
		}

		return csv;
	}
}
