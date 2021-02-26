package org.abos.sc.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

import org.abos.sc.core.battle.Conclusion;
import org.abos.sc.core.battle.Formation;
import org.abos.util.IllegalArgumentTypeException;
import org.abos.util.ParseException;
import org.abos.util.ParsedIdNotFoundException;
import org.abos.util.Registry;
import org.abos.util.SaveString;
import org.abos.util.Utilities;

/**
 * Constitutes a player / game state.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Player implements SaveString {
	
	/**
	 * Main separator for entries on a line when saving this player / game state.
	 * @see #toSaveString(StringBuilder)
	 * @see #loadFromFile(Path)
	 */
	public static final String ENTRY_SEPARATOR = ";";
	
	/**
	 * When this game was started, mainly for speedrun purposes. Should only be non null if this game state wasn't loaded.
	 * @see #speedrunActive()
	 * @see #getRunDuration()
	 */
	protected Instant creationTime = Instant.now();
	
	/**
	 * The difficulty for this player / of this game state.
	 * @see #getDifficulty()
	 */
	protected Difficulty difficulty;
	
	/**
	 * The companions this player has access to.
	 * @see #getCompanions()
	 */
	protected final Registry<Companion> companions = new Registry<>();
	
	/**
	 * The stages this player has access to.
	 * @see #getStages()
	 * @see #updateRegionStages(boolean)
	 */
	protected final Registry<Stage> stages = new Registry<>();
	
	/**
	 * The regions this player has access to.
	 * @see #getRegions()
	 * @see #updateRegionStages(boolean)
	 * @see #updateFandomRegions(boolean)
	 */
	protected final Registry<Region> regions = new Registry<>();
	
	/**
	 * The fandoms this player has access to.
	 * @see #getFandoms()
	 * @see #updateFandomRegions(boolean)
	 */
	protected final Registry<Fandom> fandoms = new Registry<>();
	
	/**
	 * The party of this player, made up of accessable companions.
	 * @see #getParty()
	 * @see #setParty(Formation)
	 */
	protected Formation party;
	
	/**
	 * The money this player has.
	 * @see #getMoney()
	 * @see #setMoney(int)
	 * @see #addMoney(int)
	 */
	protected int money = 0;
	
	/**
	 * The amount of diamonds this player has.
	 * @see #getDiamonds()
	 * @see #setDiamonds(int)
	 * @see #addDiamonds(int)
	 */
	protected int diamonds = 0;
	
	/**
	 * Private constructor for loading purposes.
	 * @see #loadFromFile(Path)
	 */
	private Player() {}
	
	/**
	 * Creates a player with the specified difficulty and access to all fandoms and
	 * their respective start region, start stage and start companion.
	 * @param difficulty the difficulty for this player, not <code>null</code>
	 * @throws NullPointerException If <code>difficulty</code> refers to <code>null</code>.
	 * @throws IllegalStateException If any start configurations are not properly loaded, 
	 * e.g. if the start region of a registered fandom isn't a registered region OR if there are no registered fandoms.
	 * @see #createValidationPlayer()
	 */
	protected Player(Difficulty difficulty) {
		Utilities.requireNonNull(difficulty, "difficulty");
		this.difficulty = difficulty;
		if (FandomBase.FANDOMS.isEmpty())
			throw new IllegalStateException("No registered fandoms available!");
		for (FandomBase fb : FandomBase.FANDOMS) {
			assert fb != null; 
			Fandom fandom = new Fandom(fb);
			fandoms.add(fandom);
			RegionBase rb = fandom.getStartRegion();
			if (rb == null)
				throw new IllegalStateException(String.format("Region base %s is not registered!", fandom.getStartRegionId()));
			Region region = new Region(rb);
			regions.add(region);
			StageBase sb = region.getStartStage();
			if (sb == null)
				throw new IllegalStateException(String.format("Stage base %s is not registered!", region.getStartStageId()));
			Stage stage = new Stage(sb, difficulty.showChallengeRatings());
			stages.add(stage);
			CharacterBase cb = CharacterBase.CHARACTERS.lookup(fb.getStartCompanionId());
			if (cb == null)
				throw new IllegalStateException(String.format("Character base %s is not registered!", fandom.getStartCompanionId()));
			Companion companion = new Companion(cb);
			companions.add(companion);
			if (fb.getId().equals(FandomBase.DEFAULT_FANDOM_ID))
				party = Formation.createFormation(companion);
		}
		if (party == null) { // shouldn't happen, but is recoverable
			System.err.println(String.format("WARNING: Default fandom base %s is not registered!", FandomBase.DEFAULT_FANDOM_ID));
			assert !companions.isEmpty();
			party = Formation.createFormation(companions.iterator().next());
		}
		updateRegionStages(true);
		updateFandomRegions(true);
	}
	
	/**
	 * Creates a player with the specified difficulty and start fandom, as well as the
	 * respective start region, start stage and start companion.
	 * @param difficulty the difficulty for this player, not <code>null</code>
	 * @param startFandom the start fandom for this player, not <code>null</code>
	 * @throws NullPointerException If <code>difficulty</code> or <code>startFandom</code> refers to <code>null</code>.
	 * @throws IllegalStateException If the start configuration is not properly loaded, 
	 * e.g. if the start region of the start fandom isn't a registered region.
	 */
	public Player(Difficulty difficulty, FandomBase startFandom) {
		Utilities.requireNonNull(difficulty, "difficulty");
		Utilities.requireNonNull(startFandom, "startFandom");
		this.difficulty = difficulty;
		Fandom fandom = new Fandom(startFandom);
		fandoms.add(fandom);
		RegionBase rb = fandom.getStartRegion();
		if (rb == null)
			throw new IllegalStateException(String.format("Region base %s is not registered!", fandom.getStartRegionId()));
		Region region = new Region(rb);
		regions.add(region);
		StageBase sb = region.getStartStage();
		if (sb == null)
			throw new IllegalStateException(String.format("Stage base %s is not registered!", region.getStartStageId()));
		Stage stage = new Stage(sb, difficulty.showChallengeRatings());
		stages.add(stage);
		CharacterBase cb = CharacterBase.CHARACTERS.lookup(startFandom.getStartCompanionId());
		if (cb == null)
			throw new IllegalStateException(String.format("Character base %s is not registered!", fandom.getStartCompanionId()));
		Companion startCompanion = new Companion(cb);
		companions.add(startCompanion);
		updateRegionStages(true);
		updateFandomRegions(true);
		party = Formation.createFormation(startCompanion);
	}
	
	/**
	 * Returns <code>true</code> if this game state constitutes a valid speedrun.
	 * @return <code>true</code> if this game state constitutes a valid speedrun, else false.
	 * @see #getRunDuration()
	 */
	public boolean speedrunActive() {
		return creationTime != null;  // keep in sync with getRunDuration()
	}
	
	/**
	 * Returns how long this game state exists.
	 * @return the duration of this run OR <code>null</code> if this game state is not a valid speedrun.
	 */
	public Duration getRunDuration() {
		if (!speedrunActive())
			return null;
		assert creationTime != null;  // keep in sync with speedrunActive()
		return Duration.between(creationTime, Instant.now());
	}
	
	/**
	 * Returns the difficulty for this player / of this game state
	 * @return the difficulty for this player, not <code>null</code>.
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Returns the registry of accessable companions for this player. 
	 * @return the registry of accessable companions for this player as a direct reference
	 */
	public Registry<Companion> getCompanions() {
		return companions;
	}

	/**
	 * Returns the registry of accessable stages for this player. 
	 * @return the registry of accessable stages for this player as a direct reference
	 * @see #updateRegionStages(boolean)
	 */
	public Registry<Stage> getStages() {
		return stages;
	}

	/**
	 * Returns the registry of accessable regions for this player. 
	 * @return the registry of accessable regions for this player as a direct reference
	 * @see #updateRegionStages(boolean)
	 * @see #updateFandomRegions(boolean)
	 */
	public Registry<Region> getRegions() {
		return regions;
	}
	
	/**
	 * Updates the stages of every region of this player.
	 * @param removeClearedFlags if the cleared flag should be removed
	 * @see Region#updateStages(Iterable)
	 * @see Region#hasBeenCleared()
	 */
	public void updateRegionStages(boolean removeClearedFlags) {
		for (Region region : regions) {
			region.updateStages(stages);
			if (removeClearedFlags)
				region.hasBeenCleared();
		}
	}

	/**
	 * Returns the registry of accessable fandoms for this player. 
	 * @return the registry of accessable fandoms for this player as a direct reference
	 * @see #updateFandomRegions(boolean)
	 */
	public Registry<Fandom> getFandoms() {
		return fandoms;
	}
	
	/**
	 * Updates the regions of every fandom of this player.
	 * @param removeClearedFlags if the cleared flag should be removed
	 * @see Fandom#updateRegions(Iterable)
	 * @see Fandom#hasBeenCleared()
	 */
	public void updateFandomRegions(boolean removeClearedFlags) {
		for (Fandom fandom : fandoms) {
			fandom.updateRegions(regions);
			if (removeClearedFlags)
				fandom.hasBeenCleared();
		}
	}
	
	/**
	 * Returns the party of this player.
	 * @return the party of this player, made up of accessable companions
	 * @see #setParty(Formation)
	 */
	public Formation getParty() {
		return party;
	}
	
	/**
	 * Sets the party of this character. The characters in the specified party are looked
	 * up in this players companions and substituted by them (without changing <code>party</code>)
	 * before being assigned to the player.
	 * @param party the party to set, not <code>null</code>
	 * @throws NullPointerException If <code>party</code> refers to <code>null</code>
	 * @throws IllegalStateException If a member of <code>party</code> is not a registered companion of this player.
	 */
	public void setParty(Formation party) {
		Utilities.requireNonNull(party, "party");
		Companion[][] partySelection = new Companion[Formation.ROW_NUMBER][Formation.COL_NUMBER];
		Character currentChar = null;
		Companion currentComp = null;
		for (int row = 0; row < Formation.ROW_NUMBER; row++)
			for (int col = 0; col < Formation.COL_NUMBER; col++) {
				currentChar = party.getCharacter(row, col);
				if (currentChar != null) {
					currentComp = companions.lookup(currentChar.getId());
					if (currentComp == null)
						throw new IllegalStateException(String.format("%s is an invalid party member for this player!",currentChar.getId()));
					partySelection[row][col] = currentComp;
				}
			}
		this.party = new Formation(partySelection);
	}
	
	/**
	 * Adds the specified amount of extra points to each companion in this player's party.
	 * @param amount the amount of extra points to add (can be negative)
	 * @throws ClassCastException If the party of this player contains a non companion member (shouldn't happen).
	 */
	public void addExtraPointsToParty(int amount) {
		for (Character companion : party) {
			assert companion instanceof Companion;
			((Companion)companion).addExtraPoints(amount);
		}
	}
	
	/**
	 * Returns the money this player has.
	 * @return the money this player has
	 * @see #setMoney(int)
	 * @see #addMoney(int)
	 */
	public int getMoney() {
		return money;
	}
	
	/**
	 * Sets the money of this player to the specified amount.
	 * @param amount the amount of money to set
	 * @see #getMoney()
	 * @see #addMoney(int)
	 */
	public void setMoney(int amount) {
		this.money = amount;
	}
	
	/**
	 * Adds the specified amount of money to this player. Overflow is prevented, i.e. money caps at max or min integer.
	 * @param amount the amount of money to add
	 * @see #getMoney()
	 * @see #setMoney(int)
	 */
	public void addMoney(int amount) {
		this.money = Utilities.addWithoutOverflow(this.money, amount);
	}
	
	/**
	 * Returns the amount of diamonds this player has.
	 * @return the amount of diamonds this player has
	 * @see #setDiamonds(int)
	 * @see #addDiamonds(int)
	 */
	public int getDiamonds() {
		return diamonds;
	}
	
	/**
	 * Sets the amount of diamonds of this player to the specified amount.
	 * @param amount the amount of diamonds to set
	 * @see #getDiamonds()
	 * @see #addDiamonds(int)
	 */
	public void setDiamonds(int amount) {
		this.diamonds = amount;
	}
	
	/**
	 * Adds the specified amount of diamonds to this player. Overflow is prevented, i.e. diamonds cap at max or min integer.
	 * @param amount the amount of diamonds to add
	 * @see #getDiamonds()
	 * @see #setDiamonds(int)
	 */
	public void addDiamonds(int amount) {
		this.diamonds = Utilities.addWithoutOverflow(this.diamonds, amount);
	}
	
	/**
	 * Saves the player / game state to a string builder.
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString()
	 * @see #saveToFile(Path, boolean)
	 * @see #loadFromFile(Path)
	 */
	@Override
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		// if changed, also change the loadFromFile function and the documentation of it
		// creation time is explicitly not saved for speedruns
		difficulty.toSaveString(s);
		s.append(System.lineSeparator());
		Utilities.iterableToSaveString(companions, ENTRY_SEPARATOR, s);
		s.append(System.lineSeparator());
		Utilities.iterableToSaveString(stages, ENTRY_SEPARATOR, s);
		s.append(System.lineSeparator());
		Utilities.iterableToSaveString(regions, ENTRY_SEPARATOR, s);
		s.append(System.lineSeparator());
		Utilities.iterableToSaveString(fandoms, ENTRY_SEPARATOR, s);
		s.append(System.lineSeparator());
		s.append(money);
		s.append(System.lineSeparator());
		s.append(diamonds);
		s.append(System.lineSeparator());
		party.toSaveString(s);
		s.append(System.lineSeparator());
	}
	
	/**
	 * Saves this player / game state to the specified file.
	 * @param path the path to the save file
	 * @param overwrite if an existing save file should be overwritten
	 * @return <code>true</code> if the file was successfully saved, else <code>false</code>
	 * @throws IOException If an I/O error occurs.
	 */
	public boolean saveToFile(Path path, boolean overwrite) throws IOException {
		if (path.toFile().isFile() && !overwrite)
			return false;
		FileWriter fw = null;
		try {
			fw = new FileWriter(path.toFile(), Utilities.ENCODING, !overwrite);
			fw.append(toSaveString());
			return true;
		}
		finally {
			if (fw != null) try {fw.close();} catch (IOException ex) {/* ignore */}
		}
	}

	// TODO add description of how the save file looks
	/**
	 * Loads a saved player / game state from the specified file.
	 * @param path the path to the saved player / game state
	 * @return the player / game state described by the file
	 * @throws ParseException If the loaded file isn't in the correct format.
	 * @throws IOException If an I/O error occurs.
	 */
	public static Player loadFromFile(Path path) throws IOException {
		FileReader fr = null;
		BufferedReader br = null;
		String eofMsg = "Unexpected end of save file in line %d";
		// if changed, also change the parse function
		try {
			fr = new FileReader(path.toFile());
			br = new BufferedReader(fr);
			String line = null;
			Player player = new Player();
			
			if ((line = br.readLine()) == null)
				throw new ParseException(String.format(eofMsg, 1));
			player.difficulty = Difficulty.parse(line);
			if ((line = br.readLine()) == null)
				throw new ParseException(String.format(eofMsg, 2));
			for (String s : line.split(ENTRY_SEPARATOR))
				Companion.parse(s, player);
			if ((line = br.readLine()) == null)
				throw new ParseException(String.format(eofMsg, 3));
			for (String s : line.split(ENTRY_SEPARATOR))
				Stage.parse(s, player);
			if ((line = br.readLine()) == null)
				throw new ParseException(String.format(eofMsg, 4));
			for (String s : line.split(ENTRY_SEPARATOR))
				Region.parse(s, player);
			if ((line = br.readLine()) == null)
				throw new ParseException(String.format(eofMsg, 5));
			for (String s : line.split(ENTRY_SEPARATOR))
				Fandom.parse(s, player);
			
			player.updateFandomRegions(true);
			player.updateRegionStages(true);
			
			if ((line = br.readLine()) == null)
				throw new ParseException(String.format(eofMsg, 6));
			try {
				player.setMoney(Integer.parseInt(line));
			}
			catch (NumberFormatException ex) {
				throw new IllegalArgumentTypeException(String.format("Amount of money for player was invalid: %s", line),ex);
			}
			if ((line = br.readLine()) == null)
				throw new ParseException(String.format(eofMsg, 7));
			try {
				player.setDiamonds(Integer.parseInt(line));
			}
			catch (NumberFormatException ex) {
				throw new IllegalArgumentTypeException(String.format("Amount of diamonds for player was invalid: %s", line),ex);
			}
			if ((line = br.readLine()) == null)
				throw new ParseException(String.format(eofMsg, 8));
			try {
				player.setParty(Formation.parse(line));
			} 
			catch (IllegalStateException ex) {
				throw new ParsedIdNotFoundException(String.format("At least one party member was inaccessable for the player: %s", line),ex);
			}
			
			// make loaded save states illegal for speedruns
			player.creationTime = null;
			
			return player;
		}
		finally {
			if (br != null) try {br.close();} catch (IOException ex) {/* ignore */}
			if (fr != null) try {fr.close();} catch (IOException ex) {/* ignore */}
		}
	}

	/**
	 * Creates a player on default difficulty with access to the default fandom.
	 * @return a player on default difficulty with access to the default fandom
	 * @throws IllegalStateException If the default fandom is not registered yet.
	 * @see Difficulty#DEFAULT
	 * @see FandomBase#DEFAULT_FANDOM_ID
	 */
	public static Player createNewDefaultPlayer() {
		FandomBase fandom = FandomBase.FANDOMS.lookup(FandomBase.DEFAULT_FANDOM_ID);
		if (fandom == null)
			throw new IllegalStateException(String.format("Fandom base %s is not registered!", FandomBase.DEFAULT_FANDOM_ID));
		return new Player(Difficulty.DEFAULT, fandom);
	}
	
	/**
	 * Creates a player on default difficulty with access to all fandoms and their respective start characters. 
	 * @return a player on default difficulty with access to all fandoms and their respective start characters
	 * @see Difficulty#DEFAULT
	 */
	public static Player createValidationPlayer() {
		return new Player(Difficulty.DEFAULT);
	}
	
	/**
	 * Checks for accessability of characters, stages and regions and for which characters images are missing.
	 * @return The evaluation as a string.
	 */
	public static String validateGameData() {
		Player player = createValidationPlayer();
		Registry<Companion> companions = new Registry<>(player.companions);
//		Registry<Fandom> fandoms = new Registry<>(player.fandoms);
		Registry<Region> regions = new Registry<>(player.regions);
		Registry<Stage> uncheckedStages = new Registry<>();
		Registry<Stage> checkedStages = new Registry<>();
		for (Stage stage : player.stages)
			uncheckedStages.add((Stage)stage.clone());
		// collect all theoretically reachable stages etc via their IDs
		Stage currentStage = null;
		while (!uncheckedStages.isEmpty()) {
			currentStage = uncheckedStages.iterator().next();
			currentStage.engageStage();
			for (Companion companion : currentStage.rewardCompanions(Conclusion.WON)) {
				if (!companions.containsId(companion.getId()))
					companions.add(companion);
			}
//			for (Fandom fandom : currentStage.rewardFandoms(Conclusion.WON)) {
//				if (!fandoms.containsId(fandom.getId()))
//					fandoms.add(fandom);
//			}
			for (Region region : currentStage.rewardRegions(Conclusion.WON)) {
				if (!regions.containsId(region.getId()))
					regions.add(region);
			}
			for (Stage stage : currentStage.rewardStages(Conclusion.WON, player.getDifficulty().showChallengeRatings())) {
				if (!uncheckedStages.containsId(stage.getId()) && !checkedStages.containsId(stage.getId()))
					uncheckedStages.add(stage);
			}
			currentStage.disengageStage();
			uncheckedStages.removeById(currentStage.getId());
			checkedStages.add(currentStage);
		}
		// get the remaining entries 
		Registry<CharacterBase> unreachableCharacters = new Registry<>();
//		Registry<FandomBase> unreachableFandoms = new Registry<>();
		Registry<RegionBase> unreachableRegions = new Registry<>();
		Registry<StageBase> unreachableStages = new Registry<>();
		for (CharacterBase character : CharacterBase.CHARACTERS)
			if (!companions.containsId(character.getId()))
				unreachableCharacters.add(character);
//		for (FandomBase fandom : FandomBase.FANDOMS)
//			if (!fandoms.containsId(fandom.getId()))
//				unreachableFandoms.add(fandom);
		for (RegionBase region : RegionBase.REGIONS)
			if (!regions.containsId(region.getId()))
				unreachableRegions.add(region);
		for (StageBase stage : StageBase.STAGES)
			if (!checkedStages.containsId(stage.getId()))
				unreachableStages.add(stage);
		// put it in a string
		StringBuilder s = new StringBuilder();
		s.append(String.format("%d characters out of %d unreachable: %s", 
				unreachableCharacters.size(), CharacterBase.CHARACTERS.size(), unreachableCharacters));
		s.append(System.lineSeparator());
//		s.append(String.format("%d fandoms out of %d unreachable: %s", 
//				unreachableFandoms.size(), FandomBase.FANDOMS.size(), unreachableFandoms));
		s.append(System.lineSeparator());
		s.append(String.format("%d regions out of %d unreachable: %s", 
				unreachableRegions.size(), RegionBase.REGIONS.size(), unreachableRegions));
		s.append(System.lineSeparator());
		s.append(String.format("%d stages out of %d unreachable: %s", 
				unreachableStages.size(), StageBase.STAGES.size(), unreachableStages));
		s.append(System.lineSeparator());
		return s.toString();
	}

}
