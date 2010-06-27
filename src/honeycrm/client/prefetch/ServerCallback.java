package honeycrm.client.prefetch;

public interface ServerCallback<T> {
	public void doRpc(Consumer<T> internalCacheCallback);
}
