package honeycrm.client.dto;

import java.io.Serializable;

/**
 * Encapsulates the list query results together with the total item counter. For pagination support
 * it makes sense to transfer both values encapsulated in one container.
 */
public class ListQueryResult<T extends AbstractDto> implements Serializable {
	private T[] results;
	private int itemCount = 0;
	private static final long serialVersionUID = 2873158543385988528L;

	public ListQueryResult() { // is there because gwt needs it for serialisation purposes
	}

	public ListQueryResult(final T[] results, final int itemCount) {
		this.results = results;
		this.itemCount = itemCount;
	}

	public T[] getResults() {
		if (null == results) {
			return (T[]) new AbstractDto[0];
		} else {
			return results;
		}
	}

	public int getItemCount() {
		return itemCount;
	}
}
