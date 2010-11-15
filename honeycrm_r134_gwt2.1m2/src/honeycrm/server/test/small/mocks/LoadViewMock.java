package honeycrm.server.test.small.mocks;

import com.google.gwt.user.client.ui.HasText;

import honeycrm.client.mvp.presenters.LoadPresenter.Display;

public class LoadViewMock implements Display {
	private final HasText indicator;
	private boolean visible = false;

	public LoadViewMock() {
		this.indicator = new HasText() {
			private String text = "";

			@Override
			public void setText(String text) {
				this.text  = text;
			}
			
			@Override
			public String getText() {
				return text;
			}
		};
	}
	
	@Override
	public HasText getLoadIndicator() {
		return indicator;
	}

	@Override
	public void showLoadingIndicator(boolean isVisible) {
		this.visible = isVisible;
	}

}
