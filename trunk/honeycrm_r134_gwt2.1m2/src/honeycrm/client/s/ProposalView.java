package honeycrm.client.s;

import honeycrm.client.dto.Dto;
import honeycrm.client.s.ModulePresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class ProposalView extends ModuleView implements Display {

	private static ProposalViewUiBinder uiBinder = GWT.create(ProposalViewUiBinder.class);

	interface ProposalViewUiBinder extends UiBinder<Widget, ProposalView> {
	}

	@UiField
	Label nameLbl;
	@UiField
	Label dateLbl;
	@UiField
	Label contactLbl;
	@UiField
	Label productsLbl;
	@UiField
	Label nameDetail;
	@UiField
	Label dateDetail;
	@UiField
	Label contactDetail;
	@UiField
	Label productsDetail;
	@UiField
	TextBox nameEdit;
	@UiField
	DatePicker dateEdit;
	@UiField
	SuggestBox contactEdit;

	public ProposalView(GenericDataProvider provider) {
		super(Module.Proposal, provider);
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
				final TextColumn<Dto> dateCol = new TextColumn<Dto>() {
					@Override
					public String getValue(Dto object) {
						return String.valueOf(object.get("date"));
					}
				};
				final TextColumn<Dto> contactCol = new TextColumn<Dto>() {
					@Override
					public String getValue(Dto object) {
						return String.valueOf(object.get("contact"));
					}
				};
				nameCol.setSortable(true);

				list.addColumn(nameCol, constants.proposalsName());
				list.addColumn(dateCol, constants.proposalsDate());
				list.addColumn(contactCol, constants.proposalsContact());

				list.addColumnSortHandler(new AsyncHandler(list));
				provider.addDataDisplay(list);
				provider.refresh(list, list.getColumnSortList());

				createBtn.setText(constants.create());
				deleteBtn.setText(constants.delete());
				editBtn.setText(constants.edit());
				saveBtn.setText(constants.save());

				nameLbl.setText(constants.contactsName());
				dateLbl.setText(constants.proposalsDate());
				contactLbl.setText(constants.proposalsContact());
				productsLbl.setText(constants.proposalsProducts());
			}

			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}

	@Override
	public HasKeyPressHandlers[] getAllFields() {
		return new HasKeyPressHandlers[] { nameEdit, contactEdit };
	}

	@Override
	public Dto getDto() {
		final Dto d = new Dto(Module.Proposal.toString());
		d.set("name", nameEdit.getText());
		d.set("date", dateEdit.getValue());
		d.set("contact", contactEdit.getText());
		// TODO serialize and store products
		return d;
	}

	@Override
	public void openView(Dto selectedObject) {
		nameDetail.setText(String.valueOf(selectedObject.get("name")));
		dateDetail.setText(String.valueOf(selectedObject.get("date")));
		contactLbl.setText(String.valueOf(selectedObject.get("contact")));
		// TODO insert products table data
	}

	@Override
	protected String[] getFieldNames() {
		return new String[] { "name", "date", "contact", "products" };
	}

	@Override
	protected Label[] getDetailViewFields() {
		return new Label[] { nameLbl, dateLbl, contactLbl, productsLbl };
	}

	@Override
	protected UIObject[] getEditViewFields() {
		return new UIObject[]{nameEdit, dateEdit, contactEdit };
	}
}
