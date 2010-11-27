package honeycrm.client.misc;

import honeycrm.client.dto.Dto;

import com.google.gwt.core.client.JavaScriptObject;

public class QuicksearchValue {
	private final Dto dto;
	private final JavaScriptObject returnValue;

	public QuicksearchValue(final Dto originalDto, final JavaScriptObject returnValue) {
		this.dto = originalDto;
		this.returnValue = returnValue;
	}

	public Dto getDto() {
		return dto;
	}

	public JavaScriptObject getReturnValue() {
		return returnValue;
	}
}
