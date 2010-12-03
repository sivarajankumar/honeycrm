package honeycrm.client.plugin;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

import honeycrm.client.misc.PlatformProvider;
import honeycrm.client.pluginviews.ProgressPluginView;

public class ProgressPluginPresenter extends AbstractPlugin {
	private static final long serialVersionUID = 5074341676146266299L;

	public interface Display {
		void display(String progressSymbol);
		Widget asWidget();
	}

	@Override
	protected void internalRunPlugin() {
		final Display view = new ProgressPluginView();
		final IPlatform platform = PlatformProvider.platform();

		platform.scheduleRepeating(new Command() {
			int step = 0;

			@Override
			public void execute() {
				view.display(getProgressSymbol(step));
				step = (step + 1) % 8;
			}
		}, 200);
		
		platform.attachToHeader(view.asWidget());
	}

	public String getProgressSymbol(final int step) {
		switch (step) {
		case 0:
		case 4:
			return "|";
		case 1:
		case 5:
			return "/";
		case 2:
		case 6:
			return "-";
		case 3:
		case 7:
			return "\\";
		default:
			return "__";
		}
	}
}
