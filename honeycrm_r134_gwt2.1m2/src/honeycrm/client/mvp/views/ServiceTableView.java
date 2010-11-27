package honeycrm.client.mvp.views;

import java.io.Serializable;
import honeycrm.client.LocalizedMessages;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.field.AbstractField;
import honeycrm.client.misc.Callback;
import honeycrm.client.misc.View;
import honeycrm.client.mvp.presenters.ServiceTablePresenter;
import honeycrm.client.mvp.presenters.ServiceTablePresenter.Display;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class ServiceTableView extends Composite implements Display {
	private static List1UiBinder uiBinder = GWT.create(List1UiBinder.class);
	private static final LocalizedMessages constants = GWT.create(LocalizedMessages.class);

	interface List1UiBinder extends UiBinder<Widget, ServiceTableView> {
	}

	private ServiceTablePresenter presenter;

	@UiField
	CellTable<Dto> table;
	@UiField
	SimplePager pager;
	@UiField
	Label sumLabel;
	@UiField
	Label sum;

	private ListDataProvider<Dto> provider;

	public ServiceTableView() {
		initWidget(uiBinder.createAndBindUi(this));

		sumLabel.setText(constants.sum());
		provider.addDataDisplay(table);
		pager.setDisplay(table);
	}

	@Override
	public ListDataProvider<Dto> getProvider() {
		return provider;
	}

	@UiFactory
	CellTable<Dto> makeTable() {
		return new CellTable<Dto>(provider = new ListDataProvider<Dto>());
	}

	@UiFactory
	SimplePager makePager() {
		return new SimplePager(TextLocation.CENTER);
	}

	@Override
	public void initColumns(final ModuleDto moduleDto, final View viewMode) {
		for (final String fieldName : moduleDto.getListFieldIds()) {
			final AbstractField<Object> field = moduleDto.getFieldById(fieldName);
			Column<Dto, Object> column = field.getColumn(fieldName, viewMode, new Callback<Dto>() {
				@Override
				public void callback(Dto arg) {
					arg.set("productCode", "sweeeet");
					arg.set("price", 23.0);
					
					provider.refresh();
				}
			});

			column.setFieldUpdater(new FieldUpdater<Dto, Object>() {
				@Override
				public void update(int index, Dto object, Object value) {
					getProvider().getList().get(index).set(fieldName, (Serializable) value);
					presenter.onItemUpdated(index, object, value);
				}
			});

			table.addColumn(column, field.getLabel());
		}
	}

	@Override
	public void updateOverallSum(double calculatedSum) {
		sum.setText(NumberFormat.getCurrencyFormat("EUR").format(calculatedSum));
	}

	@Override
	public void setValue(ServiceTablePresenter value) {
		this.presenter = value;
	}

	@Override
	public ServiceTablePresenter getValue() {
		return presenter;
	}
}
