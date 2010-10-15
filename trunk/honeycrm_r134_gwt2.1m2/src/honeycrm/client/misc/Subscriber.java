package honeycrm.client.misc;

public interface Subscriber<T> {
	public void subscribe(final Observer<T> observer);
}
