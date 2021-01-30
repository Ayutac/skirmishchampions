package org.abos.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Collection of objects having a (unique) ID. Internally a hash map is
 * used for fast access of the items via their ID. Adding <code>null</code> or entries 
 * with an <code>null</code> ID will cause exceptions. Since the entries can
 * also be accessed via their IDs, the remove operations of {@link Collection}
 * are not supported, instead there are remove operations for either the entries or their IDs.
 * @param <T> the type of entries, must have an ID
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1
 * @see #add(T)
 * @see #lookup(String)
 * @see #remove(T)
 * @see #removeById(String)
 * @see #iterator()
 */
public class Registry<T extends Id> implements Collection<T> {
	
	/**
	 * the content of this registry
	 */
	private final Map<String,T> content = new HashMap<>();
	
	/**
	 * Creates an empty registry.
	 */
	public Registry() {}
	
	/**
	 * Creates a registry with all the elements in the collection.
	 * @param c the collection with the elements to add
	 * @throws NullPointerException If <code>c</code>, any of its element or
	 * any of the element's IDs refers to <code>null</code>.
	 * @throws IllegalArgumentException If <code>c</code> contains multiple elements with the same ID.
	 */
	public Registry(Collection<? extends T> c) {
		this();
		Utilities.requireNonNull(c, "c");
		addAll(c);
	}
	
	/**
	 * Creates a registry with the specified elements.
	 * @param objects the elements to add to the registry
	 * @throws NullPointerException if any object to add or their ID refers to <code>null</code>.
	 * @throws IllegalArgumentException If there are multiple objects with the same ID.
	 */
	@SafeVarargs
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
	
	/**
	 * Removes the registry entry with the specified ID.
	 * @param id the ID of the entry to remove
	 * @return <code>true</code> if the entry was found and removed, 
	 * <code>false</code> if the entry wasn't found.
	 * @throws NullPointerException If <code>id</code> refers to <code>null</code>.
	 */
	public boolean removeById(String id) {
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
		return removeById(entry.getId());
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
	 * Checks if the registry contains a specified ID.
	 * @param id the ID of the entry to check
	 * @return <code>true</code> if the registry contains an object with the same ID as the given
	 * one, else <code>false</code>.
	 */
	public boolean containsId(String id) {
		if (id == null)
			return false;
		return content.containsKey(id);
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
	 * @return an {@link Iterator} over the elements in this collection 
	 */
	@Override
	public Iterator<T> iterator() {
		return content.values().iterator();
	}
	
	/**
	 * Iterates over the elements of this registry and puts their ID into a string,
	 * similarily to {@link Arrays#toString()} but without whitespaces.
	 * So for example with three entries it would be <code>"[itemAID,itemBID,itemCID]"</code>.
	 * @return the registry content as a string
	 * @see #iterator()
	 */
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

	/**
	 * Returns an array containing all of the entries in this registry in no particular order
	 * by calling <code>toArray(T[] a)</code> on <code>values()</code> of the underlying map.
	 * @return the registry entries as an {@link Object} array
	 * @see #iterator()
	 * @see #toArray(T[])
	 * @see Map#values()
	 * @see Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return content.values().toArray();
	}

	/**
	 * Returns an array containing all of the entries in this registry in no particular order
	 * by calling <code>toArray(T[] a)</code> on <code>values()</code> of the underlying map;
	 * the runtime type of the returned array is that of the specified array.
	 * If the collection fits in the specified array, it is returned therein.
	 * Otherwise, a new array is allocated with the runtime type of the
	 * specified array and the size of this collection.
	 * <br/><br/>
	 * If this collection fits in the specified array with room to spare 
	 * (i.e., the array has more elements than this collection), the element
	 * in the array immediately following the end of the collection is set to <code>null</code>.
	 * @param <T> the component type of the array to contain the registry
	 * @param a The array into which the entries of this registration are to bestored, if it is big enough; 
	 * otherwise, a new array of the same runtime type is allocated for this purpose.
	 * @return an array containing all of the elements in this registry
	 * @see #iterator()
	 * @see #toArray()
	 * @see Map#values()
	 * @see Collection#toArray(T[])
	 */
	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return content.values().toArray(a);
	}

	/**
	 * Checks if the registry contains all the objects given by the collection
	 * by calling <code>containsAll</code> on <code>values()</code> of the underlying map.
	 * @param c Collection to be checked for containment in this registry. 
	 * Note that the collection should contain elements of type <code>T</code> and not of <code>Entry&lt;String, T&gt;</code>, 
	 * as the keys of the underlying map are ignored.
	 * @return <code>true</code> if this registry contains all of the elements in the specified collection, else <code>false</code>
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return content.values().containsAll(c);
	}

	/**
	 * Adds all elements in the specified collection to this registry.
	 * @param c the collection with the elements to add
	 * @return <code>false</code> if <code>c</code> refers to <code>null</code> or is empty,
	 * otherwise <code>true</code> (as the registry will always be changed unless an exception is thrown).
	 * @throws NullPointerException If <code>c</code> contains <code>null</code> or an element with
	 * <code>null</code> as an ID.
	 * @throws IllegalArgumentException If <code>c</code> contains multiple elements with the same ID
	 * or an element with an ID already registered in this registry.
	 */
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
	 * @see #removeById(String)
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
	
	/**
	 * Creates a deep clone of a registry.
	 * @param <T> the type of entries, must have an ID and be cloneable
	 * @param registry the registry to clone
	 * @return A clone of the specified registry. 
	 * Will only be <code>null</code> if <code>registry</code> refers to <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IdCloneable> Registry<T> deepClone(Registry<T> registry) {
		if (registry == null)
			return null;
		Registry<T> clone = new Registry<>();
		for (T item : registry) {
			clone.add((T)item.clone());
		}
		return clone;
	}

}
