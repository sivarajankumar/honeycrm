package honeycrm.client.view.relationship;

import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.view.list.ListViewDataProvider;

public class RelationshipListViewDataProvider extends ListViewDataProvider {
	final long originatingId;
	final String originatingModule;
	
	public RelationshipListViewDataProvider(final String relationshipModule, final String originating, final long relatedId) {
		super(relationshipModule);
		this.originatingId = relatedId;
		this.originatingModule = originating;
	}

	@Override
	public void refresh(final HasData<Dto> display) {
		// TODO do only call getRelated(originating, originalModule, relationshipModule)
		// TODO this has to be extremely fast
		ServiceRegistry.commonService().getAllRelated(originatingId, originatingModule, new AsyncCallback<Map<String, ListQueryResult>>() {
			@Override
			public void onSuccess(Map<String, ListQueryResult> result) {
				insertRefreshedData(display, result.get(module)); 
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("could not load");
			}
		});
	}
}
