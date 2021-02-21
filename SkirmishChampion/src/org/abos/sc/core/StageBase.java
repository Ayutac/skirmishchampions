package org.abos.sc.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.abos.sc.core.battle.Encounter;
import org.abos.util.IdCloneable;
import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.Name;
import org.abos.util.ParseException;
import org.abos.util.ParsedIdFoundException;
import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * Contains the basic information for stages. The information provided by this class is not supposed to change.
 * For information that can change, use {@link Stage} or a subclass thereof.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see Stage
 */
public class StageBase implements IdCloneable, Name, ChallengeRatable {
	
	/**
	 * Separator character for the lists.
	 * @see #parse(String, boolean)
	 */
	public final static char LIST_SEPARATOR = ',';

	/**
	 * A registry of all globally available stages.
	 */
	public static final Registry<StageBase> STAGES = new Registry<>();
	
	/**
	 * A collection of challenge ratings associated to their stage IDs.
	 */
	// stage ratings should be accessed via the stages and not directly via RATINGS, that's why this field is protected
	protected static final Map<String, Integer> RATINGS = new HashMap<>();
	
	/**
	 * The ID of this stage.
	 * @see #getId()
	 */
	protected final String id;
	
	/**
	 * The name of this stage.
	 * @see #getName()
	 */
	protected final String name;
	
	/**
	 * The region ID of this stage.
	 * @see #getRegionId()
	 * @see #getRegion()
	 */
	protected final String regionId;
	
	/**
	 * Stages that become available through this one.
	 */
	protected final String[] nextStages;

	/**
	 * Regions that become available through this stage.
	 */
	protected final String[] nextRegions;
	
	/**
	 * Fandoms that become available through this stage.
	 */
	protected final String[] nextFandoms;
	
	/**
	 * The encoded encounter of this stage.
	 * @see #createEncounter()
	 */
	protected final String encounterString;
	
	/**
	 * Creates a new stage base.
	 * @param id the ID of this stage
	 * @param name the display name of this stage
	 * @param regionId the ID of the associated region of this stage
	 * @param nextStages IDs of stages that will become available by beating this stage. <code>null</code> is treated as empty array.
	 * @param nextRegions IDs of regions that will become available by beating this stage. <code>null</code> is treated as empty array.
	 * @param nextFandoms IDs of fandoms that will become available by beating this stage. <code>null</code> is treated as empty array.
	 * @param encounterString the encoded encounter of this stage
	 * @param register if this stage should be registered in {@link #STAGES} or not
	 * @throws NullPointerException If any one of <code>id</code>, <code>name</code>, 
	 * <code>regionId</code> or <code>encounterString</code> refers to <code>null</code>.
	 * @throws IllegalStateException If <code>register</code> is <code>true</code> but a stage
	 * with ID <code>id</code> is already registered in {@link #STAGES}.
	 * @see #StageBase(StageBase)
	 */
	public StageBase(String id, String name, String regionId, String[] nextStages, String[] nextRegions,
			String[] nextFandoms, String encounterString, boolean register) {
		Utilities.requireNonNull(id, "id");
		Utilities.requireNonNull(name, "name");
		Utilities.requireNonNull(regionId, "regionId");
		Utilities.requireNonNull(encounterString, "encounterString");
		this.id = id;
		this.name = name;
		this.regionId = regionId;
		if (nextStages == null)
			this.nextStages = new String[0];
		else
			this.nextStages = Arrays.copyOf(nextStages, nextStages.length);
		if (nextRegions == null)
			this.nextRegions = new String[0];
		else
			this.nextRegions = Arrays.copyOf(nextRegions, nextRegions.length);
		if (nextFandoms == null)
			this.nextFandoms = new String[0];
		else
			this.nextFandoms = Arrays.copyOf(nextFandoms, nextFandoms.length);
		this.encounterString = encounterString;
		if (register)
			STAGES.add(this);
	}
	
	/**
	 * Copy constructor for this class, creates a deep copy on the stage base level.
	 * @param stage the stage base to copy
	 * @throws NullPointerException If <code>stage</code> refers to <code>null</code>.
	 * @see #StageBase(String, String, String, String[], String[], String[], String, boolean)
	 * @see #clone()
	 */
	public StageBase(StageBase stage) {
		// throws NPE
		this(stage.id, stage.name, stage.regionId, stage.nextStages, stage.nextRegions, stage.nextFandoms, stage.encounterString, false);
	}

	/**
	 * Returns the ID of this stage.
	 * @return the ID of this stage
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Returns the display name of this stage.
	 * @return the display name of this stage
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the ID of the associated region of this stage.
	 * @return the ID of the associated region of this stage
	 */
	public String getRegionId() {
		return regionId;
	}
	
	/**
	 * Returns the associated region of this stage by looking it up in {@link RegionBase#REGIONS}.
	 * @return the associated region of this stage, or <code>null</code> if the ID couldn't be found in the registry
	 */
	public RegionBase getRegion() {
		return RegionBase.REGIONS.lookup(regionId);
	}
	
	/**
	 * Returns the encounter of this stage by parsing its encounter string. Also stores the challenge rating of the encounter internally.
	 * @return the encounter of this stage
	 * @throws ParseException If the encounter string cannot be parsed.
	 * @see Encounter#parse(String)
	 */
	public Encounter createEncounter() {
		Encounter encounter = Encounter.parse(encounterString);
		RATINGS.put(id, encounter.getChallengeRating());
		return encounter;
	}
	
	/**
	 * Returns the challenge rating of this stage base. If the challenge rating is internally stored, it is returned.
	 * Else the encounter gets parsed, the challenge rating stored internally and then returned.
	 * @return the challenge rating of this stage base
	 * @throws ParseException If the encounter string cannot be parsed.
	 * @see #createEncounter()
	 * @see Encounter#parse(String)
	 */
	@Override
	public int getChallengeRating() {
		Integer rating = RATINGS.get(id);
		if (rating != null)
			return rating;
		createEncounter(); // put the challenge rating down
		return RATINGS.get(id);
	}

	/**
	 * Returns a deep copy of this stage base by calling the copy constructor.
	 * @see #StageBase(StageBase)
	 */
	@Override
	public Object clone() {
		return new StageBase(this);
	}

	/**
	 * Generates a hash code for this instance, taking into account all fields. It's guarantied that
	 * the hash codes of two stage bases are equal if the stage bases are equal.
	 * @return a hash code for this stage base
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((encounterString == null) ? 0 : encounterString.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(nextFandoms);
		result = prime * result + Arrays.hashCode(nextRegions);
		result = prime * result + Arrays.hashCode(nextStages);
		result = prime * result + ((regionId == null) ? 0 : regionId.hashCode());
		return result;
	}

	/**
	 * Checks if the specified object is the same as this stage base, i.e. the other object
	 * must be or inherit from {@link StageBase}, be the same class as the object this method is called from
	 * and all its <code>StageBase</code> fields must be the same as the fields of this stage base
	 * to return <code>true</code>. The underlying arrays of next stages, regions and fandoms are compared by values, not reference, as well.
	 * If the other object is equal to this stage base, then their hash codes return the same number, if called on <code>StageBase</code>.
	 * @param obj the object to check
	 * @return <code>true</code> if this stage base is equal to the object, else <code>false</code>.
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
		StageBase other = (StageBase) obj;
		if (encounterString == null) {
			if (other.encounterString != null)
				return false;
		} else if (!encounterString.equals(other.encounterString))
			return false;
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
		if (!Arrays.equals(nextFandoms, other.nextFandoms))
			return false;
		if (!Arrays.equals(nextRegions, other.nextRegions))
			return false;
		if (!Arrays.equals(nextStages, other.nextStages))
			return false;
		if (regionId == null) {
			if (other.regionId != null)
				return false;
		} else if (!regionId.equals(other.regionId))
			return false;
		return true;
	}
	
	/**
	 * Saves the stage base to a string builder.
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString()
	 * @see #parse(String, boolean, boolean)
	 */
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		// if changed, also change the parse function and the documentation of it
		s.append(id);
		s.append(';');
		s.append(name);
		s.append(';');
		s.append(regionId);
		s.append(';');
		Utilities.arrayToString(nextStages, s, LIST_SEPARATOR);
		s.append(';');
		Utilities.arrayToString(nextRegions, s, LIST_SEPARATOR);
		s.append(';');
		Utilities.arrayToString(nextFandoms, s, LIST_SEPARATOR);
		s.append(';');
		s.append(encounterString);
	}
	
	/**
	 * Returns the stage base as a string for saving purposes, i.e. in the form needed for {@link #parse(String)}.
	 * @return the stage base as a string for saving purposes
	 * @see #toSaveString(StringBuilder)
	 * @see #parse(String, boolean, boolean)
	 */
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	/**
	 * Returns a string representation of the stage base. This is identical to
	 * calling {@link #toSaveString()}.
	 * @return a string representation of the stage base
	 */
	@Override
	public String toString() {
		return toSaveString();
	}
	
	/**
	 * Parses a string representation of a stage base into a object.
	 * The format is<br>
	 * "<code>ID;name;regionID;nextStages;nextRegions;nextFandoms;encounterString</code>"<br>
	 * where <code>nextStages</code>, <code>nextRegions</code> and <code>nextFandoms</code> are lists of strings separated by {@value #LIST_SEPARATOR} without whitespaces,
     * and <code>encounterString</code> is the parseable string representation of a {@link Encounter}.
	 * @param s the string to parse
	 * @param register If the parsed stage should be registered in {@link #STAGES} after parsing. 
	 * Will throw an exception if this stage could be parsed successfully, but their ID is already registered there.
	 * @param checkEncounter if the encounter string should be parsed as well to see if it is valid
	 * @return a stage base matching the string
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalNumberOfArgumentsException If the number of arguments in the string separated by <code>;</code> is wrong 
	 * OR if <code>checkEncounter</code> is <code>true</code> and this exception is thrown by parsing it.
	 * @throws ParsedIdFoundException If <code>register</code> is set to <code>true</code> and the stage base string is parsed successfully (except for the encounter string), but the ID is already registered in {@link #STAGES}.
	 * @throws ParseException If <code>checkEncounter</code> is <code>true</code> and this exception is thrown by parsing it. For more information
	 * see {@link Encounter#parse(String)}.
	 * @see #StageBase(String, String, String, String[], String[], String[], String, boolean)
	 * @see #toSaveString()
	 * @see #createEncounter()
	 * @see Encounter#parse(String)
	 */
	public static StageBase parse(String s, boolean register, boolean checkEncounter) {
		Utilities.requireNonNull(s, "s");
		String[] parts = s.split(";");
		final int NUMBER_OF_ARGUMENTS = 7;
		if (parts.length != NUMBER_OF_ARGUMENTS)
			throw new IllegalNumberOfArgumentsException(String.format("Stage \"%s\" to parse contained %d arguments instead of %d", s, parts.length, NUMBER_OF_ARGUMENTS));
		try {
			StageBase result = new StageBase(parts[0], parts[1], parts[2], 
					parts[3].isEmpty() ? null : parts[3].split(String.valueOf(LIST_SEPARATOR)), 
					parts[4].isEmpty() ? null : parts[4].split(String.valueOf(LIST_SEPARATOR)), 
					parts[5].isEmpty() ? null : parts[5].split(String.valueOf(LIST_SEPARATOR)), 
					parts[6], register);
			if (checkEncounter)
				result.createEncounter();
			return result;
		}
		catch (IllegalStateException ex) {
			throw new ParsedIdFoundException(ex);
		}
	}
	
	/**
	 * Parses a string representation of a stage base into a object.
	 * The format is<br>
	 * "<code>ID;name;regionID;nextStages;nextRegions;nextFandoms;encounterString</code>"<br>
	 * where <code>nextStages</code>, <code>nextRegions</code> and <code>nextFandoms</code> are lists of strings separated by {@value #LIST_SEPARATOR} without whitespaces,
     * and <code>encounterString</code> is the parseable string representation of a {@link Encounter}.
	 * @param s the string to parse
	 * @return a stage base matching the string
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalNumberOfArgumentsException If the number of arguments in the string separated by <code>;</code> is wrong 
	 * OR if this exception is thrown by parsing the encounter string.
	 * @throws ParsedIdFoundException If the stage base string is parsed successfully (except for the encounter string), but the ID is already registered in {@link #STAGES}.
	 * @throws ParseException If this exception is thrown by parsing the encounter string. For more information see {@link Encounter#parse(String)}.
	 * @see #StageBase(String, String, String, String[], String[], String[], String, boolean)
	 * @see #toSaveString()
	 * @see #createEncounter()
	 * @see Encounter#parse(String)
	 */
	public static StageBase parse(String s) {
		return parse(s, true, true);
	}
	
	/**
	 * Calls {@link #createEncounter()} on all stages in {@link #STAGES}, causing a {@link ParseException} whenever an invalid encounter string is encountered. 
	 * Since a lot of instances are created by this call, {@link System#gc()} is called afterwards.
	 * @throws ParseException If any registered stage has an invalid encounter string.
	 * @see #createEncounter()
	 * @see Encounter#parse(String)
	 */
	public static void validateEncouterStrings() {
		for (StageBase stage : STAGES)
			stage.createEncounter();
		System.gc();
	}
	
}
