package honeycrm.client.csv;

import java.util.HashMap;
import java.util.Map;

public class CsvImporterAccounts extends CsvImporter {
	@Override
	protected String getModule() {
		return "account";
	}

	@Override
	protected Map<String, String> getMapping() {
		final Map<String, String> map = new HashMap<String, String>();

		map.put("name", "name");
		map.put("annualRevenue", "annual_revenue");
		map.put("phoneOffice", "phone_office");

		map.put("industry", "industry");
		map.put("billingAddress", "billing_address_city,billing_address_country");
		map.put("shippingAddress", "shipping_address_city,shipping_address_country");
		
		map.put("employees", "employees");
		map.put("website", "website");
		map.put("tickerSymbol", "ticker_symbol");
		
		// name industry annual_revenue phone_fax billing_address_street billing_address_city billing_address_state billing_address_postalcode billing_address_country
		// rating phone_office phone_alternate website ownership employees ticker_symbol shipping_address_street shipping_address_city shipping_address_state shipping_address_postalcode shipping_address_country
		// parent_id sic_code campaign_id email1

		return map;
	}
}
