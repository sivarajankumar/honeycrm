package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.presenters.RelationshipsPresenter;
import honeycrm.client.mvp.presenters.RelationshipsPresenter.Display;
import honeycrm.client.services.ReadServiceAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RelationshipsView extends Composite implements Display {
	private static RelationshipsViewUiBinder uiBinder = GWT.create(RelationshipsViewUiBinder.class);

	interface RelationshipsViewUiBinder extends UiBinder<Widget, RelationshipsView> {
	}

	final HashMap<String, RelationshipView> views = new HashMap<String, RelationshipView>();
	final ArrayList<String> relationships;
	final String module;
	RelationshipsPresenter presenter;
	@UiField
	VerticalPanel panel;
	private final ReadServiceAsync readService;
	private LocalizedMessages constants;

	public RelationshipsView(final String module, final ArrayList<String> relationships, final ReadServiceAsync readService, final LocalizedMessages constants) {
		this.readService = readService;
		this.module = module;
		this.constants = constants;
		this.relationships = relationships;

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(RelationshipsPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void refresh(final Long relatedId) {
		for (final String relation : relationships) {
			setupView(relation);

			// TODO refresh with specified id!
			views.get(relation).setId(relatedId);
			views.get(relation).refresh();
		}
	}

	@Override
	public void makeVisible() {
		panel.setVisible(true);
	}

	@Override
	public void insertRefreshedData(Map<String, ListQueryResult> relationshipData, final long relatedId) {
		for (final String relation : relationships) {
			setupView(relation);

			if (relationshipData.containsKey(relation)) {
				views.get(relation).insertRefreshedData(relationshipData.get(relation), relatedId);
			}
		}
		if (null != presenter) {
			presenter.onViewsInitialized(views.values());
		}
	}

	private void setupView(final String relation) {
		if (views.isEmpty() || !views.containsKey(relation)) {
			views.put(relation, new RelationshipView(relation, module, readService, constants));
			panel.add(views.get(relation));
		}
	}
}
