package honeycrm.client.plugin;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Widget;

abstract public class AbstractPlugin implements Serializable {
	public enum ModifactionPlace {
		HEADER
	}
	
	private static final long serialVersionUID = 7911325484912055158L;
	private boolean enabled = false;

	public AbstractPlugin() { // for serialisation
	}
	
	public String getName() {
		return getClass().toString();
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public abstract Widget getWidget();
	public abstract ModifactionPlace getModificationPlace();
}
