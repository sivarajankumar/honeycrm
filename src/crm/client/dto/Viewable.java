package crm.client.dto;

// TODO find better name for this interface..
public interface Viewable {
	public String getTitle();
	public int[] getListViewColumnIds();
	public long getId();
	public void setFieldValue(final int index, final Object value);
	public void setId(long id);
	public boolean isMarked();
	public void setMarked(boolean marked);
	
	public int[][] getFormFieldIds();
	public Object getFieldValue(final int id);
	public Field getFieldById(final int id);
	public String getHistoryToken();
	public int[][] getSearchFields();
}
