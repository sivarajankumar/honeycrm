package honeycrm.server.test.small;

import honeycrm.client.csv.CsvImporter;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.services.NewDtoWizard;
import junit.framework.TestCase;

public class CsvImportTest extends TestCase {
	static {
		DtoModuleRegistry.create(NewDtoWizard.getConfiguration());
	}

	public void testContactsImport() {
		final String csv = "\"id\",\"date_entered\",\"date_modified\",\"modified_user_id\",\"created_by\",\"description\",\"deleted\",\"assigned_user_id\",\"salutation\",\"first_name\",\"last_name\",\"title\",\"department\",\"do_not_call\",\"phone_home\",\"phone_mobile\",\"phone_work\",\"phone_other\",\"phone_fax\",\"primary_address_street\",\"primary_address_city\",\"primary_address_state\",\"primary_address_postalcode\",\"primary_address_country\",\"alt_address_street\",\"alt_address_city\",\"alt_address_state\",\"alt_address_postalcode\",\"alt_address_country\",\"assistant\",\"assistant_phone\",\"lead_source\",\"reports_to_id\",\"birthdate\",\"campaign_id\",\"email1\",\"account_name\",\"assigned_user_name\"\n" + "\"5f511524-6562-ce77-1048-4c1904f7b660\",\"06/16/2010 07:05 pm\",\"06/16/2010 07:05 pm\",\"1\",\"1\",\"\",\"0\",\"seed_max_id\",\"\",\"Irma\",\"Bustillos\",\"Mgr Operations\",\"\",\"0\",\"(854) 239-9689\",\"(869) 613-8722\",\"(911) 108-9134\",\"\",\"\",\"777 West Filmore Ln\",\"Kansas City\",\"CA\",\"84893\",\"USA\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"Partner\",\"\",\"\",\"\",\"section.phone.support@example.edu\",\"Powell Funding\",\"max\"\n" + "\"64a2e33c-9c13-1b67-73df-4c19042f93f1\",\"06/16/2010 07:05 pm\",\"06/16/2010 07:05 pm\",\"1\",\"1\",\"\",\"0\",\"seed_will_id\",\"\",\"Ahmed\",\"Kiefer\",\"VP Operations\",\"\",\"0\",\"(222) 927-4275\",\"(002) 877-3807\",\"(761) 751-6588\",\"\",\"\",\"9 IBM Path\",\"Salt Lake City\",\"NY\",\"98839\",\"USA\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"Campaign\",\"\",\"\",\"\",\"vegan.vegan.vegan@example.tw\",\"5D Investments\",\"will\"\n" + "\"699e82fa-2587-4013-8a73-4c190457b9f1\",\"06/16/2010 07:05 pm\",\"06/16/2010 07:05 pm\",\"1\",\"1\",\"\",\"0\",\"seed_max_id\",\"\",\"Daryl\",\"Chick\",\"VP Operations\",\"\",\"0\",\"(627) 440-9071\",\"(862) 812-8784\",\"(569) 521-2554\",\"\",\"\",\"1715 Scott Dr\",\"San Mateo\",\"CA\",\"73426\",\"USA\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"Existing Customer\",\"\",\"\",\"\",\"vegan.dev@example.name\",\"Aim Capital Inc\",\"max\"\n" + "\"6e88c558-b29a-4b8f-06bb-4c1904c6f748\",\"06/16/2010 07:05 pm\",\"06/16/2010 07:05 pm\",\"1\",\"1\",\"\",\"0\",\"seed_max_id\",\"\",\"Effie\",\"Southwood\",\"VP Sales\",\"\",\"0\",\"(410) 862-4067\",\"(367) 793-8879\",\"(926) 307-8323\",\"\",\"\",\"9 IBM Path\",\"Los Angeles\",\"CA\",\"38979\",\"USA\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"Web Site\",\"\",\"\",\"\",\"sugar.beans.section@example.cn\",\"Underwater Mining Inc.\",\"max\"\n" + "\"73515873-c2ba-50a6-1705-4c1904494689\",\"06/16/2010 07:05 pm\",\"06/16/2010 07:05 pm\",\"1\",\"1\",\"\",\"0\",\"seed_will_id\",\"\",\"Alexandra\",\"Spring\",\"Director Sales\",\"\",\"0\",\"(376) 573-3885\",\"(238) 534-9821\",\"(743) 598-2149\",\"\",\"\",\"67321 West Siam St.\",\"San Mateo\",\"NY\",\"16675\",\"USA\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"Conference\",\"\",\"\",\"\",\"section80@example.biz\",\"Nelson Inc\",\"will\"\n" + "\"78159143-432f-5063-dc07-4c1904c0f30c\",\"06/16/2010 07:05 pm\",\"06/16/2010 07:05 pm\",\"1\",\"1\",\"\",\"0\",\"seed_sally_id\",\"\",\"Jerrell\",\"Lachance\",\"VP Sales\",\"\",\"0\",\"(170) 360-1699\",\"(989) 323-1320\",\"(320) 473-5892\",\"\",\"\",\"123 Anywhere Street\",\"Denver\",\"CA\",\"64860\",\"USA\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"Other\",\"\",\"\",\"\",\"vegan.im.section@example.com\",\"Cumberland Trails Inc\",\"sally\"";

		final CsvImporter importer = CsvImporter.get("Contact");
		final Dto[] list = importer.parse(csv);

		assertTrue(importer.parse("\n").length == 0);
		assertTrue(importer.parse("").length == 0);
		assertTrue(importer.parse(null).length == 0);

		assertNotNull(list);
		assertEquals(6, list.length);

		list[0].get("name").equals("Irma Bustillos");
		list[0].get("email").equals("vegan.vegan.vegan@example.tw");
		list[5].get("name").equals("Jerrell Lachance");
	}
}
