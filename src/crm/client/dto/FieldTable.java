package crm.client.dto;

public class FieldTable extends Field {
	private static final long serialVersionUID = 5834729592030900010L; 
	private Class<? extends AbstractDto> tableDtoClass;
	
	public FieldTable(final int id, final String label, final Class<? extends AbstractDto> tableDto) {
		super(id, Field.Type.TABLE, label);
		this.tableDtoClass = tableDto;
	}
	
	
}
