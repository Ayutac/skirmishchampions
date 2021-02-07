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
 * Contains the basic information for regions. The information provided by this class is not supposed to change.
 * For information that can change, use {@link Region} or a subclass thereof.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see Region
 */
public class RegionBase implements IdCloneable, Name {
	
	/**
	 * A registry of all globally available regions.
	 */
	public static final Registry<RegionBase> REGIONS = new Registry<>();
	
	/**
	 * The ID of this region.
	 * @see #getId()
	 */
	protected final String id;
	
	/**
	 * The name of this region.
	 * @see #getName()
	 */
	protected final String name;
	
	/**
	 * The fandom ID of this region.
	 * @see #getFandomId()
	 * @see #getFandom()
	 */
	protected final String fandomId;
	
	/**
	 * The ID of the stage this region would start with.
	 * @see #getStartStageId()
	 * @see #getStartStage()
	 */
	protected final String startStageId;
	
	/**
	 * Creates a new region base.
	 * @param id the ID of this region
	 * @param name the name of this region
	 * @param fandomId the ID of the fandom associated to this region
	 * @param startStageId the ID of the stage this region would start with
	 * @param register if this region should be registered in {@link #REGIONS} or not
	 * @throws NullPointerException If any one of <code>id</code>, <code>name</code>, 
	 * <code>fandomId</code> or <code>startStageId</code> refers to <code>null</code>.
	 * @throws IllegalStateException If <code>register</code> is <code>true</code> but a region
	 * with ID <code>id</code> is already registered in {@link #REGIONS}.
	 * @see #RegionBase(RegionBase)
	 */
	public RegionBase(String id, String name, String fandomId, String startStageId, boolean register) {
		Utilities.requireNonNull(id, "id");
		Utilities.requireNonNull(name, "name");
		Utilities.requireNonNull(fandomId, "fandomId");
		Utilities.requireNonNull(startStageId, "startStageId");
		this.id = id;
		this.name = name;
		this.fandomId = fandomId;
		this.startStageId = startStageId;
		if (register)
			REGIONS.add(this);
	}
	
	/**
	 * Copy constructor for this class, creates a deep copy on the region base level.
	 * @param region the region base to copy
	 * @throws NullPointerException If <code>region</code> refers to <code>null</code>.
	 * @see #RegionBase(String, String, String, String, boolean)
	 * @see #clone()
	 */
	public RegionBase(RegionBase region) {
		// throws NPE
		this(region.id, region.name, region.fandomId, region.startStageId, false);
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
	 * Returns the ID of the associated fandom of this region.
	 * @return the ID of the associated fandom of this region
	 */
	public String getFandomId() {
		return fandomId;
	}

	/**
	 * Returns the associated fandom of this region by looking it up in {@link FandomBase#FANDOMS}.
	 * @return the associated fandom of this region, or <code>null</code> if the ID couldn't be found in the registry
	 */
	public FandomBase getFandom() {
		return FandomBase.FANDOMS.lookup(fandomId);
	}
	
	/**
	 * Returns the ID of the stage this region would start with.
	 * @return the ID of the stage this region would start with
	 */
	public String getStartStageId() {
		return startStageId;
	}
	
	/**
	 * Returns the stage this region would start with by looking it up in {@link StageBase#STAGES}.
	 * @return the stage this region would start with, or <code>null</code> if the ID couldn't be found in the registry
	 */
	public StageBase getStartStage() {
		return StageBase.STAGES.lookup(startStageId);
	}
	
	/**
	 * Returns a set of the IDs of all stage bases that list the ID of this region as their region ID
	 * by going through {@link StageBase#STAGES}.
	 * @return a set of the IDs of all stage bases that list the ID of this region as their region ID
	 */
	public Set<String> collectAssociatedStageIds() {
		return StageBase.STAGES.stream()
				.filter(base -> id.equals(base.getRegionId()))
				.map(base -> base.getId())
				.collect(Collectors.toSet());
	}
	
	/**
	 * Returns a deep copy of this region base by calling the copy constructor.
	 * @see #RegionBase(RegionBase)
	 */
	@Override
	public Object clone() {
		return new RegionBase(this);
	}

	/**
	 * Generates a hash code for this instance, taking into account all fields. It's guarantied that
	 * the hash codes of two region bases are equal if the region bases are equal.
	 * @return a hash code for this region base
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((fandomId == null) ? 0 : name.hashCode());
		result = prime * result + ((startStageId == null) ? 0 : startStageId.hashCode());
		return result;
	}

	/**
	 * Checks if the specified object is the same as this region base, i.e. the other object
	 * must be or inherit from {@link RegionBase}, be the same class as the object this method is called from
	 * and all its <code>RegionBase</code> fields must be the same as the fields of this region base
	 * to return <code>true</code>.
	 * If the other object is equal to this region base, then their hash codes return the same number, if called on <code>RegionBase</code>.
	 * @param obj the object to check
	 * @return <code>true</code> if this region base is equal to the object, else <code>false</code>.
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
		RegionBase other = (RegionBase) obj;
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
		if (fandomId == null) {
			if (other.fandomId != null)
				return false;
		} else if (!fandomId.equals(other.fandomId))
			return false;
		if (startStageId == null) {
			if (other.startStageId != null)
				return false;
		} else if (!startStageId.equals(other.startStageId))
			return false;
		return true;
	}
	
	/**
	 * Saves the region base to a string builder.
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
		s.append(fandomId);
		s.append(';');
		s.append(startStageId);
	}
	
	/**
	 * Returns the region base as a string for saving purposes, i.e. in the form needed for {@link #parse(String)}.
	 * @return the region base as a string for saving purposes
	 * @see #toSaveString(StringBuilder)
	 * @see #parse(String, boolean)
	 */
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	/**
	 * Returns a string representation of the region base. This is identical to
	 * calling {@link #toSaveString()}.
	 * @return a string representation of the region base
	 */
	@Override
	public String toString() {
		return toSaveString();
	}
	
	/**
	 * Parses a string representation of a region base into a object.
	 * The format is "<code>ID;name;fandomID;startStageID</code>".
	 * @param s the string to parse
	 * @param register If the parsed region should be registered in {@link #REGIONS} after parsing. 
	 * Will throw an exception if this region could be parsed successfully, but their ID is already registered there.
	 * @return a region base matching the string
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalNumberOfArgumentsException If the number of arguments in the string separated by <code>;</code> is wrong.
	 * @throws ParsedIdFoundException If <code>register</code> is set to <code>true</code> and the region base string is parsed successfully, but the ID is already registered in {@link #REGIONS}.
	 * @see #RegionBase(String, String, String, String, boolean)
	 * @see #toSaveString()
	 */
	public static RegionBase parse(String s, boolean register) {
		Utilities.requireNonNull(s, "s");
		String[] parts = s.split(";");
		final int NUMBER_OF_ARGUMENTS = 4;
		if (parts.length != NUMBER_OF_ARGUMENTS)
			throw new IllegalNumberOfArgumentsException(String.format("Region \"%s\" to parse contained %d arguments instead of %d", s, parts.length, NUMBER_OF_ARGUMENTS));
		try {
			return new RegionBase(parts[0], parts[1], parts[2], parts[3], register);
		}
		catch (IllegalStateException ex) {
			throw new ParsedIdFoundException(ex);
		}
	}
	
	/**
	 * Parses a string representation of a region base into a object.
	 * The format is "<code>ID;name;fandomID;startStageID</code>".
	 * @param s the string to parse
	 * @return a region base matching the string
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalNumberOfArgumentsException If the number of arguments in the string separated by <code>;</code> is wrong.
	 * @throws ParsedIdFoundException If the region base string is parsed successfully, but the ID is already registered in {@link #REGIONS}.
	 * @see #RegionBase(String, String, String, String, boolean)
	 * @see #toSaveString()
	 */
	public static RegionBase parse(String s) {
		return parse(s, true);
	}

}
