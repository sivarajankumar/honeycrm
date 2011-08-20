package honeycrm.client;

import com.google.gwt.i18n.client.Messages;

public interface LocalizedMessages extends Messages {
	// TODO this should be defined as a method returning String[]�as part of a Constants interface
	String availableLocales();

	String login();
	String password();
	String userName();
	String loading();
	String welcome(String login);
	String loginMessage(String usernameForTesting);
	String language();
	String cannotGetConfiguration();
	
	String finish();
	String cancel();
	String save();
	String edit();
	String add();
	String delete();
	String create();
	String createPdf();
	
	String checkCredentials();
	String invalidLogin();
	String loginSuccessful();
	String initializing();
	
	String sum();

	String help();
	String reports();
	String misc();
	String profile();
	String logout();
	
	String globalSearch();

	String uploadPlugin();
	String response();

	String moduleContacts();
	String moduleProducts();
	String moduleDashboard();
	String moduleProposals();
	
	String contactsName();
	String contactsEmail();
	String contactsPhone();
	String contactsNotes();
	
	String productsName();
	
	String proposalsName();
	String proposalsContact();
	String proposalsDate();
	String proposalsProducts();
	String proposalsSelectedProducts();
}
