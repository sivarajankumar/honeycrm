package honeycrm.client.s;

import honeycrm.client.s.ProductPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ProductView extends ModuleView implements Display {

	private static ProductViewUiBinder uiBinder = GWT.create(ProductViewUiBinder.class);

	interface ProductViewUiBinder extends UiBinder<Widget, ProductView> {
	}

	@UiField Label nameLbl;
	@UiField Label nameDetail;
	@UiField TextBox nameEdit;
	
	public ProductView() {
		super(Module.Product);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void init(Void arg) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				nameLbl.setText(constants.contactsName());
			}
			
			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}

	@Override
	protected String[] getFieldNames() {
		return new String[]{"name"};
	}

	@Override
	public void refresh() {
		
	}
}
