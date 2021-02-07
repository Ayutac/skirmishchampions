package org.abos.sc.core;

import java.util.Set;
import java.util.stream.Collectors;

import org.abos.util.IdCloneable;
import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.Name;
import org.abos.util.ParsedIdFoundException;
import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * Contains the basic information for fandoms. The information provided by this class is not supposed to change.
 * For information that can change, use {@link Fandom} or a subclass thereof.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class FandomBase implements IdCloneable, Name {
	
	/**
	 * A registry of all globally available fandoms.
	 */
	public static final Registry<FandomBase> FANDOMS = new Registry<>();
	
	/**
	 * The ID of this fandom.
	 * @see #getId()
	 */
	protected final String id;
	
	/**
	 * The name of this fandom.
	 * @see #getName()
	 */
	protected final String name;
	
	/**
	 * The ID of the region this fandom would start with.
	 * @see #getStartRegionId()
	 * @see #getStartRegion()
	 */
	protected final String startRegionId;
	
	/**
	 * The ID of the companion this fandom would start with.
	 * @see #getStartCompanionId()
	 */
	protected final String startCompanionId;
	
	/**
	 * Create a new fandom base.
	 * @param id the ID of this fandom
	 * @param name the name of this fandom
	 * @param startRegionId the ID of the region this fandom would start with
	 * @param startCompanionId the ID of the companion this fandom would start with
	 * @param register if this fandom should be registered in {@link #FANDOMS} or not
	 * @throws NullPointerException If any one of <code>id</code>, <code>name</code>, 
	 * <code>startRegionId</code> or <code>startCompanionId</code> refers to <code>null</code>.
	 * @throws IllegalStateException If <code>register</code> is <code>true</code> but a fandom
	 * with ID <code>id</code> is already registered in {@link #FANDOMS}.
	 * @see #FandomBase(FandomBase)
	 */
	public FandomBase(String id, String name, String startRegionId, String startCompanionId, boolean register) {
		Utilities.requireNonNull(id, "id");
		Utilities.requireNonNull(name, "name");
		Utilities.requireNonNull(startRegionId, "startRegionId");
		Utilities.requireNonNull(startCompanionId, "startCompanionId");
		this.id = id;
		this.name = name;
		this.startRegionId = startRegionId;
		this.startCompanionId = startCompanionId;
		if (register)
			FANDOMS.add(this);
	}
	
	/**
	 * Copy constructor for this class, creates a deep copy on the fandom base level.
	 * @param fandom the fandom base to copy
	 * @throws NullPointerException If <code>fandom</code> refers to <code>null</code>.
	 * @see #FandomBase(String, String, String, String, boolean)
	 * @see #clone()
	 */
	public FandomBase(FandomBase fandom) {
		this(fandom.id, fandom.name, fandom.startRegionId, fandom.startCompanionId, false);
	}

	/**
	 * Returns the ID of this region.
	 * @return the ID of this region
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the display name of this region.
	 * @return the display name of this region
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the ID of the region this fandom would start with.
	 * @return the ID of the region this fandom would start with
	 */
	public String getStartRegionId() {
		return startRegionId;
	}
	
	/**
	 * Returns the region this fandom would start with by looking it up in {@link RegionBase#REGIONS}.
	 * @return the region this fandom would start with, or <code>null</code> if the ID couldn't be found in the registry
	 */
	public RegionBase getStartRegion() {
		return RegionBase.REGIONS.lookup(startRegionId);
	}
	
	/**
	 * Returns the ID of the companion this fandom would start with.
	 * @return the ID of the companion this fandom would start with
	 */
	public String getStartCompanionId() {
		return startCompanionId;
	}
	
	/**
	 * Returns a set of the IDs of all region bases that list the ID of this fandom as their fandom ID
	 * by going through {@link RegionBase#REGIONS}.
	 * @return a set of the IDs of all region bases that list the ID of this fandom as their fandom ID
	 */
	public Set<String> collectAssociatedRegionIds() {
		return RegionBase.REGIONS.stream()
				.filter(base -> id.equals(base.getFandomId()))
				.map(base -> base.getId())
				.collect(Collectors.toSet());
	}
	
	/**
	 * Returns a set of the IDs of all character bases that list the ID of this fandom as their fandom ID
	 * by going through {@link CharacterBase#CHARACTERS}.
	 * @return a set of the IDs of all character bases that list the ID of this fandom as their fandom ID
	 */
	public Set<String> collectAssociatedCharacterIds() {
		return CharacterBase.CHARACTERS.stream()
				.filter(base -> id.equals(base.getFandomId()))
				.map(base -> base.getId())
				.collect(Collectors.toSet());
	}
	
	/**
	 * Returns a deep copy of this fandom base by calling the copy constructor.
	 * @see #FandomBase(FandomBase)
	 */
	@Override
	public Object clone() {
		return new FandomBase(this);
	}

	/**
	 * Generates a hash code for this instance, taking into account all fields. It's guarantied that
	 * the hash codes of two fandom bases are equal if the fandom bases are equal.
	 * @return a hash code for this fandom base
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((startRegionId == null) ? 0 : startRegionId.hashCode());
		result = prime * result + ((startCompanionId == null) ? 0 : startCompanionId.hashCode());
		return result;
	}

	/**
	 * Checks if the specified object is the same as this fandom base, i.e. the other object
	 * must be or inherit from {@link FandomBase}, be the same class as the object this method is called from
	 * and all its <code>FandomBase</code> fields must be the same as the fields of this fandom base
	 * to return <code>true</code>.
	 * If the other object is equal to this fandom base, then their hash codes return the same number, if called on <code>FandomBase</code>.
	 * @param obj the object to check
	 * @return <code>true</code> if this fandom base is equal to the object, else <code>false</code>.
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
		FandomBase other = (FandomBase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (startRegionId == null) {
			if (other.startRegionId != null)
				return false;
		} else if (!startRegionId.equals(other.startRegionId))
			return false;
		if (startCompanionId == null) {
			if (other.startCompanionId != null)
				return false;
		} else if (!startCompanionId.equals(other.startCompanionId))
			return false;
		return true;
	}
	
	/**
	 * Saves the fandom base to a string builder.
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString()
	 * @see #parse(String, boolean)
	 */
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		// if changed, also change the parse function and the documentation of it
		s.append("id");
		s.append(';');
		s.append(name);
		s.append(';');
		s.append(startRegionId);
		s.append(';');
		s.append(startCompanionId);
	}
	
	/**
	 * Returns the fandom base as a string for saving purposes, i.e. in the form needed for {@link #parse(String)}.
	 * @return the fandom base as a string for saving purposes
	 * @see #toSaveString(StringBuilder)
	 * @see #parse(String, boolean)
	 */
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	/**
	 * Returns a string representation of the fandom base. This is identical to
	 * calling {@link #toSaveString()}.
	 * @return a string representation of the fandom base
	 */
	@Override
	public String toString() {
		return toSaveString();
	}
	
	/**
	 * Parses a string representation of a fandom base into a object.
	 * The format is "<code>ID;name;startRegionID;startCompanionID</code>".
	 * @param s the string to parse
	 * @param register If the parsed fandom should be registered in {@link #FANDOMS} after parsing. 
	 * Will throw an exception if this region could be parsed successfully, but their ID is already registered there.
	 * @return a fandom base matching the string
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalNumberOfArgumentsException If the number of arguments in the string separated by <code>;</code> is wrong.
	 * @throws ParsedIdFoundException If <code>register</code> is set to <code>true</code> and the fandom base string is parsed successfully, but the ID is already registered in {@link #FANDOMS}.
	 * @see #FandomBase(String, String, String, String, boolean)
	 * @see #toSaveString()
	 */
	public static FandomBase parse(String s, boolean register) {
		Utilities.requireNonNull(s, "s");
		String[] parts = s.split(";");
		final int NUMBER_OF_ARGUMENTS = 4;
		if (parts.length != NUMBER_OF_ARGUMENTS)
			throw new IllegalNumberOfArgumentsException(String.format("Fandom \"%s\" to parse contained %d arguments instead of %d", s, parts.length, NUMBER_OF_ARGUMENTS));
		try {
			return new FandomBase(parts[0], parts[1], parts[2], parts[3], register);
		}
		catch (IllegalStateException ex) {
			throw new ParsedIdFoundException(ex);
		}
	}
	
	/**
	 * Parses a string representation of a fandom base into a object.
	 * The format is "<code>ID;name;startRegionID;startCompanionID</code>".
	 * @param s the string to parse
	 * @return a fandom base matching the string
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalNumberOfArgumentsException If the number of arguments in the string separated by <code>;</code> is wrong.
	 * @throws ParsedIdFoundException If the fandom base string is parsed successfully, but the ID is already registered in {@link #FANDOMS}.
	 * @see #FandomBase(String, String, String, String, boolean)
	 * @see #toSaveString()
	 */
	public static FandomBase parse(String s) {
		return parse(s, true);
	}

}
