package honeycrm.client.offerings;

import honeycrm.client.admin.LogConsole;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.field.AbstractField;
import honeycrm.client.misc.NumberParser;
import honeycrm.client.misc.Observer;
import honeycrm.client.view.AbstractView.View;
import honeycrm.client.view.ITableWidget;
import honeycrm.client.view.RelateWidget;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

// TODO caching bug: the service lists that are returned are out-of-date after save
public class ServiceTableWidget extends ITableWidget {
	private static final int HEADER_ROWS = 1;
	private final FlexTable table = new FlexTable();
	private final ModuleDto moduleDto = DtoModuleRegistry.instance().get("service");
	private final View view;
	private final Label sum = new Label();
	private final Map<Integer, Dto> model = new HashMap<Integer, Dto>();

	public ServiceTableWidget(final View view) {
		this.view = view;

		initHeader();

		sum.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		final VerticalPanel panel = new VerticalPanel();
		panel.add(table);
		panel.add(sum);

		if (view != View.DETAIL) {
			// only insert the add button if we are not in detail view
			final Button addBtn = new Button("Add");
			addBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					final int rows = table.getRowCount();
					final int newRowId = rows;
					final String[] fields = moduleDto.getListFieldIds();

					model.put(newRowId, moduleDto.createDto());

					for (int x = 0; x < fields.length; x++) {
						final String index = fields[x];
						table.setWidget(rows, x, addChangeEvents(index, moduleDto.getFieldById(index).getWidget(view, moduleDto.createDto(), index), newRowId));
					}
				}
			});

			panel.add(addBtn);
		}

		initWidget(panel);
	}

	private void initHeader() {
		for (int x = 0; x < moduleDto.getListFieldIds().length; x++) {
			final AbstractField field = moduleDto.getFieldById(moduleDto.getListFieldIds()[x]);
			table.setWidget(0, x, field.getWidget(View.LIST_HEADER, moduleDto.createDto(), moduleDto.getListFieldIds()[x]));
		}
	}

	private Widget addChangeEvents(final String index, final Widget widget, final int row) {
		if ("price".equals(index) || "quantity".equals(index) || "discount".equals(index) || "kindOfDiscount".equals(index)) {
			if (widget instanceof TextBox) {
				((TextBox) widget).addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						rowChanged(row);
					}
				});
			}
		} else if ("productID".equals(index)) {
			if (widget instanceof RelateWidget) {
				((RelateWidget) widget).subscribe(new Observer<Dto>() {
					@Override
					public void notify(final Dto value) {
						// overwrite price with price of received dto
						if (model.containsKey(row) && null != value.get("price")) {
							final Dto updatedDto = getDtoFromRow(row);
							updatedDto.set("price", value.get("price"));

							insertDtoInTableRow(updatedDto, true, row);
						}
						LogConsole.log("received original price: " + value.get("price").toString() + " in row " + row);
					}
				});
			}
		}

		return widget;
	}

	protected void rowChanged(final int row) {
		model.put(row, getDtoFromRow(row));
		LogConsole.log("updated data of row " + row);

		updateSum(row);

		sum.setText(NumberFormat.getCurrencyFormat("EUR").format(getSum(model.values())));
	}

	private void updateSum(final int row) {
		for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
			final String index = moduleDto.getListFieldIds()[col];
			final AbstractField field = moduleDto.getFieldById(index);

			if ("sum".equals(index) && table.getWidget(/* HEADER_ROWS + */row, col) instanceof TextBox) {
				final Dto dto = model.get(row);
				final String text = ((TextBox) field.getWidget(View.EDIT, dto, index)).getText();
				final TextBox box = ((TextBox) table.getWidget(/* HEADER_ROWS + */row, col));
				box.setText(text);
			}
		}
	}

	@Override
	public List<Dto> getData() {
		final List<Dto> services = new LinkedList<Dto>();

		for (int row = HEADER_ROWS; row < table.getRowCount(); row++) {
			services.add(getDtoFromRow(row));
		}

		return services;
	}

	private Dto getDtoFromRow(final int row) {
		final Dto s = new Dto();
		s.setModule("service");

		for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
			if (table.getCellCount(/* HEADER_ROWS + */row) > col) {
				final String id = moduleDto.getListFieldIds()[col];
				final Widget widget = table.getWidget(/* HEADER_ROWS + */row, col);

				s.set(id, moduleDto.getFieldById(id).getData(widget));
			}
		}

		s.set("sum", getSumForSingleDto(s));
		return s;
	}

	@Override
	public void setData(List<Dto> data) {
		if (null == data)
			return;

		if (!data.isEmpty()) {
			if (data.get(0) instanceof Dto) {
				final boolean wasTableAlreadyFilled = (table.getRowCount() == HEADER_ROWS + data.size());

				for (int row = 0; row < data.size(); row++) {
					insertDtoInTableRow(data.get(row), wasTableAlreadyFilled, HEADER_ROWS + row);
				}
			} else {
				Window.alert("Expected Service. Received " + data.get(0).getClass());
				throw new RuntimeException("Expected Service. Received " + data.get(0).getClass());
			}
		}

		sum.setText(NumberFormat.getCurrencyFormat("EUR").format(getSum(data)));
	}

	/**
	 * Insert or update the table row in the UI with the data from the model stored in dto.
	 * 
	 * @param dto
	 *            The model object storing the data that should be inserted / updated in the UI.
	 * @param isUpdate
	 *            True if the UI should be updated (reuses widgets). False otherwise (creates new widgets).
	 * @param row
	 *            The number of the row that has been changed.
	 */
	private void insertDtoInTableRow(final Dto dto, final boolean isUpdate, int row) {
		// make sure the dto is stored
		model.put(row, dto);

		for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
			final String index = moduleDto.getListFieldIds()[col];
			final AbstractField field = moduleDto.getFieldById(index);

			if (isUpdate) {
				// update widget because it already exists
				if (table.getWidget(row, col) instanceof TextBox) {
					// TODO this is faaaaar to crappy!
					// TODO this should be done by field currency somehow.. the fields should provide a "String format(Serializable value);" method.
					final String text = ((TextBox) field.getWidget(View.EDIT, dto, index)).getText();
					final TextBox box = ((TextBox) table.getWidget(row, col));
					box.setText(text);
				} else if (table.getWidget(row, col) instanceof ListBox) {
					final String text = String.valueOf(dto.get(index));
					final ListBox box = ((ListBox) table.getWidget(row, col));
					for (int i=0; i<box.getItemCount(); i++) {
						box.setItemSelected(i, box.getItemText(i).equals(text));
					}
				}
			} else {
				// add a new widget and new click handler
				table.setWidget(row, col, field.getWidget(view, dto, (index)));
				addChangeEvents(index, table.getWidget(/* HEADER_ROWS + */row, col), row);
			}
		}
	}

	private double getSum(final Collection<Dto> data) {
		double currentSum = 0.0;
		for (Dto service : data) {
			currentSum += getSumForSingleDto(service);
		}
		return currentSum;
	}

	private double getSumForSingleDto(final Dto service) {
		final double price = NumberParser.convertToDouble(service.get("price"));
		final double discountValue = NumberParser.convertToDouble(service.get("discount"));
		final double discount = ("%".equals(service.get("kindOfDiscount"))) ? (discountValue / 100 * price) : (discountValue);

		return (price - discount) * (Integer) service.get("quantity");
	}
}
