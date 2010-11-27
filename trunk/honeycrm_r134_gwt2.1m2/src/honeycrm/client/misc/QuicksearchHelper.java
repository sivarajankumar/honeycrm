package honeycrm.client.misc;

import java.util.HashMap;

import honeycrm.client.dto.Dto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class QuicksearchHelper {
	private static int counter = 0;
	private static HashMap<Integer, Callback<Dto>> callbacks = new HashMap<Integer, Callback<Dto>>();
	private static final String SEARCH_SERVLET_URL = "find";
	private static final Integer CACHE_ENTRIES = 100;

	private static native void setupQuicksearch(final String input, final String container, final int currentCounter, final Dto dto, final String relateFieldName) /*-{
		$wnd.YAHOO.util.Event.onAvailable(container, function() {
			var oDS = new $wnd.YAHOO.util.XHRDataSource(@honeycrm.client.misc.QuicksearchHelper::SEARCH_SERVLET_URL);
			oDS.responseType = $wnd.YAHOO.util.XHRDataSource.TYPE_TEXT;
			oDS.responseSchema = {
			    recordDelim: "\n",
			    fieldDelim: "\t",
			    fields: ["name", "id"]
			};
			oDS.maxCacheEntries = @honeycrm.client.misc.QuicksearchHelper::CACHE_ENTRIES;

			var oAC = new $wnd.YAHOO.widget.AutoComplete(input, container, oDS);
			var handler = function(sType, aArgs) {
				var myAC = aArgs[0]; // reference back to the AC instance
				var elLI = aArgs[1]; // reference to the selected LI element
				var oData = aArgs[2]; // object literal of selected item's result data
				var name = oData[0];
				var id = oData[1];

				@honeycrm.client.misc.QuicksearchHelper::callback(ILhoneycrm/client/dto/Dto;Ljava/lang/String;Ljava/lang/String;)(currentCounter, dto, relateFieldName, id);
			};
			oAC.itemSelectEvent.subscribe(handler);
		});
	}-*/;

	public static SafeHtml getQuickSearchMarkup(final Dto dto, final String relateFieldName, final Callback<Dto> callback) {
		callbacks.put(counter, callback);

		final String inputField = "i" + counter;
		final String container = "c" + counter;
	
		if (GWT.isClient()) {
			setupQuicksearch(inputField, container, counter, dto, relateFieldName);
		}

		counter++;
		
		return internalGetQuickSearchMarkup(inputField, container);
	}

	private static SafeHtml internalGetQuickSearchMarkup(final String inputField, final String container) {
		final SafeHtmlBuilder b = new SafeHtmlBuilder();
		b.appendHtmlConstant("<div style='width:15em;padding-bottom:2em;'>");
		b.appendHtmlConstant("<input id=\"" + inputField + "\" type=\"text\">");
		b.appendHtmlConstant("<div id=\"" + container + "\" />");
		b.appendHtmlConstant("</div>");
		return b.toSafeHtml();
	}

	private static void callback(final int currentCounter, final Dto dto, final String relateField, final String id) {
		// update id with selected item's id
		dto.set(relateField, NumberParser.convertToLong(id));

		if (callbacks.containsKey(currentCounter)) {
			callbacks.get(currentCounter).callback(dto);
		}
	}
}
