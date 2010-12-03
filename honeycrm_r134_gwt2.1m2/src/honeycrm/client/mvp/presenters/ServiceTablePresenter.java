package honeycrm.client.mvp.presenters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.NumberParser;
import honeycrm.client.misc.View;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.view.client.ListDataProvider;

public class ServiceTablePresenter implements TakesValue<List<Dto>> {
	public interface Display extends TakesValue<ServiceTablePresenter> {
		ListDataProvider<Dto> getProvider();
		void initColumns(ModuleDto moduleDto, View viewMode);
		void updateOverallSum(double sum);
		HasClickHandlers getAdd();
		void hideAddButton();
		void add(Dto newService);
	}

	private final View viewMode;
	private final Display view;
	private final ModuleDto relatedModule;

	public ServiceTablePresenter(final Display view, final View viewMode, final String module, final String fieldName) {
		this.view = view;
		this.relatedModule = ModuleDto.getRelatedDto(module, fieldName);
		this.viewMode = viewMode;
		bind();
	}

	private void bind() {
		view.setValue(this);
		view.initColumns(relatedModule, viewMode);

		if (View.isReadOnly(viewMode)) {
			view.hideAddButton();
		} else {
			view.getAdd().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					add();
				}
			});
		}
	}

	@Override
	public void setValue(List<Dto> value) {
		if (null == value) {
			value = new ArrayList<Dto>();
		}
		
		view.getProvider().setList(value);
		// make sure the sum is displayed properly intially
		view.updateOverallSum(getSum(view.getProvider().getList()));
	}

	@Override
	public List<Dto> getValue() {
		final ArrayList<Dto> l = new ArrayList<Dto>();
		for (final Dto d : view.getProvider().getList()) {
			l.add(d);
		}
		return l;
	}

	public void onItemUpdated(int index, Dto object) {
		object.set("sum", getSumForSingleDto(object));
		view.getProvider().refresh();
		view.updateOverallSum(getSum(view.getProvider().getList()));
	}

	private double getSum(final Collection<Dto> data) {
		double currentSum = 0.0;
		for (Dto service : data) {
			currentSum += getSumForSingleDto(service);
		}
		return currentSum;
	}

	public double getSumForSingleDto(final Dto service) {
		final double price = NumberParser.convertToDouble(service.get("price"));
		final double discountValue = NumberParser.convertToDouble(service.get("discount"));
		final double discount = ("%".equals(service.get("kindOfDiscount"))) ? (discountValue / 100 * price) : (discountValue);
		final double qty = NumberParser.convertToDouble(service.get("quantity"));

		return (price - discount) * qty;
	}

	public void add() {
		final Dto newService = relatedModule.createDto();
		newService.set("quantity", 1);
		newService.set("discount", 0);
		newService.set("productCode", "");
		newService.set("sum", getSumForSingleDto(newService));
		newService.set("price", 0);

		view.add(newService);
		view.updateOverallSum(getSum(view.getProvider().getList()));
	}
}
