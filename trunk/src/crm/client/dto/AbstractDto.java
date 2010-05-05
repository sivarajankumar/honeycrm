package crm.client.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractDto implements Serializable, Viewable {
	private static final long serialVersionUID = -656461822356703122L;
	// Instances of all existing Dto classes
	private static final Map<Class<? extends Viewable>, AbstractDto> map = new HashMap<Class<? extends Viewable>, AbstractDto>();
	protected long id;
	protected Set<Field> fields = new HashSet<Field>();
	
	static {
		// add new modules here
		map.put(DtoContact.class, new DtoContact());
		map.put(DtoAccount.class, new DtoAccount());
		map.put(DtoEmployee.class, new DtoEmployee());
	}
	
	public AbstractDto() {
	}
	
	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public static Viewable getViewable(final Class<? extends Viewable> clazz) {
		assert map.containsKey(clazz);
		return map.get(clazz);
	}
	
	public static Viewable getViewableForHistoryToken(final String historyToken) {
		for (final Class<? extends Viewable> clazz: map.keySet()) {
			if (historyToken.equals(map.get(clazz).getHistoryToken())) {
				return map.get(clazz);
			}
		}
		assert false; // should never reach this point..
		return null;
	}
	
	@Override
	public Field getFieldById(final int id) {
		for (final Field field: fields) {
			if (id == field.getId()) {
				return field;
			}
		}
	
		// did not find a field with this id. should never reach this point.
		assert false;
		
		return null;
	}
}
