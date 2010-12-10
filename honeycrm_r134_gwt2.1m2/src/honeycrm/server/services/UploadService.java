package honeycrm.server.services;

import honeycrm.client.misc.PluginDescription;
import honeycrm.server.test.small.dyn.PluginStore;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class UploadService extends HttpServlet {
	private static final long serialVersionUID = -5910532688457597298L;
	protected static final DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	private static final PluginStore store = new PluginStore();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());

		try {
			final List<FileItem> items = uploader.parseRequest(req);
			for (final FileItem item : items) {
				// System.out.println("got " + item.getContentType() + " " + item.getName());
				store.createPlugin(new PluginDescription(item.getName(), ""), item.getInputStream());
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
}
