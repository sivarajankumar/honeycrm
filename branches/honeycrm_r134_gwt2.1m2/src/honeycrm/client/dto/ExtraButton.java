package honeycrm.client.dto;

import honeycrm.client.actions.CreateContractAction;
import honeycrm.client.actions.AbstractAction;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ExtraButton implements Serializable, IsSerializable {
	private static final long serialVersionUID = -1573835401765337631L;
	private String label = null;
	private AbstractAction action = null;

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
	//	return new CreateContractAction();
	}

	public void setAction(AbstractAction action) {
		this.action = action;
	}
}
