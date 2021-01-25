package org.abos.sc.core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.abos.util.Id;
import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.Name;
import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class StageBase implements Cloneable, Id, Name {
	
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
	 */
	protected final String encounterString;
	
	/**
	 * @param id
	 * @param name
	 * @param regionId
	 * @param nextStages
	 * @param nextRegions
	 * @param nextFandoms
	 * @param encounterString
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
			this.nextStages = nextStages;
		if (nextRegions == null)
			this.nextRegions = new String[0];
		else
			this.nextRegions = nextRegions;
		if (nextFandoms == null)
			this.nextFandoms = new String[0];
		else
			this.nextFandoms = nextFandoms;
		this.encounterString = encounterString;
		if (register)
			STAGES.add(this);
	}
	
	public StageBase(StageBase stage) {
		// throws NPE
		this(stage.id, stage.name, stage.regionId, stage.nextStages, stage.nextRegions, stage.nextFandoms, stage.encounterString, false);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getRegionId() {
		return regionId;
	}
	
	public RegionBase getRegion() {
		return RegionBase.REGIONS.lookup(regionId);
	}
	
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
	
	public static StageBase parse(String s, boolean register) {
		Utilities.requireNonNull(s, "s");
		String[] parts = s.split(";");
		final int NUMBER_OF_ARGUMENTS = 7;
		if (parts.length != NUMBER_OF_ARGUMENTS)
			throw new IllegalNumberOfArgumentsException(String.format("Stage \"%s\" to parse contained %d arguments instead of %d", s, parts.length, NUMBER_OF_ARGUMENTS));
		return new StageBase(parts[0], parts[1], parts[2], 
				parts[3].isEmpty() ? null : parts[3].split(String.valueOf(LIST_SEPARATOR)), 
				parts[4].isEmpty() ? null : parts[4].split(String.valueOf(LIST_SEPARATOR)), 
				parts[5].isEmpty() ? null : parts[5].split(String.valueOf(LIST_SEPARATOR)), 
				parts[6], register);
	}
	
	public static StageBase parse(String s) {
		return parse(s,true);
	}
	
}
