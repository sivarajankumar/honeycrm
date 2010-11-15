package honeycrm.client.plugin;

import java.io.Serializable;

abstract public class AbstractPlugin implements Serializable {
	public abstract interface PluginView {
	}

	private static final long serialVersionUID = 7911325484912055158L;
	private boolean enabled = false;
	protected IPlatform platform;

	public AbstractPlugin() { // for serialisation
	}

	public void initialize(final IPlatform app/* , final PluginView view */) {
		this.platform = app;
		// setView(view);
	}

	/**
	 * @return True if the platform and view have been set for the plugin. False otherwise.
	 */
	public boolean isInitialized() {
		return null != platform/* && null != getView() */;
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

	public void runPlugin() {
		if (isInitialized()) {
			internalRunPlugin();
		}
	}

	abstract protected void internalRunPlugin();

	// abstract protected void setView(PluginView view);
	// abstract protected PluginView getView();
	// abstract public PluginView getDefaultView();
}
