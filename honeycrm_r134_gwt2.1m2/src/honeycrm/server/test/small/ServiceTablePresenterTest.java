package honeycrm.server.test.small;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.misc.View;
import honeycrm.client.mvp.presenters.ServiceTablePresenter;
import honeycrm.client.mvp.presenters.ServiceTablePresenter.Display;
import honeycrm.server.NewDtoWizard;
import honeycrm.server.domain.Contract;
import honeycrm.server.domain.UniqueService;
import honeycrm.server.test.small.mocks.NewServiceTableViewMock;

import java.io.Serializable;
import java.util.ArrayList;

import junit.framework.TestCase;

public class ServiceTablePresenterTest extends TestCase {
	private Display view;
	private String module;
	private ServiceTablePresenter presenter;

	@Override
	protected void setUp() throws Exception {
		DtoModuleRegistry.create(NewDtoWizard.getConfiguration());

		this.view = new NewServiceTableViewMock();
		this.module = UniqueService.class.getSimpleName();
		this.presenter = new ServiceTablePresenter(view, View.EDIT, Contract.class.getSimpleName(), "uniqueServices");
	}

	public void testCreate() {
		this.presenter = new ServiceTablePresenter(view, View.EDIT, Contract.class.getSimpleName(), "uniqueServices");
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
		s.set("kindOfDiscount", "abs");
		s.set("quantity", 3);

		assertEquals((2 - 1) * 3, (int) presenter.getSumForSingleDto(s));
	}

	public void testRowChanged() {
		presenter.setValue(getDtos());
	}

	public void testEnumFieldsHaveValidDefaultsSet() {
		final Dto newDto = DtoModuleRegistry.instance().get(UniqueService.class.getSimpleName()).createDto();

		for (final String field : new String[] { "unit", "kindOfDiscount" }) {
			final Serializable value = newDto.get(field);

			assertNotNull(value);
			assertFalse(value.toString().isEmpty());
		}
	}

	public void testOnItemUpdate() {
		final Dto s = new Dto(UniqueService.class.getSimpleName());
		presenter.onItemUpdated(0, s);
	}

	public void testAdd() {
		presenter.add();
	}

	public void testSetGetValue() {
		final Dto service = new Dto(UniqueService.class.getSimpleName());
		service.set("price", 23);
		service.set("quantity", 1);
		service.set("productCode", "foo");
		
		final ArrayList<Dto> list = new ArrayList<Dto>();
		list.add(service);

		presenter.setValue(list);

		assertFalse(presenter.getValue().isEmpty());
		assertEquals(service.get("price"), presenter.getValue().get(0).get("price"));
		assertEquals(service.get("productCode"), presenter.getValue().get(0).get("productCode"));
	}
}
