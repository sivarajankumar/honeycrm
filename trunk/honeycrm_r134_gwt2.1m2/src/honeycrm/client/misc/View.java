package honeycrm.client.misc;

public enum View {
    DETAIL, EDIT, CREATE, LIST, LIST_HEADER;
    
    public static boolean isReadOnly(final View v) {
    	return v.equals(DETAIL) || v.equals(LIST) || v.equals(LIST_HEADER);
    }
}

