package honeycrm.client.csv;

import java.util.HashMap;
import java.util.Map;

public class CsvImporterContacts extends CsvImporter {
	@Override
	protected Map<String, String> getMapping() {
		final Map<String, String> map = new HashMap<String, String>();

		map.put("name", "first_name,last_name");
		map.put("email", "email1");
		map.put("phone", "phone_work");
		// map.put("doNotCall", "do_not_call");

		return map;
	}

	@Override
	protected String getModule() {
		return "contact";
	}
}
