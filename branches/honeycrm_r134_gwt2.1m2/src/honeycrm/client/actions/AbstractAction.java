package honeycrm.client.actions;

import java.io.Serializable;

import honeycrm.client.dto.Dto;

abstract public class AbstractAction implements Serializable {
	private static final long serialVersionUID = -1374882727320521289L;

	public AbstractAction() {
	}
	
	abstract public void doAction(Dto dto);
}
