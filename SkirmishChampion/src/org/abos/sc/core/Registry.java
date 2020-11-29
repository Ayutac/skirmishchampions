package org.abos.sc.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hamcrest.core.IsEqual;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Registry<T extends Id> implements Collection<T> {
	
	/**
	 * the content of this registry
	 */
	private final Map<String,T> content = new HashMap<>();
	
	public Registry() {}
	
	public Registry(Collection<? extends T> c) {
		this();
		addAll(c);
	}
	
	public Registry(T... objects) {
		this();
		for (T object : objects)
			add(object);
	}
	
	/**
	 * Adds a non <code>null</code> item to this registry, if the item's ID isn't in the registry already.
	 * Else throws an exception.
	 * @param item the item to be added
	 * @return <code>true</code> as this registry is always changed when no exception is thrown
	 * @throws NullPointerException If <code>item</code> or its ID refers to <code>null</code>.
	 * @throws IllegalArgumentException If the ID is already within the registry.
	 */
	public boolean add(T item) {
		Utilities.requireNonNull(item, "item");
		String id = item.getId();
		Utilities.requireNonNull(id, "ID of item");
		if (content.containsKey(id))
			throw new IllegalArgumentException("ID "+id+" has already been registered!");
		content.put(id, item);
		return true;
	}
	
//	public boolean addIfMissing(T item) {
//		if (item == null)
//			throw new NullPointerException("item must be specified!");
//		if (lookup(item.getId()) == null)
//			return add(item);
//		else
//			return false;
//	}
	
	/**
	 * Removes the registry entry with the specified ID.
	 * @param id the ID of the entry to remove
	 * @return <code>true</code> if the entry was found and removed, 
	 * <code>false</code> if the entry wasn't found.
	 * @throws NullPointerException If <code>id</code> refers to <code>null</code>.
	 */
	public boolean remove(String id) {
		Utilities.requireNonNull(id, "id");
		return content.remove(id) != null;
	}
	
	/**
	 * Removes the specified registry entry via its ID. Note that if the entry is stored 
	 * within the registry under another key but not under its own ID, the entry stored under 
	 * <code>entry.getId()</code> will be removed. However, as all registry entries are saved
	 * with their ID, this shouldn't happen.
	 * @param entry the entry to remove
	 * @return <code>true</code> if the entry's ID was found and and the entry hence removed, 
	 * <code>false</code> if the entry's ID wasn't found. 
	 * @throws NullPointerException If <code>id</code> refers to <code>null</code>.
	 */
	public boolean remove(T entry) {
		Utilities.requireNonNull(entry, "entry");
		return remove(entry.getId());
	}
	
	/**
	 * Removes all entries from this registry. The registry will be empty after this method returns.
	 */
	@Override
	public void clear() {
		content.clear();
	}
	
	/**
	 * Looks up the ID in the registry and returns the corrosponding entry.
	 * @param id the ID to look up
	 * @return the entry with the given ID or <code>null</code> if no such entry can be found
	 * @see #contains(Object)
	 */
	public T lookup(String id) {
		Utilities.requireNonNull(id, "id");
		T result = content.get(id);
		assert result == null || id.equals(result.getId());
		return result;
	}

	/**
	 * Checks if the registry contains the specified entry.
	 * @param entry the entry to check, NOT the ID of the entry
	 * @return <code>true</code> if the registry contains an object with the same ID as the given
	 * one that is equal to <code>entry</code>. If <code>entry</code> is null or not an ID instance, returns
	 * <code>false</code> immediatly.
	 */
	@Override
	public boolean contains(Object entry) {
		if (entry == null || !(entry instanceof Id))
			return false;
		String id = ((Id)entry).getId();
		if (id == null)
			return false;
		T lookup = lookup(id);
		return lookup != null && lookup.equals(entry);
	}
	
	/**
	 * Returns the number of entries in this registry. If this registry contains more than 
	 * {@link Integer#MAX_VALUE} entries, returns {@link Integer#MAX_VALUE}.
	 * @return the number of entries in this registry
	 */
	@Override
	public int size() {
		return content.size();
	}

	/**
	 * Returns <code>true</code> if this registry contains no entries.
	 * @return <code>true</code> if this registry contains no entries
	 */
	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}
	
	/**
	 * Returns an iterator over the entries in this registry. 
	 * There are no guarantees concerning the order in which the entries are returned;
	 * especially the order of the iterator is almost guarantied to differ from the order
	 * in which the entries were added to this registry.
	 * @return an {@link Iterator} over the lements in this collection 
	 */
	@Override
	public Iterator<T> iterator() {
		return content.values().iterator();
	}
	
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append('[');
		if (!isEmpty()) {
			Iterator<T> it = iterator();
			s.append(it.next().getId());
			while (it.hasNext()) {
				s.append(',');
				s.append(it.next().getId());
			}
		}
		s.append(']');
		return s.toString();
	}

	@Override
	public Object[] toArray() {
		return content.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return content.values().toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return content.values().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		if (c == null || c.isEmpty())
			return false;
		for (T obj : c) {
			add(obj);
		}
		return true;
	}
	
	/**
	 * Not supported for {@link Registry}, as it is unclear if the object
	 * is the name of the entry to be removed or the entry itself. Use one of the other two
	 * remove operations instead
	 * @throws UnsupportedOperationException Always.
	 * @see #remove(String)
	 * @see #remove(T)
	 */
	@Override
	public boolean remove(Object obj) {
		throw new UnsupportedOperationException("Use either a string or the type for the removal!");
	}

	/**
	 * Not supported for {@link Registry}, as it is unclear if the collection contains
	 * the names of entries to be removed or the entries themselves.
	 * @throws UnsupportedOperationException Always.
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Use remove instead.");
	}

	/**
	 * Not supported for {@link Registry}, as it is unclear if the collection contains
	 * the names of entries to be retained or the entries themselves.
	 * @throws UnsupportedOperationException Always.
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Use remove instead.");
	}

}
