package honeycrm.client.csv;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.CollectionHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CsvExporter extends AbstractCsv {
	public CsvExporter() {
	}

	public String export(final List<Dto> list) {
		if (list.isEmpty()) {
			return "";
		}
		
		final ModuleDto moduleDto = DtoModuleRegistry.instance().get(list.get(0).getModule());
		final String[] fields = moduleDto.getListFieldIds();
		String csv = "";

		for (final Dto entry : list) {
			final Collection<String> line = new LinkedList<String>();

			for (int i = 0; i < fields.length; i++) {
				final Object value = entry.get(fields[i]);
				final String valueAsString = null == value ? "null" : value.toString();
				line.add(FIELD_DEL + valueAsString + FIELD_DEL);
			}

			csv += CollectionHelper.join(line, COL_SEP) + LINE_SEP;
		}

		return csv;
	}
}
