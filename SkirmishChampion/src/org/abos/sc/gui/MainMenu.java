package org.abos.sc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.Companion;
import org.abos.sc.core.ConfigManager;
import org.abos.sc.core.Difficulty;
import org.abos.sc.core.FandomBase;
import org.abos.sc.core.Player;
import org.abos.sc.core.RegionBase;
import org.abos.sc.core.StageBase;
import org.abos.util.ParseException;
import org.abos.util.Utilities;
import org.abos.util.gui.GUIUtilities;
import org.abos.util.gui.ImagePanel;
import org.abos.util.gui.TextAreaFrame;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class MainMenu extends JFrame {

	public final static String TITLE = "Skirmish Champion";
	
	public final static Dimension TITLE_SCREEN_DIMENSION = new Dimension(325, 325);
	
	// the code assumes that this is in lowercase
	public final static String SAVE_GAME_EXT = "sav";
	
	public final static String SAVE_GAME_EXT_D = "." + SAVE_GAME_EXT;
	
	protected Player player;
	
	protected StageSelectionFrame stageSelectionFrame;
	
	protected JPanel titleScreen;
	
	protected JButton newGameButton;
	
	protected JButton continueGameButton;
	
	protected JButton saveGameButton;
	
	protected JButton loadGameButton;
	
	protected JFileChooser saveGameChooser;
	
	protected TextAreaFrame creditsFrame;
	
	protected JButton creditsButton;
	
	protected TextAreaFrame licenseFrame;
	
	protected JButton licenseButton;
	
	protected JButton exitButton;
	
	/**
	 * @throws HeadlessException
	 */
	public MainMenu() throws HeadlessException {
		super(TITLE);
		initComponents();
		initLayout();
		if (!GUIUtilities.LOGOS.isEmpty())
			setIconImages(GUIUtilities.LOGOS);
	}
	
	public static Player createNewPlayer() {
		return new Player(Difficulty.EASY, FandomBase.FANDOMS.lookup("twi"), new Companion(CharacterBase.CHARACTERS.lookup("twi_erin")));
		// TODO maybe better default? 
	}
	
	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		Utilities.requireNonNull(player, "player");
		this.player = player;
		stageSelectionFrame.setPlayer(player);
	}
	
	public void newGame() {
		setPlayer(createNewPlayer());
		continueGameButton.setEnabled(true);
		continueGame();
	}
	
	public void continueGame() {
		setVisible(false);
		stageSelectionFrame.setVisible(true);
	}
	
	public void saveGame() {
		if (saveGameChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				Path saveGame = saveGameChooser.getSelectedFile().toPath();
				// if the filter for save games is selected, automatically add the extension if missing
				if (!GUIUtilities.isAcceptAllFilterSelected(saveGameChooser)) {
					String path = saveGame.toString();
					if (!path.toLowerCase().endsWith(SAVE_GAME_EXT_D))
						saveGame = Path.of(path+SAVE_GAME_EXT_D);
				}
				player.saveToFile(saveGame, true);
				ConfigManager.setProperty(ConfigManager.LAST_SAVE_LOCATION, saveGame.toString());
				try {ConfigManager.saveConfig();} catch (IOException ex) {/* ignore */}
				JOptionPane.showMessageDialog(this, "Saved game!", "Saving...", JOptionPane.INFORMATION_MESSAGE);
			}
			catch (IOException ex) {
				GUIUtilities.errorMessage(this, "Saving...", "Saving game failed!", ex);
			}
		}
	}
	
	public void loadGame() {
		if (saveGameChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			Path saveGame = saveGameChooser.getSelectedFile().toPath();
			Player loadedPlayer = null;
			try {
				loadedPlayer = Player.loadFromFile(saveGame);
			}
			catch (IOException ex) {
				GUIUtilities.errorMessage(this, "Loading...", "Loading save game failed!", ex);
			}
			if (loadedPlayer != null) {
				setPlayer(loadedPlayer);
				continueGameButton.setEnabled(true);
				ConfigManager.setProperty(ConfigManager.LAST_SAVE_LOCATION, saveGame.toString());
				try {ConfigManager.saveConfig();} catch (IOException ex) {/* ignore */}
				JOptionPane.showMessageDialog(this, "Loaded save game!", "Loading...", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	@Override
	public void dispose() {
		creditsFrame.dispose();
		licenseFrame.dispose();
		super.dispose();
	}

	/**
	 * 
	 */
	private void initComponents() {
		boolean applicationPath = true;
		try {
			titleScreen = new ImagePanel(GUIUtilities.getTitleScreenPath(), TITLE_SCREEN_DIMENSION);
		} catch (IllegalStateException ex) {
			applicationPath = false;
			GUIUtilities.errorMessage("Utility Failure", "Application path not found, no image loaded!", ex);
		} catch (IOException ex) {
			GUIUtilities.errorMessage("Image Loading Failure", "Title screen image couldn't be loaded!", ex);
		}
		if (titleScreen == null) { // fallback screen
			titleScreen = new JPanel();
			// following line is explicitly not done in initLayout to avoid confusion 
			titleScreen.setPreferredSize(TITLE_SCREEN_DIMENSION);
		}
		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(e -> newGame());
		continueGameButton = new JButton("Continue");
		continueGameButton.addActionListener(e -> continueGame());
		String lastSaveLocation = ConfigManager.getProperty(ConfigManager.LAST_SAVE_LOCATION);
		File lastSaveLocationFile = null;
		if (lastSaveLocation == null) {
			continueGameButton.setEnabled(false);
			player = createNewPlayer();
		}
		else {
			lastSaveLocationFile = new File(lastSaveLocation);
			if (!lastSaveLocationFile.isFile()) {
				continueGameButton.setEnabled(false);
				player = createNewPlayer();
			}
			else {
				try {
					player = Player.loadFromFile(lastSaveLocationFile.toPath());
				}
				catch (ParseException ex) {
					player = createNewPlayer();
					continueGameButton.setEnabled(false);
				}
				catch (IOException ex) {
					player = createNewPlayer();
					continueGameButton.setEnabled(false);
				}
			}
		}
		saveGameButton = new JButton("Save Game");
		saveGameButton.addActionListener(e -> saveGame());
		loadGameButton = new JButton("Load Game");
		loadGameButton.addActionListener(e -> loadGame());
		creditsFrame = new TextAreaFrame("Credits");
		if (applicationPath)
			creditsFrame.setTextFilePath(Utilities.getApplicationDirectory().resolve("credits.txt"));
		else
			creditsFrame.setText("Application path not found, no credits loaded!");
		creditsButton = new JButton("Credits");
		creditsButton.addActionListener(e -> creditsFrame.setVisible(true));
		licenseFrame = new TextAreaFrame("License");
		if (applicationPath)
			licenseFrame.setTextFilePath(Utilities.getApplicationDirectory().resolve("license.txt"));
		else
			licenseFrame.setText("Application path not found, no license loaded!"+System.lineSeparator()+
					"(Note that this game is licensed nonetheless.)");
		licenseButton = new JButton("License");
		licenseButton.addActionListener(e -> licenseFrame.setVisible(true));
		exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> System.exit(0));
		stageSelectionFrame = new StageSelectionFrame(player, true);
		stageSelectionFrame.setAfterHiding(() -> setVisible(true));
		if (applicationPath) {
			saveGameChooser = new JFileChooser(Utilities.getApplicationDirectory().toFile());
			saveGameChooser.setFileFilter(new FileNameExtensionFilter("Skirmish Champion save game", SAVE_GAME_EXT));
		}
		else {
			saveGameButton.setEnabled(false);
			loadGameButton.setEnabled(false);
			GUIUtilities.errorMessage("Utility Failure", "Application path not found, saving/loading disabled!", null);
		}
	}
	
	private void initLayout() {
		setLayout(new BorderLayout());
		add(titleScreen, BorderLayout.PAGE_START);
		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		buttonPanel.add(newGameButton);
		buttonPanel.add(continueGameButton);
		buttonPanel.add(saveGameButton);
		buttonPanel.add(loadGameButton);
		buttonPanel.add(creditsButton);
		buttonPanel.add(licenseButton);
		buttonPanel.add(exitButton);
		add(buttonPanel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		try {
			Utilities.loadApplicationDirectory();
		} catch (IOException ex) {
			GUIUtilities.errorMessage("Utility Failure", "Could access path to the application directory!", ex);
		}
		try {
			ConfigManager.loadConfig();
		}
		catch (IllegalStateException ex) {
			GUIUtilities.errorMessage("Utility Failure", "Application path not found, config not loaded!", ex);
		}
		catch (IOException ex) {
			GUIUtilities.errorMessage("Config Failure", "Config couldn't be loaded!", ex);
		}
		String current = "";
		try {
			Utilities.checkApplicationDirectory();
			Path path = Utilities.getApplicationDirectory().resolve("resources");
			for (Path file : Files.newDirectoryStream(path.resolve("characters"))) {
				current = file.toString();
				Utilities.loadFromFile(file, CharacterBase::parse);
			}
			for (Path file : Files.newDirectoryStream(path.resolve("stages"))) {
				current = file.toString();
				Utilities.loadFromFile(file, StageBase::parse);
			}
			for (Path file : Files.newDirectoryStream(path.resolve("regions"))) {
				current = file.toString();
				Utilities.loadFromFile(file, RegionBase::parse);
			}
			for (Path file : Files.newDirectoryStream(path.resolve("fandoms"))) {
				current = file.toString();
				Utilities.loadFromFile(file, FandomBase::parse);
			}
		}
		catch (IllegalStateException ex) {
			GUIUtilities.errorMessage("Startup Failure", "Application path not found, game files not loaded!", ex);
			return;
		}
		catch (IOException ex) {
			GUIUtilities.errorMessage("Startup Failure", "File "+current+" couldn't be loaded!", ex);
			return;
		}
		catch (ParseException ex) {
			GUIUtilities.errorMessage("Startup Failure", "File "+current+" seems to be invalid!", ex);
			return;
		}
		try {
			GUIUtilities.loadLogos();
		}
		catch (IllegalStateException ex) {
			GUIUtilities.errorMessage("Utility Failure", "Application path not found, no logos loaded!", ex);
		}
		catch (IOException ex) {
			GUIUtilities.LOGOS.clear();
			GUIUtilities.errorMessage("Logo Loading Failure", "The logos couldn't be loaded!", ex);
		}
		ToolTipManager.sharedInstance().setInitialDelay(300);
		MainMenu game = new MainMenu();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
		// TODO this needs to be put somewhere else:
		System.out.print(Player.validateGameData(createNewPlayer()));
	}

}
