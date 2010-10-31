package honeycrm.client.mvp.views;

import java.io.Serializable;
import java.util.ArrayList;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.mvp.presenters.DetailPresenter;
import honeycrm.client.mvp.presenters.RelationshipPresenter;
import honeycrm.client.mvp.presenters.DetailPresenter.Display;
import honeycrm.client.view.AbstractView.View;
import honeycrm.client.view.relationship.RelationshipsContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DetailView extends Composite implements Display {
	private static DetailViewUiBinder uiBinder = GWT.create(DetailViewUiBinder.class);

	@UiTemplate("DetailView.ui.xml")
	interface DetailViewUiBinder extends UiBinder<Widget, DetailView> {
	}

	@UiField
	FlexTable table;
	@UiField
	RelationshipsView relationshipsView;
	@UiField
	Button createBtn;
	@UiField
	Button editBtn;
	@UiField
	Button saveBtn;
	@UiField
	Button cancelBtn;

	final String module;
	DetailPresenter presenter;
	ModuleDto moduleDto;
	Dto dto;

	public DetailView(final String module) {
		this.module = module;
		this.moduleDto = DtoModuleRegistry.instance().get(module);

		initWidget(uiBinder.createAndBindUi(this));
		
		createBtn.setText("Create");
		editBtn.setText("Edit");
		saveBtn.setText("Save");
		cancelBtn.setText("Cancel");
	}

	@UiFactory
	FlexTable makeTable() {
		return new FlexTable();
	}

	@UiFactory
	RelationshipsContainer makeRelationshipContainer() {
		return new RelationshipsContainer(module);
	}

	private void resetFields(final Dto newDto, final View view, final String focussedField) {
		this.dto = newDto;

		final String[][] fieldIds = moduleDto.getFormFieldIds();

		for (int y = 0; y < fieldIds.length; y++) {
			for (int x = 0; x < fieldIds[y].length; x++) {
				final String id = fieldIds[y][x];

				final int labelX = 2 * x + 0, labelY = y;
				final int valueX = 2 * x + 1, valueY = y;

				final Widget widgetLabel = getWidgetLabel(id, labelX, labelY);
				final Widget widgetValue = getWidgetByType(newDto, id, view);

				widgetLabel.setStyleName("detail_view_label");
				widgetValue.setStyleName("detail_view_value");

				addEvents(view, widgetLabel, widgetValue, id);
				addFocus(view, widgetValue, id, focussedField);

				if (view == View.DETAIL || (view != View.DETAIL && !Dto.isInternalReadOnlyField(id))) {
					// display the widget because we are in readonly mode or we
					// are not in ro mode but it is no internal field
					table.setWidget(labelY, labelX, widgetLabel);
					table.setWidget(valueY, valueX, widgetValue);
				}
			}
		}
	}

	protected Widget getWidgetByType(final Dto tmpDto, final String fieldId, final View view) {
		return moduleDto.getFieldById(fieldId).getWidget(view, tmpDto, fieldId);
	}

	private void addFocus(View view, Widget widgetValue, String id, String focussedField) {
		if (View.EDIT == view && null != focussedField && id.equals(focussedField) && widgetValue instanceof FocusWidget) {
			// TODO Cursor is still not put into the widget (e.g. text box) even with focus properly set.
			((FocusWidget) widgetValue).setFocus(true);
		}
	}

	private Widget getWidgetLabel(final String id, final int labelX, final int labelY) {
		final Widget widgetLabel;
		if (table.isCellPresent(labelY, labelX)) {
			if (table.getWidget(labelY, labelX) != null) {
				widgetLabel = table.getWidget(labelY, labelX);
			} else {
				widgetLabel = getLabelForField(id);
			}
		} else {
			widgetLabel = getLabelForField(id);
		}
		return widgetLabel;
	}

	protected Label getLabelForField(final String id) {
		return new Label(moduleDto.getFieldById(id).getLabel() + ":");
	}

	private void addEvents(final View view, final Widget widgetLabel, final Widget widgetValue, final String focussedField) {
		if (view == View.DETAIL) {
			if (widgetLabel instanceof Label) {
				((Label) widgetLabel).addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						// the label of this field has been clicked. we assume the user
						// wanted to express that he would like to start editing the entity
						// so we start editing of this entity for him
						// History.newItem(HistoryTokenFactory.get(moduleDto.getModule(), ModuleAction.EDIT, focussedField));
						startEdit();
					}
				});
			}
		} else {
			if (widgetValue instanceof TextBox) {
				((TextBox) widgetValue).addKeyDownHandler(new KeyDownHandler() {
					@Override
					public void onKeyDown(KeyDownEvent event) {
						if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
							if (null != presenter) {
								presenter.onSave();
							}
						}
					}
				});
			}
		}
	}

	@Override
	public void setPresenter(DetailPresenter modulePresenter) {
		this.presenter = modulePresenter;
	}

	@Override
	public void setData(Dto dto) {
		createBtn.setVisible(true);
		editBtn.setVisible(true);
		cancelBtn.setVisible(false);
		saveBtn.setVisible(false);

		resetFields(dto, View.DETAIL, null);
	}

	@Override
	public void startCreate() {
		createBtn.setVisible(false);
		editBtn.setVisible(false);
		cancelBtn.setVisible(true);
		saveBtn.setVisible(true);

		resetFields(moduleDto.createDto(), View.CREATE, null);
	}

	@Override
	public void startEdit() {
		createBtn.setVisible(false);
		editBtn.setVisible(false);
		cancelBtn.setVisible(true);
		saveBtn.setVisible(true);

		resetFields(dto, View.EDIT, null);
		relationshipsView.setVisible(false);
	}
	
	@Override
	public RelationshipPresenter.Display getRelationshipsView() {
		return relationshipsView;
	}

	@Override
	public HasClickHandlers getCreateBtn() {
		return createBtn;
	}
	
	@Override
	public HasClickHandlers getSaveBtn() {
		return saveBtn;
	}

	@Override
	public HasClickHandlers getEditBtn() {
		return editBtn;
	}

	@Override
	public HasClickHandlers getCancelBtn() {
		return cancelBtn;
	}

	@Override
	public Dto getData() {
		final String[][] fields = moduleDto.getFormFieldIds();
		final Dto newDto = moduleDto.createDto();

		for (int y = 0; y < fields.length; y++) {
			for (int x = 0; x < fields[y].length; x++) {
				final String field = fields[y][x];

				if (!Dto.isInternalReadOnlyField(field)) {
					// TODO this position y, 2*x+1 depends on the current layout of the form..
					final Widget widgetValue = table.getWidget(y, 2 * x + 1);
					final Serializable value = moduleDto.getFieldById(field).getData(widgetValue);
					newDto.set(field, value);
				}
			}
		}

		// Copy the id field to allow updates.
		newDto.setId(dto.getId());

		return newDto;
	}
	
	@UiFactory RelationshipsView makeRelationshipsView() {
		final ArrayList<String> list = DtoModuleRegistry.instance().getRelatedModules(moduleDto.getModule());
		java.util.Collections.sort(list);
		return new RelationshipsView(module, list);
	}
}
