package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.field.AbstractField;
import honeycrm.client.misc.Callback;
import honeycrm.client.misc.NumberParser;
import honeycrm.client.misc.QuicksearchValue;
import honeycrm.client.misc.View;
import honeycrm.client.mvp.presenters.ServiceTablePresenter;
import honeycrm.client.mvp.presenters.ServiceTablePresenter.Display;

import java.io.Serializable;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
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
	Button add;
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

		add.setText(constants.add());
		sumLabel.setText(constants.sum());
		provider.addDataDisplay(table);
		pager.setDisplay(table);
	}

	@Override
	public HasClickHandlers getAdd() {
		return add;
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
			final AbstractField<Object, Object> field = moduleDto.getFieldById(fieldName);
			Column<Dto, Object> column = field.getColumn(fieldName, viewMode, new Callback<Object>() {
				@Override
				public void callback(final Object arg) {
					// TODO do as much as possible of this code in the presenter to make sure this can be tested.
					if (arg instanceof QuicksearchValue) {
						QuicksearchValue q = (QuicksearchValue) arg;

						JsArrayString o = q.getReturnValue().cast();

						final Long id = NumberParser.convertToLong(o.get(1));
						final String name = o.get(0);
						final String productCode = o.get(2);
						final String price = o.get(3);

						q.getDto().set("productID", id);
						q.getDto().set("productCode", productCode);
						q.getDto().set("price", price);

						if (null == q.getDto().get("productID_resolved")) {
							q.getDto().set("productID_resolved", new Dto("Product"));
						}
						final Dto resolved = (Dto) q.getDto().get("productID_resolved");
						resolved.setId(id);
						resolved.set("name", name);

						provider.refresh();
					}
				}
			});

			column.setFieldUpdater(new FieldUpdater<Dto, Object>() {
				@Override
				public void update(int index, Dto object, Object value) {
					getProvider().getList().get(index).set(fieldName, (Serializable) value);
					presenter.onItemUpdated(index, object);
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

	@Override
	public void hideAddButton() {
		add.setVisible(false);
	}

	@Override
	public void add(Dto newService) {
		provider.getList().add(newService);
	}
}
