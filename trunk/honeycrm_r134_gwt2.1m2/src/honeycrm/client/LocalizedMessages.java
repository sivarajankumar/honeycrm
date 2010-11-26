package honeycrm.client;

import com.google.gwt.i18n.client.Messages;

public interface LocalizedMessages extends Messages {
	// TODO this should be defined as a method returning String[]Êas part of a Constants interface
	String availableLocales();

	String login();
	String password();
	String userName();
	String loading();
	String welcome(String login);
	String loginMessage(String usernameForTesting);
	String language();
	String cannotGetConfiguration();
	
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
}
