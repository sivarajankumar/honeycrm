package honeycrm.client.mvp.presenters;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.NumberParser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.user.client.TakesValue;

public class ServiceTablePresenter implements TakesValue<ArrayList<Dto>>{
	public interface Display {
		void setPresenter(ServiceTablePresenter presenter);
		void setSumForRow(Dto dto, int row);
		void setOverallSum(double sum);
		Dto getDtoFromRow(int row);
		void insertDtoIntoRow(Dto dto, int row);
		int getRowCount();
	}

	private final Display view;
	private final ModuleDto moduleDto;
	private final HashMap<Integer, Dto> model = new HashMap<Integer, Dto>();

	public ServiceTablePresenter(Display view, final String module) {
		this.view = view;
		this.moduleDto = DtoModuleRegistry.instance().get(module);
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

		// TODO simplifiy this!
		if (service.get("quantity") instanceof Long) {
			return (price - discount) * (Long) service.get("quantity");
		} else if (service.get("quantity") instanceof Integer) {
			return (price - discount) * (Integer) service.get("quantity");
		} else {
			throw new RuntimeException("Cannot determine sum");
		}
	}

	public void rowChanged(int row) {
		final Dto updatedService = view.getDtoFromRow(row);
		updatedService.set("sum", getSumForSingleDto(updatedService));
		model.put(row, updatedService);
		
		view.setSumForRow(updatedService, row);
		view.setOverallSum(getSum(model.values()));
	}

	@Override
	public ArrayList<Dto> getValue() {
		final ArrayList<Dto> services = new ArrayList<Dto>();
		for (final Dto d: model.values()) {
			services.add(d);
		}
		return services;
		
		/*final ArrayList<Dto> services = new ArrayList<Dto>();

		for (int row = 0; row < view.getRowCount(); row++) {
			services.add(view.getDtoFromRow(row));
		}

		return services;*/
	}

	@Override
	public void setValue(final ArrayList<Dto> data) {
		if (null == data) {
			return;
		}

		if (!data.isEmpty()) {
			if (data.get(0) instanceof Dto) {
				for (int row = 0; row < data.size(); row++) {
					model.put(row, data.get(row));
					view.insertDtoIntoRow(data.get(row), row);
				}
			} else {
				throw new RuntimeException("Expected Service. Received " + data.get(0).getClass());
			}
		}

		view.setOverallSum(getSum(model.values()));
	}

	public void receivedProduct(int row, Dto product) {
		if (model.containsKey(row) && null != product.get("price")) {
			final Dto updatedService = view.getDtoFromRow(row);
			updatedService.set("price", product.get("price"));
			updatedService.set("productID", product.getId());
			updatedService.set("productCode", product.get("productCode"));

			model.put(row, updatedService);
			
			view.insertDtoIntoRow(updatedService, row);
		}
	}

	public void appendRow() {
		final int newRowId = view.getRowCount();
		model.put(newRowId, moduleDto.createDto());
		view.insertDtoIntoRow(moduleDto.createDto(), newRowId);
	}
}
