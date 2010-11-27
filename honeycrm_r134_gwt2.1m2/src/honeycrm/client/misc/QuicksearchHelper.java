package honeycrm.client.misc;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class QuicksearchHelper {
	private static int counter = 0;
	private static final String SEARCH_SERVLET_URL = "find";

	private static native void setupQuicksearch(final String inputId, final String containerId) /*-{
		$wnd.YAHOO.util.Event.onAvailable(containerId, function() {
			var oDS = new $wnd.YAHOO.util.XHRDataSource(@honeycrm.client.misc.QuicksearchHelper::SEARCH_SERVLET_URL);
			oDS.responseType = $wnd.YAHOO.util.XHRDataSource.TYPE_TEXT;
			oDS.responseSchema = {
			    recordDelim: "\n",
			    fieldDelim: "\t"
			};
			oDS.maxCacheEntries = 5;
	
			var oAC = new $wnd.YAHOO.widget.AutoComplete(inputId, containerId, oDS);
		});
	}-*/;

	public static SafeHtml getQuickSearchHTML() {
		final String inputFieldName = "i" + counter;
		final String containerName = "c" + counter;
		counter++;

		setupQuicksearch(inputFieldName, containerName);

		final SafeHtmlBuilder b = new SafeHtmlBuilder();
		b.appendHtmlConstant("<div style='width:15em;padding-bottom:2em;'>");
		b.appendHtmlConstant("<input id=\"" + inputFieldName + "\" type=\"text\">");
		b.appendHtmlConstant("<div id=\"" + containerName + "\" />");
		b.appendHtmlConstant("</div>");

		return b.toSafeHtml();
	}
}
