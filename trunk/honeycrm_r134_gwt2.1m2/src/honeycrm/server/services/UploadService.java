package honeycrm.server.services;

import honeycrm.client.misc.PluginDescription;
import honeycrm.server.PluginStore;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class UploadService extends HttpServlet {
	private static final long serialVersionUID = -5910532688457597298L;
	protected static final DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	private static final Logger log = Logger.getLogger(UploadService.class.getSimpleName());
	private static final PluginStore store = new PluginStore();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iterator = upload.getItemIterator(req);

			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();

				if (item.isFormField()) {
					log.warning("Got a form field: " + item.getFieldName());
				} else {
					log.warning("Got an uploaded file: " + item.getFieldName() + ", name = " + item.getName());

					// System.out.println("got " + item.getContentType() + " " + item.getName());
					store.createPlugin(new PluginDescription(item.getName(), ""), item.openStream());
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}
