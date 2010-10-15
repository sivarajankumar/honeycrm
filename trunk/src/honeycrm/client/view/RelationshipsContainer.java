package honeycrm.client.view;

import honeycrm.client.CommonServiceAsync;
import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.TabCenterView;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.prefetch.Consumer;
import honeycrm.client.prefetch.Prefetcher;
import honeycrm.client.prefetch.ServerCallback;
import honeycrm.client.view.AbstractView.View;

import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RelationshipsContainer extends AbstractView {
	private Panel panel = new VerticalPanel();

	public RelationshipsContainer(final String relatedDtoClass) {
		super(relatedDtoClass);
		initWidget(panel);
	}

	public void refresh(final Long relatedId) {
		clear();

		for (final ModuleDto originating : DtoModuleRegistry.instance().getDtos()) {
			if (SingleRelationshipPanel.doesRelationshipExist(originating.getModule(), moduleDto.getModule())) {
				panel.add(new SingleRelationshipPanel(originating, relatedId, moduleDto.getModule()));
			}
		}
	}

	/**
	 * Visually throw away all panels.
	 */
	public void clear() {
		panel.clear();
	}
}

// TODO instantiate and reuse SearchableListView instead. only problem left to solve is: how to
// determine the name of the id field in the original dto that stores the id of the related dto.
// e.g. if we want to display all contacts for account 23. how to we know in client side code that
// we have to search for all contacts with accountId = 23? currently this is only known on server
// side since there the RelatesTo annotation is read using reflection
class SingleRelationshipPanel extends Composite {
	private static final CommonServiceAsync commonService = ServiceRegistry.commonService();
	private final ModuleDto originatingDtoClass;
	private final String relatedDtoClass;
	private final Long id;
	private FlexTable table = new FlexTable();

	public SingleRelationshipPanel(final ModuleDto originatingDto, final Long id, final String relatedDto) {
		this.originatingDtoClass = originatingDto;
		this.relatedDtoClass = relatedDto;
		this.id = id;

		final HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.add(getTitleLabel());

		/*
		 * only add the create button if this relationship has is represented by a single id field
		 * e.g. no create button should be displayed in the contact <-> contact relationship because
		 * there are three different fields in each contact referencing other contacts. just
		 * clicking the create button it is not clear which of the fields should be pre-filled.
		 */
		if (hasRelationshipUniqueFieldName()) {
			hpanel.add(getCreateBtn());
		}

		final VerticalPanel vpanel = new VerticalPanel();
		vpanel.add(hpanel);
		vpanel.add(table);

		refresh();

		initWidget(vpanel);
	}

	public static boolean doesRelationshipExist(final String originatingDomain, final String relatedDomain) {
		final Map<String, Map<String, Set<String>>> r = DtoModuleRegistry.instance().getRelationships();
		return r.containsKey(originatingDomain) && r.get(originatingDomain).containsKey(relatedDomain);
	}

	private boolean hasRelationshipUniqueFieldName() {
		final Map<String, Map<String, Set<String>>> r = DtoModuleRegistry.instance().getRelationships();
		// assume the relationship exists.
		return 1 == r.get(originatingDtoClass.getModule()).get(relatedDtoClass).size();
	}

	private Widget getCreateBtn() {
		final Button btn = new Button("Create");
		btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final Map<String, Map<String, Set<String>>> relationships = DtoModuleRegistry.instance().getRelationships();

				/**
				 * e.g. contactId
				 */
				final String fieldId = relationships.get(originatingDtoClass.getModule()).get(relatedDtoClass).iterator().next();

				TabCenterView.instance().showCreateViewForModulePrefilled(originatingDtoClass.getModule(), fieldId, id);
			}
		});
		return btn;
	}

	public void refresh() {
		Prefetcher.instance.get(new Consumer<ListQueryResult>() {
			@Override
			public void setValueAsynch(ListQueryResult value) {
				insertRelatedDtos(value);
			}
		}, new ServerCallback<ListQueryResult>() {
			@Override
			public void doRpc(final Consumer<ListQueryResult> internalCacheCallback) {
				LoadIndicator.get().startLoading();

				commonService.getAllRelated(originatingDtoClass.getModule(), id, relatedDtoClass, new AsyncCallback<ListQueryResult>() {
					@Override
					public void onSuccess(ListQueryResult result) {
						LoadIndicator.get().endLoading();
						internalCacheCallback.setValueAsynch(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						LoadIndicator.get().endLoading();
						Window.alert(caught.getLocalizedMessage());
					}
				});
			}
		}, 60 * 1000, originatingDtoClass.getModule(), id, relatedDtoClass);
	}

	private void insertRelatedDtos(ListQueryResult result) {
		final int headerRows = 1;
		final int leadingCols = 1;

		for (int col = 0; col < originatingDtoClass.getListFieldIds().length; col++) {
			final String id = originatingDtoClass.getListFieldIds()[col];

			if (!isDuplicateRelateField(id)) {
				table.setWidget(0, leadingCols + col, new Label(originatingDtoClass.getFieldById(id).getLabel()));
			}
		}

		for (int row = 0; row < result.getResults().length; row++) {
			final Dto originatingDto = result.getResults()[row];

			table.setWidget(headerRows + row, 0, new Hyperlink("open", originatingDto.getHistoryToken() + " " + originatingDto.getId()));

			for (int col = 0; col < originatingDtoClass.getListFieldIds().length; col++) {
				final String id = originatingDtoClass.getListFieldIds()[col];

				if (!isDuplicateRelateField(id)) {
					final Widget w = originatingDto.getFieldById(id).getWidget(View.DETAIL, originatingDto.get(id));
					table.setWidget(headerRows + row, leadingCols + col, w);
				}
			}
		}
	}

	// Returns true if the field with the given id is a relate field representing the original dto
	// class itself.
	// Assume we want to display all offerings for a contact. Therefore we want to display an
	// offerings list containing
	// a column displaying the contact. We want to identify and skip the contact column since we
	// already know that this
	// offering is linked to the contact.
	private boolean isDuplicateRelateField(final String id) {
		return originatingDtoClass.getFieldById(id) instanceof FieldRelate && ((FieldRelate) originatingDtoClass.getFieldById(id)).getRelatedClazz().equals(relatedDtoClass);
	}

	private Label getTitleLabel() {
		final Label title = new Label(originatingDtoClass.getTitle() + "s");
		// TODO add style for this in custom css file
		title.setStyleName("relationship_title");
		return title;
	}
}