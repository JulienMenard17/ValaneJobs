package ca.sharkyy.valanejobs;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	private static Main main;
	private File jobsConfigFile;
	private FileConfiguration jobsConfig;
	private File databaseFile;
	private FileConfiguration database;

	@Override
	public void onEnable() {
		main = this;
		createJobsConfig();
		createDatabase();
		ConfigMgr.loadJobsConfig();
		PlayerXpMgr.loadPlayersXp();
		getCommand("valanejobs").setExecutor(new CommandExecutor());
		Bukkit.getPluginManager().registerEvents(new JobsEventsListener(), this);
	}
	
	@Override
	public void onDisable() {
		PlayerXpMgr.printDataToDataBase();
		saveDatabase();
	}
	
	public static Main inst() {
		return main;
	}
	
	public void reload() {
	createJobsConfig();
	PlayerXpMgr.printDataToDataBase();
	saveDatabase();
	createDatabase();
	ConfigMgr.loadJobsConfig();
	PlayerXpMgr.loadPlayersXp();
	}
	
	public FileConfiguration getJobsConfig() {
		return this.jobsConfig;
	}
	
	
	private void createJobsConfig() {
		jobsConfigFile = new File(getDataFolder(), "jobsConfig.yml");
		if(!jobsConfigFile.exists()) {
			jobsConfigFile.getParentFile().mkdirs();
			saveResource("jobsConfig.yml", false);
		}
		
		jobsConfig = new YamlConfiguration();
		try {
			jobsConfig.load(jobsConfigFile);
		} catch (IOException | InvalidConfigurationException e ) {
			e.printStackTrace();
		}
	}
	
	public void saveJobsConfig() {
	    try {
	    	jobsConfig.save(jobsConfigFile);
	        } catch (IOException e) {
	            this.getLogger().warning("Unable to save " + "jobsConfig.yml"); // shouldn't really happen, but save throws the exception
	        }
	}
	
	public FileConfiguration getDataBase() {
		return this.database;
	}
	
	
	private void createDatabase() {
		databaseFile = new File(getDataFolder(), "database.yml");
		if(!databaseFile.exists()) {
			databaseFile.getParentFile().mkdirs();
			saveResource("database.yml", false);
		}
		
		database = new YamlConfiguration();
		try {
			database.load(databaseFile);
		} catch (IOException | InvalidConfigurationException e ) {
			e.printStackTrace();
		}
	}
	
	public void saveDatabase() {
	    try {
	    	database.save(databaseFile);
	        } catch (IOException e) {
	            this.getLogger().warning("Unable to save " + "database.yml"); // shouldn't really happen, but save throws the exception
	        }
	}
}
