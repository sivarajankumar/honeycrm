package honeycrm.server.test.small.mocks;

import com.google.gwt.user.client.ui.HasText;

public class HasTextMock implements HasText {
	private String str = "";

	@Override
	public String getText() {
		return str;
	}

	@Override
	public void setText(String text) {
		this.str = text;
	}
}
