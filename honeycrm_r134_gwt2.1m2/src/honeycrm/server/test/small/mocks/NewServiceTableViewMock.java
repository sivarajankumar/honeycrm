package honeycrm.server.test.small.mocks;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.view.client.ListDataProvider;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.View;
import honeycrm.client.mvp.presenters.ServiceTablePresenter;
import honeycrm.client.mvp.presenters.ServiceTablePresenter.Display;

public class NewServiceTableViewMock implements Display {
	private HasClickHandlers add = new HasClickHandlersMock();

	@Override
	public ListDataProvider<Dto> getProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initColumns(ModuleDto moduleDto, View viewMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOverallSum(double sum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(ServiceTablePresenter value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServiceTablePresenter getValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
