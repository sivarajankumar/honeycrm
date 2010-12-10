package honeycrm.server.test.small.dyn;

import honeycrm.server.test.Plugin;

public class DynamicallyLoadedClassTwo123 implements Plugin {
	@Override
	public String request() {
		return "23";
	}
}
