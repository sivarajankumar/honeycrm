package honeycrm.server.test.small;

import honeycrm.server.test.Plugin;

public class DynamicallyLoadedClass implements Plugin {
	@Override
	public String request() {
		return "42";
	}
}
