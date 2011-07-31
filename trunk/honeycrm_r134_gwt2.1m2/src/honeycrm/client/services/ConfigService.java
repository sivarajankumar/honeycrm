package honeycrm.client.services;

import honeycrm.client.dto.Configuration;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("config")
public interface ConfigService extends RemoteService {
	public Configuration getConfiguration();
}
