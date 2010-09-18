package honeycrm.server.services;

import honeycrm.client.dto.Configuration;
import honeycrm.client.services.ConfigService;
import honeycrm.server.RelationshipFieldTable;
import honeycrm.server.transfer.DtoWizard;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ConfigServiceImpl extends RemoteServiceServlet implements ConfigService {
	private static final long serialVersionUID = 4097533640309440023L;

	@Override
	public Configuration getConfiguration() {
		return new Configuration(DtoWizard.instance.getDtoConfiguration(), RelationshipFieldTable.instance.getMap());
	}
}
