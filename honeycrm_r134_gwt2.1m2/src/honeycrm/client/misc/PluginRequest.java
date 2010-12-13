package honeycrm.client.misc;

import java.io.Serializable;

public class PluginRequest implements Serializable {
	private static final long serialVersionUID = 4095202438080102056L;
	private String request;
	private PluginDescription description;

	public PluginRequest() {
	}
	
	public PluginRequest(PluginDescription description, String request) {
		this.request = request;
		this.description = description;
	}
	
	public String getRequest() {
		return request;
	}
	
	public PluginDescription getDescription() {
		return description;
	}
}
