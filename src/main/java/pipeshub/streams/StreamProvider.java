package pipeshub.streams;

/**
 * Provides the objects to be consumed by the streams. 
 * @author worker
 *
 * @param <T>
 */
public interface StreamProvider<T> {
	
	/**
	 * Tells weather the incoming objects are available for consumption.
	 * Providers need not to block this operation, as the PipesHub stream will 
	 * continuously check and will wait if required.
	 * @return <code>true</code> if available 
	 * 		   <code>false</code> otherwise	
	 */
	public boolean available();
	
	/**
	 * Get the next available object from the source. 
	 * Providers need not block this operation and return an empty or 
	 * <code>null</code> result.
	 * @return   <code>T</code> Object, If object is available for consumption
	 *        or <code>null</code> or empty result , This depends on provider implemetor
	 */
	public T stream();
}
