package ca.sharkyy.valanejobs;

import java.util.ArrayList;
import java.util.HashMap;

public class Jobs {
	
	private String name;
	private String displayName;
	private HashMap<Integer,ArrayList<JobsEvent>> jobsEventPerLevel;
	
	public Jobs(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
		jobsEventPerLevel = new HashMap<Integer, ArrayList<JobsEvent>>();
	}
	
	public void addJobsEvent(int level, JobsEvent event) {
		ArrayList<JobsEvent> events;
		if(getJobsEventForLevel(level) != null) {
			events = new ArrayList<JobsEvent>(getJobsEventForLevel(level));
		}else {
			events = new ArrayList<JobsEvent>();
		}
		events.add(event);
		jobsEventPerLevel.put(level, events);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public HashMap<Integer, ArrayList<JobsEvent>> getJobsEventPerLevel() {
		return jobsEventPerLevel;
	}
	
	public ArrayList<JobsEvent> getJobsEventForLevel(int level){
		return jobsEventPerLevel.get(level);
	}

}
