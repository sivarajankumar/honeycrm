package honeycrm.client.view;

import honeycrm.client.dto.AbstractDto;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;

abstract public class ITableWidget extends Composite {
	abstract public List<? extends AbstractDto> getData();
	abstract public void setData(List<? extends AbstractDto> data);
}
