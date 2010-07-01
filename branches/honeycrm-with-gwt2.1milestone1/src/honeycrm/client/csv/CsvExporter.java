package honeycrm.client.csv;

import honeycrm.client.DtoRegistry;
import honeycrm.client.dto.AbstractDto;
import honeycrm.client.misc.CollectionHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CsvExporter extends AbstractCsv {
	private final Class<? extends AbstractDto> clazz;
	private final AbstractDto dto;

	public CsvExporter(final Class<? extends AbstractDto> clazz) {
		this.clazz = clazz;
		this.dto = DtoRegistry.instance.getDto(clazz);
	}

	public String export(final List<AbstractDto> list) {
		String csv = "";

		for (AbstractDto entry : list) {
			final Collection<String> line = new LinkedList<String>();

			for (int i = 0; i < entry.getListViewColumnIds().length; i++) {
				final Object value = entry.getFieldValue(entry.getListViewColumnIds()[i]);
				final String valueAsString = null == value ? "null" : value.toString();
				line.add(FIELD_DEL + valueAsString + FIELD_DEL);
			}

			csv += CollectionHelper.join(line, COL_SEP) + LINE_SEP;
		}

		return csv;
	}
}
