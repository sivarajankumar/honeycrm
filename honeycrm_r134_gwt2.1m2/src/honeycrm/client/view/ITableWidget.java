package honeycrm.client.view;

import honeycrm.client.dto.Dto;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;

@Deprecated
abstract public class ITableWidget extends Composite {
	abstract public List<Dto> getData();

//	abstract public void setData(List<Dto> data);
}
