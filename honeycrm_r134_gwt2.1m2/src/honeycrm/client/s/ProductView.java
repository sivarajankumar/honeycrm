package honeycrm.client.s;

import honeycrm.client.dto.Dto;
import honeycrm.client.s.ProductPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ProductView extends ModuleView implements Display {
	private static ProductViewUiBinder uiBinder = GWT.create(ProductViewUiBinder.class);

	interface ProductViewUiBinder extends UiBinder<Widget, ProductView> {
	}

	@UiField
	Label nameLbl;
	@UiField
	Label nameDetail;
	@UiField
	TextBox nameEdit;

	public ProductView(GenericDataProvider provider) {
		super(Module.Product, provider);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void init(Void arg) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				pager.setDisplay(list);
				list.setSelectionModel(selectionModel);
				list.setPageSize(20);
				pager.firstPage();

				final TextColumn<Dto> nameCol = new TextColumn<Dto>() {
					@Override
					public String getValue(Dto object) {
						return String.valueOf(object.get("name"));
					}
				};
				nameCol.setSortable(true);

				list.addColumn(nameCol, constants.contactsName());

				list.addColumnSortHandler(new AsyncHandler(list));
				provider.addDataDisplay(list);
				provider.refresh(list, list.getColumnSortList());

				createBtn.setText(constants.create());
				deleteBtn.setText(constants.delete());
				editBtn.setText(constants.edit());
				saveBtn.setText(constants.save());

				nameLbl.setText(constants.contactsName());
			}

			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}

	@Override
	protected String[] getFieldNames() {
		return new String[] { "name" };
	}
}
