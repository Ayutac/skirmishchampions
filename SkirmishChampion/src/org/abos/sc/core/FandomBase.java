package org.abos.sc.core;

import java.util.ArrayList;
import java.util.List;

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
public class FandomBase implements Cloneable, Id, Name {
	
	public static final Registry<FandomBase> FANDOMS = new Registry<>();
	
	protected final String id;
	
	protected final String name;
	
	protected final String startRegionId;
	
	protected final String startCompanionId;
	
	/**
	 * List of characters in this fandom for sorting purposes.
	 * This list may only contain one character per ID, and only these of this fandom.
	 * This list is <code>null</code>-free.
	 */
	protected final List<CharacterBase> characters;
	
	/**
	 * @param id
	 * @param name
	 * @param startRegionId
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
		characters = new ArrayList<>();
		if (register)
			FANDOMS.add(this);
	}
	
	/**
	 * 
	 */
	public FandomBase(FandomBase fandom) {
		this(fandom.id, fandom.name, fandom.startRegionId, fandom.startCompanionId, false);
		characters.addAll(fandom.characters);
	}

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
	 * @return the startRegionId
	 */
	public String getStartRegionId() {
		return startRegionId;
	}
	
	/**
	 * @return the startCompanionId
	 */
	public String getStartCompanionId() {
		return startCompanionId;
	}
	
	protected boolean addCharacter(CharacterBase character) {
		Utilities.requireNonNull(character, "character");
		if (!character.getFandomId().equals(id))
			throw new IllegalArgumentException("character "+character.getId()+" doesn't belong to this fandom!");
		for (CharacterBase entry : characters)
			if (character.getId().equals(entry.getId()))
				throw new IllegalArgumentException("Only one entry per ID is permitted! "+character.getId()+" is already used!");
		return characters.add(character);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Fandom(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((startRegionId == null) ? 0 : startRegionId.hashCode());
		result = prime * result + ((characters == null) ? 0 : characters.hashCode());
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
		if (characters == null) {
			if (other.characters != null)
				return false;
		} else if (!characters.equals(other.id))
			return false;
		return true;
	}
	
	public void toSaveString(StringBuilder s) {
		s.append("id"); // throws NPE
		s.append(';');
		s.append(name);
		s.append(';');
		s.append(startRegionId);
		s.append(';');
		s.append(startCompanionId);
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
	
	public static FandomBase parse(String s, boolean register) {
		Utilities.requireNonNull(s, "s");
		String[] parts = s.split(";");
		final int NUMBER_OF_ARGUMENTS = 4;
		if (parts.length != NUMBER_OF_ARGUMENTS)
			throw new IllegalNumberOfArgumentsException(String.format("Fandom \"%s\" to parse contained %d arguments instead of %d", s, parts.length, NUMBER_OF_ARGUMENTS));
		return new FandomBase (parts[0], parts[1], parts[2], parts[3], register);
	}
	
	public static FandomBase parse(String s) {
		return parse(s, true);
	}

}
