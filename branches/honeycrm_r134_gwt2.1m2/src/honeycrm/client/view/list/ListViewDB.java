package honeycrm.client.view.list;

import honeycrm.client.dto.Dto;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;

public class ListViewDB {
	private final ListViewDataProvider dataProvider;

	public ListViewDB(final ListViewDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
	
	public void addDataDisplay(final HasData<Dto> display) {
		dataProvider.addDataDisplay(display);
	}
	
	public void refresh() {
		for (final HasData<Dto> display: dataProvider.getDataDisplays()) {
			dataProvider.refresh(display);
		}
	}
	
	public static final ProvidesKey<Dto> KEY_PROVIDER = new ProvidesKey<Dto>() {
		@Override
		public Object getKey(Dto item) {
			return null == item ? null : item.getId();
		}
	};
}
