package org.abos.sc.core;

import java.util.Arrays;

import org.abos.util.Id;
import org.abos.util.IdCloneable;
import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.Name;
import org.abos.util.ParseException;
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
public class StageBase implements IdCloneable, Name {
	
	/**
	 * separator character for the lists
	 * @see #parse(String, boolean)
	 */
	public final static char LIST_SEPARATOR = ',';

	/**
	 * a registry of all globally available stages
	 */
	public static final Registry<StageBase> STAGES = new Registry<>();
	
	/**
	 * the ID of this stage
	 * @see #getId()
	 */
	protected final String id;
	
	/**
	 * the name of this stage
	 * @see #getName()
	 */
	protected final String name;
	
	/**
	 * the region ID of this stage
	 * @see #getRegionId()
	 * @see #getRegion()
	 */
	protected final String regionId;
	
	/**
	 * stages that become available through this one
	 */
	protected final String[] nextStages;

	/**
	 * regions that become available through this stage
	 */
	protected final String[] nextRegions;
	
	/**
	 * fandoms that become available through this stage
	 */
	protected final String[] nextFandoms;
	
	/**
	 * the encoded encounter of this stage
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
	 * @param register if this stage should be registered in {@link StageBase#STAGES} or not
	 * @throws NullPointerException If any one of <code>id</code>, <code>name</code>, 
	 * <code>regionId</code> or <code>encounterString</code> refers to <code>null</code>.
	 * @throws IllegalArgumentException If <code>register</code> is <code>true</code> but a stage
	 * with ID <code>id</code> is already registered in {@link StageBase#STAGES}.
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
	 * Returns the encounter of this stage by parsing its encounter string.
	 * @return the encounter of this stage
	 * @throws ParseException If the encounter string cannot be parsed.
	 * @see BattleEncounter#parse(String)
	 */
	public BattleEncounter createEncounter() {
		return BattleEncounter.parse(encounterString);
	}
	
	@Override
	public Object clone() {
		return new StageBase(this);
	}

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
	
	public void toSaveString(StringBuilder s) {
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
	
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	@Override
	public String toString() {
		return "StageBase [id=" + id + ", name=" + name + ", regionId=" + regionId + ", nextStages="
				+ Arrays.toString(nextStages) + ", nextRegions=" + Arrays.toString(nextRegions) + ", nextMaps="
				+ Arrays.toString(nextFandoms) + ", encounterString=" + encounterString + "]";
	}
	
	public static StageBase parse(String s, boolean register, boolean checkEncounter) {
		Utilities.requireNonNull(s, "s");
		String[] parts = s.split(";");
		final int NUMBER_OF_ARGUMENTS = 7;
		if (parts.length != NUMBER_OF_ARGUMENTS)
			throw new IllegalNumberOfArgumentsException(String.format("Stage \"%s\" to parse contained %d arguments instead of %d", s, parts.length, NUMBER_OF_ARGUMENTS));
		StageBase result = new StageBase(parts[0], parts[1], parts[2], 
				parts[3].isEmpty() ? null : parts[3].split(String.valueOf(LIST_SEPARATOR)), 
				parts[4].isEmpty() ? null : parts[4].split(String.valueOf(LIST_SEPARATOR)), 
				parts[5].isEmpty() ? null : parts[5].split(String.valueOf(LIST_SEPARATOR)), 
				parts[6], register);
		if (checkEncounter)
			result.createEncounter();
		return result;
	}
	
	public static StageBase parse(String s) {
		return parse(s, true, true);
	}
	
	/**
	 * Calls {@link #createEncounter()} on all stages in {@link #STAGES}, causing a {@link ParseException} whenever an invalid encounter string is encountered. 
	 * Since a lot of instances are created by this call, {@link System#gc()} is called afterwards.
	 * @throws ParseException If any registered stage has an invalid encounter string.
	 * @see #createEncounter()
	 * @see BattleEncounter#parse(String)
	 */
	public static void validateEncouterStrings() {
		for (StageBase stage : STAGES)
			stage.createEncounter();
		System.gc();
	}
	
}
