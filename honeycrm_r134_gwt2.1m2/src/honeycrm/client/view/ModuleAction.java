package honeycrm.client.view;

public enum ModuleAction {
	CREATE, INIT, DETAIL, EDIT, DELETE, IMPORT, EXPORT, PRINT, DUPLICATE, FINDDUPLICATES, CHANGES, CLOSE, CANCEL, SAVE, ADVANCEDSEARCH, REPORT;
	
	public static ModuleAction fromString(final String str) {
		for (final ModuleAction action: ModuleAction.values()) {
			if (action.toString().toLowerCase().equals(str.toLowerCase())) {
				return action;
			}
		}
		return null;
	}
}
