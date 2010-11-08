package honeycrm.client.mvp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.presenters.RelationshipsPresenter;
import honeycrm.client.mvp.presenters.RelationshipsPresenter.Display;

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

	public RelationshipsView(final String module, final ArrayList<String> relationships) {
		this.module = module;
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
			views.put(relation, new RelationshipView(relation, module));
			panel.add(views.get(relation));
		}
	}
}
