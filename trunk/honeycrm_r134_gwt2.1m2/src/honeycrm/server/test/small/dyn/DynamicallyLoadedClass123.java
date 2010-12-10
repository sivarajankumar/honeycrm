package honeycrm.server.test.small.dyn;

import honeycrm.server.test.Plugin;

public class DynamicallyLoadedClass123 implements Plugin {
	@Override
	public String request() {
		return "42";
	}
}
