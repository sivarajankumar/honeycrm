package honeycrm.server.test.small.mocks;

import honeycrm.client.dto.Dto;
import honeycrm.client.mvp.presenters.DetailPresenter;
import honeycrm.client.mvp.presenters.DetailPresenter.Display;

import java.util.HashMap;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;

public class DetailViewMock implements Display {
	private honeycrm.client.mvp.presenters.RelationshipsPresenter.Display relationshipView = new RelationshipsViewMock();
	private HasClickHandlers create = new HasClickHandlersMock();
	private HasClickHandlers save = new HasClickHandlersMock();
	private HasClickHandlers edit = new HasClickHandlersMock();
	private HasClickHandlers cancel = new HasClickHandlersMock();

	@Override
	public void setData(Dto dto) {
		// TODO Auto-generated method stub

	}

	@Override
	public Dto getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPresenter(DetailPresenter modulePresenter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startCreate(HashMap<String, Object> prefilledFields) {
		// TODO Auto-generated method stub

	}

	@Override
	public HasClickHandlers getCreateBtn() {
		return create;
	}

	@Override
	public HasClickHandlers getSaveBtn() {
		return save;
	}

	@Override
	public HasClickHandlers getEditBtn() {
		return edit;
	}

	@Override
	public HasClickHandlers getCancelBtn() {
		return cancel;
	}

	@Override
	public void startEdit() {
	}

	@Override
	public honeycrm.client.mvp.presenters.RelationshipsPresenter.Display getRelationshipsView() {
		return relationshipView;
	}

}
