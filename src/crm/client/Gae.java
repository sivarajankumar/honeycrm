package crm.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Gae implements EntryPoint {
	public void onModuleLoad() {
		// RootLayoutPanel.get().add(new RichTextArea());
		RootLayoutPanel.get().add(new TabLayout());
	}
}