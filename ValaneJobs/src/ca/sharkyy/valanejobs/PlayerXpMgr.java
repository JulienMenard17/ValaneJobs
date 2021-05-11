package ca.sharkyy.valanejobs;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;

public class PlayerXpMgr {
	
	private static ArrayList<PlayerXp> playerXpList;
	
	public static void loadPlayersXp() {
		playerXpList = new ArrayList<PlayerXp>();
		FileConfiguration database = Main.inst().getDataBase();
		ArrayList<String> playerDataList = new ArrayList<String>(database.getStringList("PlayerXP"));
		for(int i = 0; i < playerDataList.size(); i++) {
			String bufferStr = playerDataList.get(i);
			String[] buffer = bufferStr.split(":");
			PlayerXp p = new PlayerXp(UUID.fromString(buffer[0]));
			int counter = 2;
			for(int j = 0; j < Integer.parseInt(buffer[1]); j++) {
				p.addXpData(ConfigMgr.getJobsByName(buffer[counter]), Integer.parseInt(buffer[counter + 1]));
				counter += 2;
			}
			playerXpList.add(p);
		}
	}
	
	public static PlayerXp getPlayerXpByUUID(UUID playerUUID) {
		for(int i = 0; i < playerXpList.size(); i++) {
			if(playerXpList.get(i).getUUID().equals(playerUUID)) {
				return playerXpList.get(i);
			}
		}
		return null;
	}
	
	public static void addPlayerToList(UUID playerUUID) {
		PlayerXp pXp = new PlayerXp(playerUUID);
		ArrayList<Jobs> jobsList = ConfigMgr.getJobsList();
		for(int i = 0; i < jobsList.size(); i++) {
			pXp.addXpData(jobsList.get(i), 0);
		}
		playerXpList.add(pXp);
	}
	
	public static ArrayList<PlayerXp> getPlayerXpList() {
		return playerXpList;
	}
	
	public static void printDataToDataBase() {
		ArrayList<String> dataList = new ArrayList<String>();
		for(int i = 0; i < playerXpList.size(); i++) {
			StringBuilder buffer = new StringBuilder();
			PlayerXp pXp = playerXpList.get(i);
			
			buffer.append(pXp.getUUID() + ":" + pXp.getJobsList().size());
			for(int j = 0; j < pXp.getJobsList().size(); j++) {
				buffer.append(":" + pXp.getJobsList().get(j).getName());
				buffer.append(":" + pXp.getJobsXp().get(pXp.getJobsList().get(j)));
			}
			dataList.add(buffer.toString());
		}
		
		Main.inst().getDataBase().set("PlayerXP", dataList);
	}

}
