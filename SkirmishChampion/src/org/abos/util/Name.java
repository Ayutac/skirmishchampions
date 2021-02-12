package org.abos.util;

import java.util.Comparator;

/**
 * For denoting an object has a name.
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1
 */
public interface Name {
	
	/**
	 * Returns the name of the object.
	 * @return the name of the object
	 */
	public String getName();
	
	/**
	 * Returns a comparator that sorts objects with names alphabetically by their names.
	 * <code>null</code> is permitted for both objects to compare and names of the objects to compare.
	 * <code>null</code> is smaller than any other instance of <code>T</code> and an object with 
	 * a <code>null</code> name is smaller than any other non <code>null</code> instance of <code>T</code>,
	 * except of course in both cases if both objects respectively their names are <code>null</code>,
	 * in which this comparator evaluates them as equal.  
	 * @param <T> the class extending {@link Name}
	 * @return A comparator for <code>T</code>.
	 */
	public static <T extends Name> Comparator<T> createNameComparator() {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				if (o1.getName() == o2.getName())
					return 0;
				if (o1.getName() == null)
					return Integer.MIN_VALUE;
				if (o2.getName() == null)
					return Integer.MAX_VALUE;
				return o1.getName().compareTo(o2.getName());
			}
		};
	}

}
