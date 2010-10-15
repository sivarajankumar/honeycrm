package honeycrm.client.misc;

import com.google.gwt.user.client.Command;

public class Timer {
	/**
	 * Executes the given command and returns the number of seconds required for execution.
	 */
	public static long getTime(final Command command) {
		final long before = System.currentTimeMillis();
		command.execute();
		return System.currentTimeMillis() - before;
	}
}
