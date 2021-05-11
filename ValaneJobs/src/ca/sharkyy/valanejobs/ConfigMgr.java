package ca.sharkyy.valanejobs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigMgr {
	
	private static ArrayList<Integer> xpLevel;
	private static ArrayList<Jobs> jobsList;
	
	public static void loadJobsConfig() {
		FileConfiguration config = Main.inst().getJobsConfig();
		String buffer;
		//Nbr d'xp par level
		xpLevel = new ArrayList<Integer>();
		ArrayList<String> configXpLevel = new ArrayList<String>(config.getStringList("LevelXP"));
		for(int i = 0; i < configXpLevel.size(); i++) {
			buffer = configXpLevel.get(i);
			buffer = buffer.split(":")[1];
			xpLevel.add(Integer.parseInt(buffer));
		}
		buffer = "";
		//Recupérer la liste des métiers
		jobsList = new ArrayList<Jobs>();
		
		ArrayList<String> configJobsList = new ArrayList<String>(config.getStringList("Jobs"));
		for(int i = 0; i < configJobsList.size(); i++) {
			Jobs job = new Jobs(configJobsList.get(i), config.getString(configJobsList.get(i) + ".DisplayName"));
			//List<String> eventsLevelList = Main.inst().getJobsConfig().getStringList(configJobsList.get(i) + ".Events");
			for(int j = 0; j <= xpLevel.size(); j++) {
				List<String> eventsList = config.getStringList(configJobsList.get(i) + ".Events." + String.valueOf(j));
				for(int l = 0; l < eventsList.size(); l++) {
					buffer = eventsList.get(l);
					String[] bufferTab = buffer.split(":");
					job.addJobsEvent(j, new JobsEvent(Events.getEventByName(bufferTab[0]), Integer.parseInt(bufferTab[1]), Byte.parseByte(bufferTab[2]), Integer.parseInt(bufferTab[3])));
				}
			}
			
			jobsList.add(job);
			
		}
		
		
		
	}
	
	public static ArrayList<Integer> getXpLevel() {
		return xpLevel;
	}
	
	public static ArrayList<Jobs> getJobsList() {
		return jobsList;
	}
	
	public static Jobs getJobsByDisplayName(String jobsDName) {
		for(int i = 0; i < jobsList.size(); i++) {
			if(jobsList.get(i).getDisplayName().equals(jobsDName)) {
				return jobsList.get(i);
			}
		}
		return null;	
	}
	
	public static Jobs getJobsByName(String jobsName) {
		for(int i = 0; i < jobsList.size(); i++) {
			if(jobsList.get(i).getName().equals(jobsName)) {
				return jobsList.get(i);
			}
		}
		return null;	
	}
	
	
	

}
