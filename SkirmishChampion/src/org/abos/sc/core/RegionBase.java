package org.abos.sc.core;

import java.util.Set;
import java.util.stream.Collectors;

import org.abos.util.IdCloneable;
import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.Name;
import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class RegionBase implements IdCloneable, Name {
	
	public static final Registry<RegionBase> REGIONS = new Registry<>();
	
	protected final String id;
	
	protected final String name;
	
	protected final String fandomId;
	
	protected final String startStageId;
	
	/**
	 * @param id
	 * @param name
	 * @param startStageId
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
	 * 
	 */
	public RegionBase(RegionBase region) {
		this(region.id, region.name, region.fandomId, region.startStageId, false);
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * @return the fandomId
	 */
	public String getFandomId() {
		return fandomId;
	}

	public FandomBase getFandom() {
		return FandomBase.FANDOMS.lookup(fandomId);
	}
	
	/**
	 * @return the startStageId
	 */
	public String getStartStageId() {
		return startStageId;
	}
	
	public StageBase getStartStage() {
		return StageBase.STAGES.lookup(startStageId);
	}
	
	public Set<String> collectAssociatedStageIds() {
		return StageBase.STAGES.stream()
				.filter(base -> id.equals(base.getRegionId()))
				.map(base -> base.getId())
				.collect(Collectors.toSet());
	}
	
	@Override
	public Object clone() {
		return new RegionBase(this);
	}

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
	
	public void toSaveString(StringBuilder s) {
		s.append("id"); // throws NPE
		s.append(';');
		s.append(name);
		s.append(';');
		s.append(fandomId);
		s.append(';');
		s.append(startStageId);
	}
	
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	@Override
	public String toString() {
		return toSaveString();
	}
	
	public static RegionBase parse(String s, boolean register) {
		Utilities.requireNonNull(s, "s");
		String[] parts = s.split(";");
		final int NUMBER_OF_ARGUMENTS = 4;
		if (parts.length != NUMBER_OF_ARGUMENTS)
			throw new IllegalNumberOfArgumentsException(String.format("Region \"%s\" to parse contained %d arguments instead of %d", s, parts.length, NUMBER_OF_ARGUMENTS));
		return new RegionBase(parts[0], parts[1], parts[2], parts[3], register);
	}
	
	public static RegionBase parse(String s) {
		return parse(s, true);
	}

}
