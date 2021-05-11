package ca.sharkyy.valanejobs;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class JobsEvent {
	
	
	private Events event;
	private int id;
	private byte data;
	private int xp;
	
	//Data is useless for Entites
	public JobsEvent(Events eventType, int id, byte data, int xp) {
		event = eventType;
		this.id = id;
		this.data = data;
		this.xp = xp;
	}
	
	public Events getEventType() {
		return event;
	}
	
	public int getId() {
		return id;
	}
	
	public byte getData() {
		return data;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getItem() {
		ItemStack item = new ItemStack(id, 1, data);
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public EntityType getEntityType() {
		return EntityType.fromId(id);
	}
	
	public int getXpAmount() {
		return xp;
	}
	

}
