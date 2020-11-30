package org.abos.sc.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.abos.util.IllegalArgumentTypeException;
import org.abos.util.Registry;
import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Player {
	
	protected Registry<Companion> companions = new Registry<>();
	
	protected Registry<Stage> stages = new Registry<>();
	
	protected Registry<Region> regions = new Registry<>();
	
	protected Registry<Fandom> fandoms = new Registry<>();
	
	protected int money = 0;
	
	protected int diamonds = 0;
	
	private Player() {}
	
	protected Player(FandomBase startFandom) {
		Utilities.requireNonNull(startFandom, "startFandom");
		Fandom fandom = new Fandom(startFandom);
		fandoms.add(fandom);
		Region region = new Region(RegionBase.REGIONS.lookup(fandom.getStartRegionId()));
		regions.add(region);
		Stage stage = new Stage(StageBase.STAGES.lookup(region.getStartStageId()));
		stages.add(stage);
		updateRegionStages();
		updateFandomRegions();
	}
	
	public Player(FandomBase startFandom, Companion startCompanion) {
		this(startFandom);
		Utilities.requireNonNull(startCompanion, "startCompanion");
		companions.add(startCompanion);
	}

	public Player(FandomBase startFandom, Registry<? extends Companion> startCompanions) {
		this(startFandom);
		Utilities.requireNonNull(startCompanions, "startCompanions");
		if (startCompanions.isEmpty())
			throw new IllegalArgumentException("At least one start companion must be given!");
		companions.addAll(startCompanions);
	}
	
	public Registry<Companion> getCompanions() {
		return companions;
	}

	public Registry<Stage> getStages() {
		return stages;
	}

	public Registry<Region> getRegions() {
		return regions;
	}
	
	public void updateRegionStages() {
		for (Region region : regions)
			region.updateStages(stages);
	}

	public Registry<Fandom> getFandoms() {
		return fandoms;
	}
	
	public void updateFandomRegions() {
		for (Fandom fandom : fandoms)
			fandom.updateRegions(regions);
	}
	
	/**
	 * @return the money
	 */
	public int getMoney() {
		return money;
	}
	
	/**
	 * @param amount the amount of money to set
	 */
	public void setMoney(int amount) {
		this.money = amount;
	}
	
	public void addMoney(int amount) {
		this.money += amount;
	}
	
	/**
	 * @return the diamonds
	 */
	public int getDiamonds() {
		return diamonds;
	}
	
	/**
	 * @param amount the diamonds to set
	 */
	public void setDiamonds(int amount) {
		this.diamonds = amount;
	}
	
	public void addDiamonds(int amount) {
		this.diamonds += amount;
	}
	
	protected void companionsToSaveString(StringBuilder s) {
		for (Companion companion : companions) {
			companion.toSaveString(s);
			s.append(";");
		}
	}
	
	protected void stagesToSaveString(StringBuilder s) {
		for (Stage stage : stages) {
			stage.toSaveString(s);
			s.append(";");
		}
	}
	
	protected void regionsToSaveString(StringBuilder s) {
		for (Region region : regions) {
			region.toSaveString(s);
			s.append(";");
		}
	}
	
	protected void fandomsToSaveString(StringBuilder s) {
		for (Fandom fandom : fandoms) {
			fandom.toSaveString(s);
			s.append(";");
		}
	}
	
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		companionsToSaveString(s);
		s.append(System.lineSeparator());
		stagesToSaveString(s);
		s.append(System.lineSeparator());
		regionsToSaveString(s);
		s.append(System.lineSeparator());
		fandomsToSaveString(s);
		s.append(System.lineSeparator());
		s.append(money);
		s.append(System.lineSeparator());
		s.append(diamonds);
		s.append(System.lineSeparator());
	}
	
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}
	
	public boolean saveToFile(Path path, boolean overwrite) throws IOException {
		if (path.toFile().isFile() && !overwrite)
			return false;
		FileWriter fw = new FileWriter(path.toFile(), Utilities.ENCODING, !overwrite);
		fw.append(toSaveString());
		fw.close();
		return true;
	}
	
	public static Player loadFromFile(Path path) throws IOException {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(path.toFile());
			br = new BufferedReader(fr);
			String line = null;
			Player player = new Player();
			line = br.readLine();
			for (String s : line.split(";"))
				Companion.parse(s, player);
			line = br.readLine();
			for (String s : line.split(";"))
				Stage.parse(s, player);
			line = br.readLine();
			for (String s : line.split(";"))
				Region.parse(s, player);
			line = br.readLine();
			for (String s : line.split(";"))
				Fandom.parse(s, player);
			line = br.readLine();
			try {
				player.setMoney(Integer.parseInt(line));
			}
			catch (NumberFormatException ex) {
				throw new IllegalArgumentTypeException(String.format("Amount of money for player was invalid: %s", line),ex);
			}
			line = br.readLine();
			try {
				player.setDiamonds(Integer.parseInt(line));
			}
			catch (NumberFormatException ex) {
				throw new IllegalArgumentTypeException(String.format("Amount of diamonds for player was invalid: %s", line),ex);
			}
			player.updateFandomRegions();
			player.updateRegionStages();
			return player;
		}
		finally {
			if (br != null) try {br.close();} catch (IOException ex) {/* ignore */}
			if (fr != null) try {br.close();} catch (IOException ex) {/* ignore */}
		}
	}

}
