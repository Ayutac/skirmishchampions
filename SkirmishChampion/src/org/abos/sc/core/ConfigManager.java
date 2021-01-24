package org.abos.sc.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class ConfigManager {
	
	/**
	 * the name of the configuration
	 */
	public final static String CONFIG_NAME = "skirmishChampion.cfg";
	
	/**
	 * the key for the last save location
	 */
	public final static String LAST_SAVE_LOCATION = "last_save_location";

	/**
	 * the configuration of this application instance
	 */
	protected static Properties CONFIG = new Properties(); 

	/**
	 * Private constructor to avoid instantiation.
	 */
	private ConfigManager() {}
	
	public static String getProperty(String key) {
		return CONFIG.getProperty(key);
	}
	
	public static Object setProperty(String key, String value) {
		return CONFIG.setProperty(key, value);
	}
	
	/**
	 * 
	 * @throws IllegalStateException If {@link Utilities#loadApplicationDirectory()} hasn't successfully been called yet.
	 * @throws IOException
	 */
	public static void loadConfig() throws IOException {
		Utilities.checkApplicationDirectory();
		FileReader fr = null;
		Properties newConfig = null;
		try {
			newConfig = new Properties();
			File configFile = Utilities.getApplicationDirectory().resolve(CONFIG_NAME).toFile();
			if (!configFile.exists()) {
				CONFIG = newConfig;
				return;
			}
			fr = new FileReader(configFile, Utilities.ENCODING);
			newConfig.load(fr);
		}
		finally {
			if (fr != null) try {fr.close();} catch(IOException ex) {/* ignore */}
		}
		CONFIG = newConfig;
	}
	
	/**
	 * 
	 * @throws IllegalStateException If {@link Utilities#loadApplicationDirectory()} hasn't successfully been called yet.
	 * @throws IOException
	 */
	public static void saveConfig() throws IOException {
		Utilities.checkApplicationDirectory();
		FileWriter fw = null;
		try {
			fw = new FileWriter(Utilities.getApplicationDirectory().resolve(CONFIG_NAME).toFile(), Utilities.ENCODING, false);
			CONFIG.store(fw, null);
		}
		finally {
			if (fw != null) try {fw.close();} catch(IOException ex) {/* ignore */}
		}
	}

}
