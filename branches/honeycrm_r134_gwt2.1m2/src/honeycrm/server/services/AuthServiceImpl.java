package honeycrm.server.services;

import honeycrm.client.dto.Dto;
import honeycrm.client.services.AuthService;
import honeycrm.server.CommonServiceReader;

import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AuthServiceImpl extends RemoteServiceServlet implements AuthService {
	private static final long serialVersionUID = -4517684380570643032L;
	private static final CommonServiceReader reader = new CommonServiceReader();
	private static final Logger logger = Logger.getLogger(AuthServiceImpl.class.getSimpleName());

	@Override
	public Long login(final String login, final String password) {
		if (login.isEmpty()) {
			return fail(login);
		} else if (login.equals("42")) { // allow creating employees in the first place using the "42" login 
			return 42L;
		} else {
			// TODO use real auth framework to check credentials. for now only search an employee with the specified login name.
			final Dto userDto = reader.getByName("employee", login);

			if (null == userDto) {
				return fail(login);
			} else {
				return userDto.getId();
			}
		}
	}

	private Long fail(final String login) {
		logger.info("Login failed for employee: '" + login + "'");
		return null;
	}
}
