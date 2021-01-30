package org.abos.util;

/**
 * Combines the <code>Id</code> interface with the <code>Clonable</code> interface
 * for generic type use
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.4
 */
public interface IdCloneable extends Id, Cloneable {
	
	/**
	 * Creates a clone of this object.
	 * @return a clone of this object
	 */
	public Object clone();
	
}
