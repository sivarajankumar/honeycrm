package honeycrm.client.view.relationship;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.view.list.ListViewDataProvider;

public class RelationshipListViewDataProvider extends ListViewDataProvider {
	long originatingId;
	final String originatingModule;

	public RelationshipListViewDataProvider(final String relationshipModule, final String originating, final ReadServiceAsync readService) {
		super(relationshipModule, readService);
		this.originatingModule = originating;
	}

	public void setOriginatingId(long originatingId) {
		this.originatingId = originatingId;
	}

	@Override
	public void refresh(final HasData<Dto> display) {
		// TODO do only call getRelated(originating, originalModule, relationshipModule)
		// TODO this has to be extremely fast
		if (originatingId > 0) {
			if (lastRefreshTooYoung()) {
				return;
			}
			
			lastRefresh = System.currentTimeMillis();
			
			readService.getAllRelated(originatingModule, originatingId, module, new AsyncCallback<ListQueryResult>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("could not load");
				}

				@Override
				public void onSuccess(ListQueryResult result) {
					insertRefreshedData(display, result);
				}
			});
		} else {
			// ignore this since id is invalid.
		}
	}
}
