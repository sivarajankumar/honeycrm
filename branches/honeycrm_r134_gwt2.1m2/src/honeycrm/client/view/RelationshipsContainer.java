package honeycrm.client.view;

import honeycrm.client.CommonServiceAsync;
import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.TabCenterView;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.prefetch.Consumer;
import honeycrm.client.prefetch.Prefetcher;
import honeycrm.client.prefetch.ServerCallback;

import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
class SingleRelationshipPanel extends ListView {
	private static final CommonServiceAsync commonService = ServiceRegistry.commonService();
	private final ModuleDto originatingDtoClass;
	private final String relatedDtoClass;
	private final Long id;

	public SingleRelationshipPanel(final ModuleDto originatingDto, final Long id, final String relatedDto) {
		super(originatingDto.getModule());
		
		setShowTitle(true);
		this.originatingDtoClass = originatingDto;
		this.relatedDtoClass = relatedDto;
		this.id = id;
		
		/*
		 * only add the create button if this relationship has is represented by a single id field
		 * e.g. no create button should be displayed in the contact <-> contact relationship because
		 * there are three different fields in each contact referencing other contacts. just
		 * clicking the create button it is not clear which of the fields should be pre-filled.
		 */
		if (hasRelationshipUniqueFieldName()) {
			setAdditionalButtons(getCreateBtn());
		}

		refresh();
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

	private Button getCreateBtn() {
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

	@Override
	public void refresh() {
		Prefetcher.instance.get(new Consumer<ListQueryResult>() {
			@Override
			public void setValueAsynch(ListQueryResult value) {
				refreshListViewValues(value);
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
}