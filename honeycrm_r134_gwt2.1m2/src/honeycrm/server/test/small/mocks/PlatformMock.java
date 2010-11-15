package honeycrm.server.test.small.mocks;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

import honeycrm.client.plugin.IPlatform;
import honeycrm.client.services.ReadServiceAsync;
import static org.easymock.EasyMock.*;

public class PlatformMock implements IPlatform {
	private final ReadServiceAsync readService;

	public PlatformMock() {
		this.readService = createNiceMock(ReadServiceAsync.class);
	}
	
	@Override
	public void scheduleRepeating(Command command, final int updateInterval) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					System.out.println("running command");
					try {
						Thread.sleep(updateInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	@Override
	public ReadServiceAsync getReadService() {
		return readService;
	}
	
	@Override
	public void attachToHeader(Widget w) {
		System.out.println("Attached to header");
	}
}
