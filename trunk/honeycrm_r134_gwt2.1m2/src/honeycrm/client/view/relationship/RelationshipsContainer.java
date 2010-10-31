package honeycrm.client.view.relationship;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.view.AbstractView;
import honeycrm.client.view.ModuleAction;
import honeycrm.client.view.list.ListView;
import honeycrm.client.view.list.ListViewDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RelationshipsContainer extends AbstractView {
	private final Panel panel = new VerticalPanel();
	/**
	 * Map storing the single relationship panel instances. instead of recreating them on every refresh we will reuse the instances and just call updateList on them.
	 */
	private final Map<String, SingleRelationshipPanel> map = new HashMap<String, SingleRelationshipPanel>();
	private final ArrayList<String> originatingNames;

	public RelationshipsContainer(final String relatedDtoClass) {
		super(relatedDtoClass);
		initWidget(panel);

		originatingNames = DtoModuleRegistry.instance().getRelatedModules(moduleDto.getModule());
		Collections.sort(originatingNames);
	}

	public void refresh(final Long relatedId) {
		for (final String originating : originatingNames) {
			if (map.isEmpty() || !map.containsKey(originating)) {
				// insert a new relationship panel for this dto module into the map
				final SingleRelationshipPanel relPanel = new SingleRelationshipPanel(originating, relatedId, moduleDto.getModule());
				map.put(originating, relPanel);

				// attach the new panel
				panel.add(relPanel);
			} else {
				map.get(originating).refresh();
			}
		}
	}
}

// TODO instantiate and reuse SearchableListView instead. only problem left to solve is: how to
// determine the name of the id field in the original dto that stores the id of the related dto.
// e.g. if we want to display all contacts for account 23. how to we know in client side code that
// we have to search for all contacts with accountId = 23? currently this is only known on server
// side since there the RelatesTo annotation is read using reflection
class SingleRelationshipPanel extends ListView {
	private final String relatedDtoClass;
	private final Long id;

	public SingleRelationshipPanel(final String originatingDto, final Long id, final String relatedDto) {
		super(originatingDto);

		setDisclose(true);
		setShowTitle(true);
		setPageSize(5);

		this.relatedDtoClass = relatedDto;
		this.id = id;

		/*
		 * only add the create button if this relationship has is represented by a single id field e.g. no create button should be displayed in the contact <-> contact relationship because there are three different fields in each contact referencing other contacts. just clicking the create button it is not clear which of the fields should be pre-filled.
		 */
		if (hasRelationshipUniqueFieldName()) {
			setAdditionalButtons(getCreateBtn());
		}

		refresh();
	}

/*	public static boolean doesRelationshipExist(final String originatingDomain, final String relatedDomain) {
		final HashMap<String, HashMap<String, HashSet<String>>> r = DtoModuleRegistry.instance().getRelationships();
		return r.containsKey(originatingDomain) && r.get(originatingDomain).containsKey(relatedDomain);
	}*/

	private boolean hasRelationshipUniqueFieldName() {
		final HashMap<String, HashMap<String, HashSet<String>>> r = DtoModuleRegistry.instance().getRelationships();
		// assume the relationship exists.
		return 1 == r.get(moduleDto.getModule()).get(relatedDtoClass).size();
	}

	@Override
	protected ListViewDataProvider getListDataProvider() {
		return new RelationshipListViewDataProvider(moduleDto.getModule(), relatedDtoClass);
	}

	private Button getCreateBtn() {
		final Button btn = new Button("Create");
		btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				final HashMap<String, HashMap<String, HashSet<String>>> relationships = DtoModuleRegistry.instance().getRelationships();

				/**
				 * name of the field that should be pre-filled e.g. contactId
				 */
				final String field = relationships.get(moduleDto.getModule()).get(relatedDtoClass).iterator().next();
				History.newItem(HistoryTokenFactory.get(moduleDto.getModule(), ModuleAction.CREATE, field, id));
			}
		});
		return btn;
	}
}