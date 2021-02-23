package org.abos.sc.core;

import java.util.Comparator;

import org.abos.util.AbstractNamedComparator;

/**
 * An interface to give challenge ratings of objects.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
public interface ChallengeRatable {
	
	/**
	 * Returns an integer for this object rating it in some way
	 * @return the challenge rating of this object
	 */
	public int getChallengeRating();

	/**
	 * Creates a comparator for challenge ratable objects, sorting them by their challenge rating
	 * from smallest to biggest value. <code>null</code> objects are allowed and sorted as smaller than any non <code>null</code> object.
	 * If both supplied stages are non <code>null</code>, the compare method of the comparator
	 * returns exactly their difference in challenge ratings (first minus second).
	 * @param <T> a subclass of {@link ChallengeRatable}
	 * @return a comparator for the challenge rating
	 */
	public static <T extends ChallengeRatable> AbstractNamedComparator<T> createCRComparator() {
		return new AbstractNamedComparator<T>("CR") {
			@Override public int compare(T o1, T o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				return o1.getChallengeRating() - o2.getChallengeRating();
			}
		};
	}
	
}
