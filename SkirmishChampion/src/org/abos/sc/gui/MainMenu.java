package org.abos.sc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.Companion;
import org.abos.sc.core.ConfigManager;
import org.abos.sc.core.FandomBase;
import org.abos.sc.core.ParseException;
import org.abos.sc.core.Player;
import org.abos.sc.core.RegionBase;
import org.abos.sc.core.StageBase;
import org.abos.sc.core.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class MainMenu extends JFrame {

	public final static String TITLE = "Skirmish Champion";
	
	protected Player player;
	
	protected StageSelectionFrame stageSelectionFrame;
	
	protected ImagePanel titleScreen;
	
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
	
	public Player getNewPlayer() {
		return new Player(FandomBase.FANDOMS.lookup("twi"), new Companion(CharacterBase.CHARACTERS.lookup("twi_erin")));
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
		setPlayer(getNewPlayer());
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

	private void initComponents() {
		try {
			titleScreen = new ImagePanel(GUIUtilities.getTitleScreenPath(), new Dimension(325, 325));
		} catch (IOException ex) {
			GUIUtilities.errorMessage("Image Loading Failure", "Title screen image couldn't be loaded!", ex);
		}
		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(e -> newGame());
		continueGameButton = new JButton("Continue");
		continueGameButton.addActionListener(e -> continueGame());
		String lastSaveLocation = ConfigManager.getProperty(ConfigManager.LAST_SAVE_LOCATION);
		File lastSaveLocationFile = null;
		if (lastSaveLocation == null) {
			continueGameButton.setEnabled(false);
			player = getNewPlayer();
		}
		else {
			lastSaveLocationFile = new File(lastSaveLocation);
			if (!lastSaveLocationFile.isFile()) {
				continueGameButton.setEnabled(false);
				player = getNewPlayer();
			}
			else {
				try {
					player = Player.loadFromFile(lastSaveLocationFile.toPath());
				}
				catch (ParseException ex) {
					player = getNewPlayer();
					continueGameButton.setEnabled(false);
				}
				catch (IOException ex) {
					player = getNewPlayer();
					continueGameButton.setEnabled(false);
				}
			}
		}
		saveGameButton = new JButton("Save Game");
		saveGameButton.addActionListener(e -> saveGame());
		loadGameButton = new JButton("Load Game");
		loadGameButton.addActionListener(e -> loadGame());
		creditsFrame = new TextAreaFrame("Credits");
		try {
			creditsFrame.setTextPath(Utilities.getApplicationDirectory().resolve("credits.txt"));
		}
		catch (IOException ex) {
			creditsFrame.setText(ex.toString());
		}
		creditsButton = new JButton("Credits");
		creditsButton.addActionListener(e -> creditsFrame.setVisible(true));
		licenseFrame = new TextAreaFrame("License");
		try {
			licenseFrame.setTextPath(Utilities.getApplicationDirectory().resolve("license.txt"));
		}
		catch (IOException ex) {
			licenseFrame.setText(ex.toString());
		}
		licenseButton = new JButton("License");
		licenseButton.addActionListener(e -> licenseFrame.setVisible(true));
		exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> System.exit(0));
		stageSelectionFrame = new StageSelectionFrame(player, true);
		stageSelectionFrame.setAfterHiding(() -> setVisible(true));
		try {
			saveGameChooser = new JFileChooser(Utilities.getApplicationDirectory().toFile());
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Save Game access is broken!", "Error", JOptionPane.ERROR_MESSAGE);
			saveGameChooser = null;
		}
		if (saveGameChooser == null) {
			saveGameButton.setEnabled(false);
			loadGameButton.setEnabled(false);
		}
		else {
			saveGameChooser.setFileFilter(new FileNameExtensionFilter("Skirmish Champion save game", "sav"));
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
			ConfigManager.loadConfig();
		}
		catch (IOException ex) {
			GUIUtilities.errorMessage("Config Failure", "Config couldn't be loaded!", ex);
		}
		try {
			Path path = Utilities.getBinaryDirectory().resolveSibling("resources");
			Utilities.loadFromFile(path.resolve("chars_twi.txt"), CharacterBase::parse);
			Utilities.loadFromFile(path.resolve("stages_twi.txt"), StageBase::parse);
			Utilities.loadFromFile(path.resolve("regions_twi.txt"), RegionBase::parse);
			Utilities.loadFromFile(path.resolve("fandoms.txt"), FandomBase::parse);
		}
		catch (IOException ex) {
			GUIUtilities.errorMessage("Startup Failure", "A file couldn't be loaded!", ex);
		}
		catch (ParseException ex) {
			GUIUtilities.errorMessage("Startup Failure", "A file seems to be invalid!", ex);
		}
		try {
			GUIUtilities.loadLogos();
		}
		catch (IOException ex) {
			GUIUtilities.LOGOS.clear();
			GUIUtilities.errorMessage("Logo Loading Failure", "The logos couldn't be loaded!", ex);
		}
		MainMenu game = new MainMenu();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
	}

}
