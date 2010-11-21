package honeycrm.server.test.small.mocks;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import honeycrm.client.mvp.presenters.CsvImportPresenter.Display;

public class CsvImportViewMock implements Display {
	private HasClickHandlers cancel = new HasClickHandlersMock();
	private HasClickHandlers importBtn = new HasClickHandlersMock();
	private HasText status = new HasTextMock();
	private HasText header = new HasTextMock();
	private HasValue<String> textArea = new HasValueMock<String>();
	
	@Override
	public HasClickHandlers getCancelBtn() {
		return cancel;
	}

	@Override
	public HasClickHandlers getImportBtn() {
		return importBtn ;
	}

	@Override
	public HasText getStatus() {
		return status;
	}

	@Override
	public HasText getHeader() {
		return header;
	}

	@Override
	public HasValue<String> getTextArea() {
		return textArea ;
	}

	@Override
	public Widget asWidget() {
		return null;
	}

	@Override
	public void center() {
	}

	@Override
	public void hide() {
	}
}
