package honeycrm.client.csv;

import honeycrm.client.dto.Dto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CsvImporter extends AbstractCsv {
	/**
	 * Parse a given csv string and create a list of contact dto objects of it.
	 */
	public List<Dto> parse(final String csvString) {
		// TODO do some prechecks to find out whether the file is correct (thus proper col separators are used etc).
		
		final List<Dto> list = new LinkedList<Dto>();

		final String[][] table = getData(csvString);
		final Map<String, Integer> positions = getColumnPositions(table[0]);

		// start at 1 to skip header row
		for (int y = 1; y < table.length; y++) {
			final Dto newContact = new Dto();

			newContact.set("name", table[y][positions.get("first_name")] + " " + table[y][positions.get("last_name")]);
			newContact.set("email", table[y][positions.get("email1")]);
			newContact.set("phone", table[y][positions.get("phone_work")]);
			newContact.set("doNotCall", table[y][positions.get("do_not_call")].equals("1"));
			
			list.add(newContact);
		}
		
		return list;
	}
	
	private String[][] getData(final String csvString) {
		final String[] lines = csvString.split(LINE_SEP);
		final String[][] table = new String[lines.length][];

		for (int y = 0; y < lines.length; y++) {
			table[y] = getCleanedRow(lines[y].split(COL_SEP));
		}

		return table;
	}
	
	private String[] getCleanedRow(final String[] dirtyColumns) {
		final String[] columns = new String[dirtyColumns.length];
		
		for (int i=0; i<dirtyColumns.length; i++) {
			columns[i] = getFieldContentWithoutFieldDelimiter(dirtyColumns[i]);
		}
		
		return columns;
	}

	/**
	 * returns a map that stores the positions of all fields in the header row i.e. {name: 10, address: 11, email: 12, ...}
	 */
	private Map<String, Integer> getColumnPositions(final String[] headerColumns) {
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
