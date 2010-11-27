package honeycrm.client.misc;

import honeycrm.client.dto.Dto;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class QuicksearchHelper {
	private static int counter = 0;
	private static final String SEARCH_SERVLET_URL = "find";

	private static native void setupQuicksearch(final String input, final String container, final Dto dto) /*-{
		$wnd.YAHOO.util.Event.onAvailable(container, function() {
			var oDS = new $wnd.YAHOO.util.XHRDataSource(@honeycrm.client.misc.QuicksearchHelper::SEARCH_SERVLET_URL);
			oDS.responseType = $wnd.YAHOO.util.XHRDataSource.TYPE_TEXT;
			oDS.responseSchema = {
			    recordDelim: "\n",
			    fieldDelim: "\t",
			    fields: ["name", "id"]
			};
			oDS.maxCacheEntries = 100;

			var oAC = new $wnd.YAHOO.widget.AutoComplete(input, container, oDS);
			var handler = function(sType, aArgs) {
				var myAC = aArgs[0]; // reference back to the AC instance
				var elLI = aArgs[1]; // reference to the selected LI element
				var oData = aArgs[2]; // object literal of selected item's result data
				var name = oData[0];
				var id = oData[1];
				
				@honeycrm.client.misc.QuicksearchHelper::updateDto(Lhoneycrm/client/dto/Dto;Ljava/lang/String;Ljava/lang/String;)(dto, "productID", id);
			};
			oAC.itemSelectEvent.subscribe(handler);
		});
	}-*/;

	public static SafeHtml getQuickSearchHTML(final Dto dto) {
		final String inputField = "i" + counter;
		final String container = "c" + counter;
		counter++;

		setupQuicksearch(inputField, container, dto);

		final SafeHtmlBuilder b = new SafeHtmlBuilder();
		b.appendHtmlConstant("<div style='width:15em;padding-bottom:2em;'>");
		b.appendHtmlConstant("<input id=\"" + inputField + "\" type=\"text\">");
		b.appendHtmlConstant("<div id=\"" + container + "\" />");
		b.appendHtmlConstant("</div>");

		return b.toSafeHtml();
	}
	
	private static void updateDto(final Dto dto, final String field, final String id) {
		dto.set(field, NumberParser.convertToLong(id));
	}
}
