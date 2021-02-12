package org.abos.util;

import java.util.Comparator;

/**
 * For denoting an object has an ID that is (supposed to be) unique.
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1
 */
public interface Id {
	
	/**
	 * Returns the ID of the object.
	 * @return the ID of the object
	 */
	public String getId();
	
	/**
	 * Returns a comparator that sorts objects with IDs alphabetically by their IDs.
	 * <code>null</code> is permitted for both objects to compare and names of the objects to compare.
	 * <code>null</code> is smaller than any other instance of <code>T</code> and an object with 
	 * a <code>null</code> ID is smaller than any other non <code>null</code> instance of <code>T</code>,
	 * except of course in both cases if both objects respectively their OD are <code>null</code>,
	 * in which this comparator evaluates them as equal.  
	 * @param <T> the class extending {@link ID}
	 * @return A comparator for <code>T</code>.
	 */
	public static <T extends Id> Comparator<T> createIdComparator() {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				if (o1.getId() == o2.getId())
					return 0;
				if (o1.getId() == null)
					return Integer.MIN_VALUE;
				if (o2.getId() == null)
					return Integer.MAX_VALUE;
				return o1.getId().compareTo(o2.getId());
			}
		};
	}

}
