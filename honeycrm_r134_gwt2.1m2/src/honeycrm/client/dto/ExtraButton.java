package honeycrm.client.dto;

import honeycrm.client.actions.AbstractAction;
import honeycrm.client.view.ModuleAction;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ExtraButton implements Serializable, IsSerializable {
	private static final long serialVersionUID = -1573835401765337631L;
	private String label;
	private AbstractAction action;
	private ModuleAction show;

	public ExtraButton() {
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public AbstractAction getAction() {
		return action;
	}

	public void setAction(AbstractAction action) {
		this.action = action;
	}

	public ModuleAction getShow() {
		return show;
	}

	public void setShow(ModuleAction show) {
		this.show = show;
	}
}
