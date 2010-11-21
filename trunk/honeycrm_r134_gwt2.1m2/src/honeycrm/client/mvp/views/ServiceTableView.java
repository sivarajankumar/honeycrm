package honeycrm.client.mvp.views;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.field.AbstractField;
import honeycrm.client.misc.Observer;
import honeycrm.client.misc.View;
import honeycrm.client.mvp.presenters.ServiceTablePresenter;
import honeycrm.client.mvp.presenters.ServiceTablePresenter.Display;
import honeycrm.client.view.ITableWidget;
import honeycrm.client.view.RelateWidget;

import java.util.ArrayList;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ServiceTableView extends ITableWidget implements Display {
	private static final int HEADER_ROWS = 1;
	private final FlexTable table = new FlexTable();
	private final ModuleDto moduleDto;
	private final View view;
	private final HTML sum = new HTML();
	private final VerticalPanel panel = new VerticalPanel();
	private ServiceTablePresenter presenter;

	public ServiceTableView(final Dto dto, final String fieldId, final View view) {
		this.view = view;
		this.moduleDto = ModuleDto.getRelatedDto(dto.getModule(), fieldId);
		initialize();
		initWidget(panel);
	}

	private void initialize() {
		initHeader();

		sum.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

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

//					presenter.appendRow();

					for (int x = 0; x < fields.length; x++) {
						final String index = fields[x];
						table.setWidget(rows, x, addChangeEvents(index, moduleDto.getFieldById(index).getWidget(View.CREATE, moduleDto.createDto(), index), newRowId));
					}
				}
			});

			panel.add(addBtn);
		}
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
						presenter.rowChanged(row);
					}
				});
			}
		} else if ("productID".equals(index)) {
			if (widget instanceof RelateWidget) {
				((RelateWidget) widget).subscribe(new Observer<Dto>() {
					@Override
					public void notify(final Dto value) {
						// overwrite price with price of received dto
						presenter.receivedProduct(row, value);
					}
				});
			}
		}

		return widget;
	}

	@Override
	public ArrayList<Dto> getData() {
		if (null == presenter) {
			throw new RuntimeException("Cannot return a value since no presenter has been set.");
		} else {
			return presenter.getValue();
		}
	}

	@Override
	public Dto getDtoFromRow(final int row) {
		final Dto s = new Dto();
		s.setModule(moduleDto.getModule());

		for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
			if (table.getCellCount(row) > col) {
				final String id = moduleDto.getListFieldIds()[col];
				final Widget widget = table.getWidget(row, col);

				s.set(id, moduleDto.getFieldById(id).getData(widget));
			}
		}

		return s;
	}

	@Override
	public void insertDtoIntoRow(Dto dto, int row) {
		// TODO initialize isUpdate variable properly
		insertDtoInTableRow(dto, false, HEADER_ROWS + row);
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
					for (int i = 0; i < box.getItemCount(); i++) {
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

	@Override
	public void setPresenter(ServiceTablePresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setSumForRow(Dto dto, int row) {
		for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
			final String index = moduleDto.getListFieldIds()[col];
			final AbstractField field = moduleDto.getFieldById(index);

			if ("sum".equals(index) && table.getWidget(row, col) instanceof TextBox) {
				final String text = ((TextBox) field.getWidget(View.EDIT, dto, index)).getText();
				final TextBox box = ((TextBox) table.getWidget(row, col));
				box.setText(text);
			}
		}
	}

	@Override
	public void setOverallSum(double calculatedSum) {
		sum.setHTML("<b>" + NumberFormat.getCurrencyFormat("EUR").format(calculatedSum) + "</b>");
	}

	@Override
	public int getRowCount() {
		return table.getRowCount() - HEADER_ROWS;
	}
}