package honeycrm.client.misc;

import java.io.Serializable;

public class PluginDescription implements Serializable {
	private static final long serialVersionUID = -1216316162666008152L;
	private String name;
	private String description;

	public PluginDescription() {
	}
	
	public PluginDescription(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
}
