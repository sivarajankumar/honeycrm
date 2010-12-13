package honeycrm.client.misc;

import java.io.Serializable;

public class PluginResponse implements Serializable {
	private static final long serialVersionUID = 643807241448323382L;
	private String response;

	public PluginResponse() {
	}

	public PluginResponse(final String respone) {
		this.response = respone;
	}
	
	public String getResponse() {
		return response;
	}
}
