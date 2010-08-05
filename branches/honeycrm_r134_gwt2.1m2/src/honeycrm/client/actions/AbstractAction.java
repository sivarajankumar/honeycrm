package honeycrm.client.actions;

import honeycrm.client.dto.Dto;

import java.io.Serializable;

abstract public class AbstractAction implements Serializable {
	private static final long serialVersionUID = -1374882727320521289L;

	public AbstractAction() {
	}

	abstract public void doAction(Dto dto);
}
