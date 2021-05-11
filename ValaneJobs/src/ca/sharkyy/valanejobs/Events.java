package ca.sharkyy.valanejobs;

public enum Events {
	KILL,
	CRAFT,
	BREAK,
	COOK;
	
	public static Events getEventByName(String name) {
		switch(name) {
		case "KILL":
			return KILL;
		case "CRAFT":
			return CRAFT;
		case "BREAK":
			return BREAK;
		case "COOK":
			return COOK;
		default:
			return null;
		}
	}
}
