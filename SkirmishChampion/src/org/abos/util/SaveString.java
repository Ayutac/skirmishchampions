package org.abos.util;

/**
 * An interface to show something can saved as a string. All implementing classes 
 * should also provide a static parse function of some kind. 
 * @author Sebastian Koch
 * @version %I%
 * @since 0.7
 */
public interface SaveString {

	/**
	 * Appends this object in string form to a string builder
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString()
	 */
	public void toSaveString(StringBuilder s);
	
	/**
	 * Returns this object as a string for saving purposes, i.e. in the form needed for a static parse function.
	 * For more information see {@link #toSaveString(StringBuilder)}.
	 * @return the object as a string for saving purposes
	 * @see #toSaveString(StringBuilder)
	 */
	public default String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}
	
}
