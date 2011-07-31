package honeycrm.server.test.small.mocks;

import honeycrm.client.mvp.presenters.LoadPresenter.Display;

import com.google.gwt.user.client.ui.HasText;

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
	public void showLoadingIndicator(boolean isVisible) {
		this.visible = isVisible;
	}

}
