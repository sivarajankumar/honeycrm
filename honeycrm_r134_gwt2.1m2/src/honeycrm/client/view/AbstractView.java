package honeycrm.client.view;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.prefetch.Prefetcher;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.DeleteServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;
import java.io.Serializable;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

abstract public class AbstractView extends Composite {
	// protected final CommonServiceAsync commonService = ServiceRegistry.commonService();
	protected static final CreateServiceAsync createService = ServiceRegistry.createService();
	protected static final ReadServiceAsync readService = ServiceRegistry.readService();
	protected static final UpdateServiceAsync updateService = ServiceRegistry.updateService();
	protected static final DeleteServiceAsync deleteService = ServiceRegistry.deleteService();
	protected final ModuleDto moduleDto;

	public AbstractView(final String moduleName) {
		this.moduleDto = DtoModuleRegistry.instance().get(moduleName);
	}

	protected String getTitleFromClazz() {
		return moduleDto.getTitle();
	}

	/**
	 * Initialize a dto instance from the widgets (e.g., textboxes, dateboxes, relate fields) in a table.
	 */
	protected Dto initializeDtoFromTable(final String[][] fieldIds, final FlexTable table) {
		final Dto tmpDto = moduleDto.createDto();
		
		for (int y = 0; y < fieldIds.length; y++) {
			for (int x = 0; x < fieldIds[y].length; x++) {
				final String id = fieldIds[y][x];

				if (!Dto.isInternalReadOnlyField(id)) {
					// TODO this position y, 2*x+1 depends on the current layout of the form..
					final Widget widgetValue = table.getWidget(y, 2 * x + 1);
					final Serializable value = moduleDto.getFieldById(id).getData(widgetValue);
					tmpDto.set(id, value);
				}
			}
		}
		
		return tmpDto;
	}

	protected void save(final FlexTable table, final long id) {
		final Dto tmpDto = initializeDtoFromTable(moduleDto.getFormFieldIds(), table);
		
		// id == -1 indicates that there is no id yet.
		// thus if id != -1 we know the id -> we do an update
		final boolean isUpdate = -1 != id && 0 != id;

		if (isUpdate) {
			tmpDto.setId(id);
		}

		if (isUpdate) {
			updateService.update(tmpDto, new AsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
					// mark cache invalid to make sure the changed values will be displayed
					Prefetcher.instance.invalidate(moduleDto.getModule(), id);

					// TODO re-implement
					// TabCenterView.instance().get(moduleDto.getModule()).saveCompleted();
				}

				@Override
				public void onFailure(Throwable caught) {
					displayError(caught);
				}
			});
		} else {
			createService.create(tmpDto, new AsyncCallback<Long>() {
				@Override
				public void onFailure(Throwable caught) {
					displayError(caught);
				}

				@Override
				public void onSuccess(Long result) {
					// TODO this won't work since we do not instantiate tabcenterview anymore.
					// TabCenterView.instance().get(moduleDto.getModule()).saveCompletedForId(result);
				}
			});
		}
	}

	protected Label getLabelForField(final String id) {
		return new Label(moduleDto.getFieldById(id).getLabel() + ":");
	}

	/**
	 * Returns the widget for displaying fieldId of tmpViewable for the view.
	 */
	protected Widget getWidgetByType(final Dto tmpDto, final String fieldId, final View view) {
		return moduleDto.getFieldById(fieldId).getWidget(view, tmpDto, fieldId);
	}

	/**
	 * Empties all input fields of the given {@link FlexTable}, e.g. after clearing a search widget.
	 */
	protected void emptyInputFields(final FlexTable table) {
		for (int y = 0; y < table.getRowCount(); y++) {
			for (int x = 0; x < table.getCellCount(y); x++) {
				final Widget w = table.getWidget(y, x);

				// clear the widget
				if (w instanceof TextBox) {
					((TextBox) w).setText("");
				} else if (w instanceof DateBox) {
					((DateBox) w).setValue(null);
				} else if (w instanceof RelateWidget) {
					((RelateWidget) w).setText("");
				}// else if (w instanceof )
			}
		}
	}

	protected void displayError(final Throwable caught) {
		Window.alert(caught.getClass().toString());
		// throw new RuntimeException(caught.getLocalizedMessage());
	}
	
	public enum View {
		DETAIL, EDIT, CREATE, LIST, LIST_HEADER
	}
}
