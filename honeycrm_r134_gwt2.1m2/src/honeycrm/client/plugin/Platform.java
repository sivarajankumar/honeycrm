package honeycrm.client.plugin;

import java.io.Serializable;

import honeycrm.client.mvp.presenters.HeaderPresenter.Display;
import honeycrm.client.services.ReadServiceAsync;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class Platform implements IPlatform, Serializable {
	private static final long serialVersionUID = 8984048542396863556L;
	// private Display header;
	// private ReadServiceAsync readService;

	public Platform() {
	}
	
	public Platform(final Display header, final ReadServiceAsync readService) {
//		this.header = header;
//		this.readService = readService;
	}

	@Override
	public ReadServiceAsync getReadService() {
//		return readService;
		return null;
	}

	@Override
	public void attachToHeader(Widget w) {
	//	header.attachPluginWidget(w);
	}

	@Override
	public void scheduleRepeating(final Command command, final int updateInterval) {
		new Timer() {
			@Override
			public void run() {
				command.execute();
			}
		}.scheduleRepeating(updateInterval);
	}

}
