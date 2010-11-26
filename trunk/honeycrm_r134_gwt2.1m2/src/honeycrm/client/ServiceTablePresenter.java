package honeycrm.client;

import java.util.ArrayList;
import java.util.List;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.View;
import honeycrm.client.services.ReadServiceAsync;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;

public class ServiceTablePresenter implements TakesValue<List<Dto>> {
	public interface Display {
		void setPresenter(ServiceTablePresenter presenter);
		HasClickHandlers getAdd();
		ListDataProvider<Dto> getProvider();
		void initColumns(ModuleDto moduleDto, View viewMode);
	}

	private View viewMode;
	private Display view;
	private ModuleDto moduleDto;
	private ReadServiceAsync readService;

	public ServiceTablePresenter(final Display view, final View viewMode, final String module, final ReadServiceAsync readService) {
		this.view = view;
		this.viewMode = View.EDIT;
		this.readService = readService;
		this.moduleDto = DtoModuleRegistry.instance().get(module);
		bind();
	}

	private void bind() {
		view.setPresenter(this);

		view.initColumns(moduleDto, viewMode);
		view.getAdd().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final int i = view.getProvider().getList().size();

				final Dto d = moduleDto.createDto();
				d.set("name", "paul" + i);

				view.getProvider().getList().add(d);
			}
		});
	}

	@Override
	public void setValue(List<Dto> value) {
		view.getProvider().setList(value);
	}

	@Override
	public List<Dto> getValue() {
		return view.getProvider().getList();
	}
}
