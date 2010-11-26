package honeycrm.client;

import java.util.List;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.View;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.view.client.ListDataProvider;

public class ServiceTablePresenter implements TakesValue<List<Dto>> {
	public interface Display {
		void setPresenter(ServiceTablePresenter presenter);
		ListDataProvider<Dto> getProvider();
		void initColumns(ModuleDto moduleDto, View viewMode);
	}

	private View viewMode;
	private Display view;
//	private ReadServiceAsync readService;
	private String module;
	private String fieldName;

	public ServiceTablePresenter(final Display view, final View viewMode, final String module, final String fieldName/* , final ReadServiceAsync readService */) {
		this.view = view;
		this.module = module;
		this.fieldName = fieldName;
		this.viewMode = viewMode;
		// this.readService = readService;
		bind();
	}

	private void bind() {
		view.setPresenter(this);
		view.initColumns(ModuleDto.getRelatedDto(module, fieldName), viewMode);
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
