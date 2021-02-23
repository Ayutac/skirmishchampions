package org.abos.util;

import java.util.Comparator;

/**
 * A comparator with a name for easier use in sorters.
 * @author Sebastian Koch
 * @version %I%
 * @param <T> the type for comparison
 * @since Skirmish Champions 0.7
 */
public abstract class AbstractNamedComparator<T> implements Name, Comparator<T> {
	
	/**
	 * The name of this comparator.
	 * @see #getName()
	 */
	protected final String name;

	/**
	 * Creates a new comparator with the specified string.
	 * @param name the name of this comparator, not <code>null</code>.
	 * @throws NullPointerException If <code>name</code> refers to <code>null</code>.
	 */
	public AbstractNamedComparator(String name) {
		Utilities.requireNonNull(name, "name");
		this.name = name;
	}
	
	/**
	 * Returns the name of this comparator.
	 * @return the name of this comparator, not <code>null</code>
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Generates a hash code for this comparator, taking into account its name and class. It's guarantied that
	 * the hash codes of two abstract named comparators are equal if the abstract named comparators are equal.
	 * @return a hash code for this character base
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode() {
		final int prime = 103;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + getClass().hashCode();
		return result;
	}

	/**
	 * Tests if another object is equal to this abstract named comparator. Two abstract named comparator are equal if and only if
	 * their names are equal.
	 * @return <code>true</code> if the other object is a abstract named comparator equal to this one, else <code>false</code>
	 * @see #hashCode()
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractNamedComparator<?> other = (AbstractNamedComparator<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	/**
	 * Returns a string representation of the abstract named comparator. This is identical to calling {@link #getName()}.
	 * @return a string representation of the abstract named comparator
	 */
	@Override
	public String toString() {
		return getName();
	}
	
}
