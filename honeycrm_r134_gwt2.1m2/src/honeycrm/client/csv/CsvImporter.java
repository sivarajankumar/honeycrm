package honeycrm.client.csv;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Window;

abstract public class CsvImporter extends AbstractCsv {
	private static final String GLUE = ",";

	public static CsvImporter get(final String module) {
		if ("Contact".equals(module)) {
			return new CsvImporterContacts();
		} else if ("Account".equals(module)) {
			return new CsvImporterAccounts();
		}

		Window.alert("No CSV importer available for '" + module + "'");
		throw new RuntimeException("No CSV importer available for '" + module + "'");
	}

	public Dto[] parse(final String csvString) {
		if (null != csvString && !csvString.isEmpty()) {
			final String[][] table = getData(csvString);

			if (table.length > 0) {
				return internalParse(table, getColumnPositions(table[0]));
			}
		}

		// return an empty list since the input has been empty
		return new Dto[0];
	}

	abstract protected String getModule();

	abstract protected Map<String, String> getMapping();

	/**
	 * Parse a given csv string and create a list of contact dto objects of it.
	 */
	protected Dto[] internalParse(final String[][] table, final Map<String, Integer> positions) {
		final ArrayList<Dto> list = new ArrayList<Dto>();

		final Map<String, String> map = getMapping();

		final ModuleDto moduleDto = DtoModuleRegistry.instance().get(getModule());
		
		// start at 1 to skip header row
		for (int y = 1; y < table.length; y++) {
			final Dto dto = new Dto();
			dto.setModule(getModule());

			for (final String keyHoney : map.keySet()) {
				final String importStr = getGluedValues(table, positions, y, map.get(keyHoney));
				final Serializable typedValue = moduleDto.getFieldById(keyHoney).getTypedData(importStr);
				
				dto.set(keyHoney, typedValue);
			}

			list.add(dto);
		}

		return list.toArray(new Dto[0]);
	}

	/**
	 * keySugar may contain some GLUE (e.g. ",") to express that several fields should be concatenated during import. If we find such a key we have to split the different keys and then resolve and concatenate their values.
	 */
	private String getGluedValues(final String[][] table, final Map<String, Integer> positions, int y, final String keySugar) {
		if (keySugar.contains(GLUE)) {
			final String[] actualKeys = keySugar.split(GLUE);
			String str = "";

			for (int i = 0; i < actualKeys.length; i++) {
				str += table[y][positions.get(actualKeys[i])];
				
				if (i < actualKeys.length -1) {
					str += " ";
				}
			}
			return str;
		} else {
			return table[y][positions.get(keySugar)];
		}
	}

	protected String[][] getData(final String csvString) {
		final String[] lines = csvString.split(LINE_SEP);
		final String[][] table = new String[lines.length][];

		for (int y = 0; y < lines.length; y++) {
			table[y] = getCleanedRow(lines[y].split(COL_SEP));
		}

		return table;
	}

	private String[] getCleanedRow(final String[] dirtyColumns) {
		final String[] columns = new String[dirtyColumns.length];

		for (int i = 0; i < dirtyColumns.length; i++) {
			columns[i] = getFieldContentWithoutFieldDelimiter(dirtyColumns[i]);
		}

		return columns;
	}

	/**
	 * returns a map that stores the positions of all fields in the header row i.e. {name: 10, address: 11, email: 12, ...}
	 */
	protected Map<String, Integer> getColumnPositions(final String[] headerColumns) {
		final Map<String, Integer> map = new HashMap<String, Integer>();

		int columnNumber = 0;
		for (final String columnStr : headerColumns) {
			map.put(columnStr, columnNumber++);
		}

		return map;
	}

	/**
	 * returns only the actual field content without the surrounding delimiter i.e. "foo" -> foo
	 */
	private String getFieldContentWithoutFieldDelimiter(final String fieldWithSurroundingDelimiter) {
		final int delLen = FIELD_DEL.length();
		final int fieldLength = fieldWithSurroundingDelimiter.length();

		return fieldWithSurroundingDelimiter.substring(delLen, fieldLength - delLen);
	}
}
