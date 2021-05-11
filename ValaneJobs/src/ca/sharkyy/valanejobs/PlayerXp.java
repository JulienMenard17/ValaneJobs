package ca.sharkyy.valanejobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PlayerXp {
	
	private UUID playerID;
	private HashMap<Jobs, Integer> jobsXp;
	
	public PlayerXp(UUID playerUUID) {
		playerID = playerUUID;
		jobsXp = new HashMap<Jobs, Integer>();
	}
	
	public void addXpData(Jobs job, int xp) {
		jobsXp.put(job, xp);

	}
	
	//return le nouveau nombre d'xp
	public int addXp(Jobs job, int xp) {
		int actualLevel = getLevel(job);
		if(jobsXp.get(job) != null) {
			int actualXp = jobsXp.get(job);
			actualXp += xp;
			jobsXp.put(job, actualXp);
		}else {
			jobsXp.put(job, xp);
		}
		int afterLevel = getLevel(job);
		OfflinePlayer offp = Bukkit.getOfflinePlayer(playerID);
		if(offp.isOnline()) {
			offp.getPlayer().sendMessage("§a" + job.getDisplayName() + ": +" + xp);
			if(actualLevel < afterLevel) {
				offp.getPlayer().sendMessage("§bFélicitation, vous êtes maintenant Level " + afterLevel + " !");
			}
		}
		
		return jobsXp.get(job);
	}
	
	//return le nouveau nombre d'xp
	public int removeXp(Jobs job, int xp) {
		if(jobsXp.get(job) != null) {
			int actualXp = jobsXp.get(job);
			actualXp -= xp;
			if(actualXp >= 0) {
				jobsXp.put(job, actualXp);
			}else {
				jobsXp.put(job, 0);
			}
		}else {
			jobsXp.put(job, 0);
		}
		return jobsXp.get(job);
	}
	
	public void resetXp(Jobs job) {
		jobsXp.put(job, 0);
	}
	
	public UUID getUUID() {
		return playerID;
	}
	
	public HashMap<Jobs, Integer> getJobsXp() {
		return jobsXp;
	}
	
	public ArrayList<Jobs> getJobsList(){
		ArrayList<Jobs> jobsList = new ArrayList<Jobs>(jobsXp.keySet());
		return jobsList;
	}
	
	public int getLevel(Jobs job) {
		int xp = jobsXp.get(job);
		int counter = 0;
		int levelXp = 0;
		for(int i = 0; i < ConfigMgr.getXpLevel().size(); i++) {
			levelXp += ConfigMgr.getXpLevel().get(i);
			if(xp >= levelXp) {
				counter++;
			}else {
				return counter;
			}
		}
		return counter;
	}
	
	public int getXp(Jobs job) {
		int xp = getJobsXp().get(job);
		for(int i = 0; i < getLevel(job); i++) {
			xp -= ConfigMgr.getXpLevel().get(i);
		}
		return xp;
		
	}
	
	public int getXpNeeded(Jobs job) {
		int nextLevel = getLevel(job);
		if(getLevel(job) == ConfigMgr.getXpLevel().size())
			nextLevel--;
		int left = ConfigMgr.getXpLevel().get(nextLevel) - getXp(job);
		if(left < 0)
			left = 0;
				
		return left;
	}
	
	


}
