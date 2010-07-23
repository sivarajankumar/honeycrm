package honeycrm.client.dto;

import java.io.Serializable;

/**
 * Encapsulates the list query results together with the total item counter. For pagination support it makes sense to transfer both values encapsulated in one container.
 */
public class ListQueryResult implements Serializable {
	private Dto[] results;
	private int itemCount = 0;
	private static final long serialVersionUID = 2873158543385988528L;

	public ListQueryResult() { // is there because gwt needs it for serialisation purposes
	}

	public ListQueryResult(final Dto[] results, final int itemCount) {
		this.results = results;
		this.itemCount = itemCount;
	}

	public Dto[] getResults() {
		if (null == results) {
			return new Dto[0];
		} else {
			return results;
		}
	}

	public int getItemCount() {
		return itemCount;
	}
}
