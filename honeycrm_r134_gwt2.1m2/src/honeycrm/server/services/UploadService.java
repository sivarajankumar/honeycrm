package honeycrm.server.services;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class UploadService extends UploadAction {
	private static final long serialVersionUID = -5910532688457597298L;
	protected static final DatastoreService db = DatastoreServiceFactory.getDatastoreService();

	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
	    String response = "";
	    int cont = 0;
		
		for (final FileItem item : sessionFiles) {
			if (item.isFormField()) {
				continue;
			}
			
			final Entity newPlugin = new Entity("Plugin");

			newPlugin.setProperty("name", "foo");
			newPlugin.setProperty("bytecode", item.get());

			db.put(newPlugin);
			
			/// Compose a xml message with the full file information
	        response += "<file-" + cont + "-field>" + item.getFieldName() + "</file-" + cont + "-field>\n";
	        response += "<file-" + cont + "-name>" + item.getName() + "</file-" + cont + "-name>\n";
	        response += "<file-" + cont + "-size>" + item.getSize() + "</file-" + cont + "-size>\n";
	        response += "<file-" + cont + "-type>" + item.getContentType() + "</file-" + cont + "type>\n";
		}
		
		System.out.println(response);
		
		removeSessionFileItems(request);
		
		return "<response>\n" + response + "</response>\n";
	}

	@Override
	public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// super.getUploadedFile(request, response);
	}

	@Override
	public void removeItem(HttpServletRequest request, FileItem item) throws UploadActionException {
		// TODO Auto-generated method stub
		// super.removeItem(request, item);
	}

}
