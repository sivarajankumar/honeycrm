package honeycrm.server.domain.decoration;

import honeycrm.client.actions.AbstractAction;
import honeycrm.client.view.ModuleAction;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HasExtraButton {
	String label();
	Class<? extends AbstractAction> action();
	ModuleAction show();
}
