package honeycrm.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListViewAdapter;
import com.google.gwt.view.client.SingleSelectionModel;

public class Gae implements EntryPoint {
	public void onModuleLoad() {
		// RootLayoutPanel.get().add(new LoadingPanel());

		// create some data
		ArrayList<String[]> values = new ArrayList<String[]>();

		for (int i=0; i<100; i++) {
			values.add(new String[] { String.valueOf(i), String.valueOf(Random.nextInt()) });
		}
		
		// create a ListViewAdapter
		ListViewAdapter<String[]> lva = new ListViewAdapter<String[]>();
		// give the ListViewAdapter our data
		lva.setList(values);

		RootPanel.get().add(new HTML("<hr />"));

		{
			// CellTable
			CellTable<String[]> ct = new CellTable<String[]>();
			ct.setSelectionEnabled(true);
			ct.setSelectionModel(new SingleSelectionModel());
			ct.setPageSize(15);
			lva.addView(ct);
			ct.addColumn(new TextColumn<String[]>() {
				@Override
				public String getValue(String[] object) {
					return object[0];
				}
			}, "First");

			ct.addColumn(new TextColumn<String[]>() {

				@Override
				public String getValue(String[] object) {
					return "" + object[1] + "";
				}
			}, "Second");

			// create a pager, giving it a handle to the CellTable
			SimplePager<String[]> pager = new SimplePager<String[]>(ct, SimplePager.TextLocation.CENTER);

			// add the Pager to the page
			RootPanel.get().add(pager);

			// add the CellList to the page
			RootPanel.get().add(ct);
		}
	}
}
