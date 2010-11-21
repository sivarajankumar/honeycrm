package honeycrm.server.test.small.mocks;

import honeycrm.client.dto.Dto;
import honeycrm.client.mvp.presenters.ServiceTablePresenter;
import honeycrm.client.mvp.presenters.ServiceTablePresenter.Display;
import honeycrm.server.domain.UniqueService;

public class ServiceTableViewMock implements Display {

	@Override
	public void setPresenter(ServiceTablePresenter presenter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSumForRow(Dto dto, int row) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOverallSum(double sum) {
		// TODO Auto-generated method stub

	}

	@Override
	public Dto getDtoFromRow(int row) {
		final Dto dto = new Dto(UniqueService.class.getSimpleName());
		dto.set("price", 2);
		dto.set("quantity", 1);
		dto.set("discountKind", "abs");
		dto.set("discount", 1);
		return dto;
	}

	@Override
	public void insertDtoIntoRow(Dto dto, int row) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
