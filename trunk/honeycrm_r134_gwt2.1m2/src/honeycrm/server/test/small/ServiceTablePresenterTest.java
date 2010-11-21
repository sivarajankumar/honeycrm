package honeycrm.server.test.small;

import java.util.ArrayList;

import honeycrm.client.dto.Dto;
import honeycrm.client.mvp.presenters.ServiceTablePresenter;
import honeycrm.client.mvp.presenters.ServiceTablePresenter.Display;
import honeycrm.server.domain.UniqueService;
import honeycrm.server.test.small.mocks.ServiceTableViewMock;
import junit.framework.TestCase;

public class ServiceTablePresenterTest extends TestCase {
	private Display view;
	private String module;
	private ServiceTablePresenter presenter;

	@Override
	protected void setUp() throws Exception {
		this.view = new ServiceTableViewMock();
		this.module = UniqueService.class.getSimpleName();
		this.presenter = new ServiceTablePresenter(view, module);
	}

	public void testCreate() {
		presenter = new ServiceTablePresenter(view, module);
	}

	public void testSetDataNull() {
		presenter.setValue(null);
	}

	public void testSetDataEmpty() {
		presenter.setValue(new ArrayList<Dto>());
	}

	public void testSetDataReal() {
		presenter.setValue(getDtos());
	}

	private ArrayList<Dto> getDtos() {
		final ArrayList<Dto> dtos = new ArrayList<Dto>();
		for (int i = 0; i < 4; i++) {
			final Dto dto = new Dto(module);
			dto.set("price", i % 2 == 0 ? (int) i : (long) i); // every 2nd dto uses Long instead of Integer
			dto.set("quantity", i % 2 == 0 ? (int) i : (long) i);
			dtos.add(dto);
		}
		return dtos;
	}

	public void testGetData() {
		presenter.setValue(new ArrayList<Dto>());
		assertTrue(presenter.getValue().isEmpty());
	}

	public void testSumForSingleRow() {
		final Dto s = new Dto(module);
		s.set("price", 2);
		s.set("discount", 1);
		s.set("discountKind", "abs");
		s.set("quantity", 3);

		assertEquals((2 - 1) * 3, (int) presenter.getSumForSingleDto(s));
	}

	public void testRowChanged() {
		presenter.setValue(getDtos());
		presenter.rowChanged(0);
	}
}
