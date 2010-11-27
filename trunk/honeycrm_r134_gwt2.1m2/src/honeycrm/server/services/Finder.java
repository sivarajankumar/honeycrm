package honeycrm.server.services;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.QuicksearchHelper;
import honeycrm.server.domain.Product;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Finder extends HttpServlet {
	private static final long serialVersionUID = -6395236770479847774L;
	private final ReadServiceImpl readService = new ReadServiceImpl();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final String query = req.getParameter("query");
		final ServletOutputStream out = resp.getOutputStream();

		for (final Dto d : readService.getAllByNamePrefix(Product.class.getSimpleName(), query, 0, 20).getResults()) {
			for (final String field : new String[] { "name", "id", "productCode", "price" }) {
				out.print(String.valueOf(d.get(field)) + QuicksearchHelper.FIELD_DELIMITER);
			}

			out.print(QuicksearchHelper.RECORD_DELIMITER);
		}

		out.close();
	}
}
