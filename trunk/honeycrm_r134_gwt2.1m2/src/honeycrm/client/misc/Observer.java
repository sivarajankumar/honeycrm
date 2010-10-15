package honeycrm.client.misc;

public interface Observer<T> {
	public void notify(T value);
}
