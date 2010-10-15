package honeycrm.client.login;

public class User {
	private static long userId;
	private static String login;

	public static void initUser(final long userId, final String login) {
		User.userId = userId;
		User.login = login;
	}

	public static long getUserId() {
		return userId;
	}

	public static String getLogin() {
		return login;
	}
}
