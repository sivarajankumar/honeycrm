package honeycrm.server.services;

import honeycrm.client.dto.Configuration;
import honeycrm.client.services.ConfigService;
import honeycrm.client.services.NewDtoWizard;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ConfigServiceImpl extends RemoteServiceServlet implements ConfigService {
	private static final long serialVersionUID = 4097533640309440023L;

	@Override
	public Configuration getConfiguration() {
		// final Configuration c = new Configuration(DtoWizard.instance.getDtoConfiguration(), RelationshipFieldTable.instance.getMap());

		return NewDtoWizard.getConfiguration();
	}
}
