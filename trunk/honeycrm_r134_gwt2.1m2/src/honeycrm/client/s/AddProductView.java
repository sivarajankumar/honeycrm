package honeycrm.client.s;

import honeycrm.client.dto.Dto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddProductView extends LocalizedView implements Initializable<Void> {

	private static AddProductViewUiBinder uiBinder = GWT.create(AddProductViewUiBinder.class);

	interface AddProductViewUiBinder extends UiBinder<Widget, AddProductView> {
	}
	
	@UiField
	Button cancel;
	@UiField
	Button finish;
	@UiField
	TextBox search;
	@UiField
	CellTable<Dto> table;
	@UiField
	Label selectedProductsLbl;
	@UiField
	TextArea selectedProducts;
	
	public AddProductView() {
		initWidget(uiBinder.createAndBindUi(this));

		this.addAttachHandler(new AttachEvent.Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				init(null);
			}
		});
	}
	
	@UiHandler("cancel")
	void handleClick(ClickEvent e) {
		// panel.hide();
	}
	
	@UiFactory
	CellTable<Dto> makeTable() {
		return new CellTable<Dto>(ModuleView.keyProvider);
	}

	@Override
	public void init(Void arg) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				finish.setText(constants.finish());
				cancel.setText(constants.cancel());
				selectedProductsLbl.setText(constants.proposalsSelectedProducts());
			
				final TextColumn<Dto> nameCol = new TextColumn<Dto>() {
					@Override
					public String getValue(Dto object) {
						return String.valueOf(object.get("name"));
					}
				};
				nameCol.setSortable(true);

				table.addColumn(nameCol, constants.productsName());
				
				// panel.center();
			}
			
			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}
}
