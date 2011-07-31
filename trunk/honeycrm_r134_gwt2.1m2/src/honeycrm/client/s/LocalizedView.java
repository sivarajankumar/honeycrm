package honeycrm.client.s;

import honeycrm.client.LocalizedMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;

public class LocalizedView extends Composite {
	protected final LocalizedMessages constants = GWT.create(LocalizedMessages.class);
}
