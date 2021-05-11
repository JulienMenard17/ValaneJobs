package ca.sharkyy.valanejobs;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class JobsEventsListener implements Listener {
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(PlayerXpMgr.getPlayerXpByUUID(p.getUniqueId()) == null) {
			PlayerXpMgr.addPlayerToList(p.getUniqueId());
		}else {
			PlayerXp pXp = PlayerXpMgr.getPlayerXpByUUID(p.getUniqueId());
			if(pXp.getJobsXp().keySet().size() < ConfigMgr.getJobsList().size()) {
				ArrayList<Jobs> pJobsList = new ArrayList<Jobs>(pXp.getJobsXp().keySet());
				ArrayList<Jobs> jobsList = ConfigMgr.getJobsList();
				for(int i= 0; i < jobsList.size(); i++ ) {
					if(!pJobsList.contains(jobsList.get(i))) {
						pXp.addXpData(jobsList.get(i), 0);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(e.isCancelled())
			return;
		if (PlayerXpMgr.getPlayerXpByUUID(e.getPlayer().getUniqueId()) != null) {
			PlayerXp pXp = PlayerXpMgr.getPlayerXpByUUID(e.getPlayer().getUniqueId());
			for (int i = 0; i < ConfigMgr.getJobsList().size(); i++) {
				Jobs job = ConfigMgr.getJobsList().get(i);
				if (job.getJobsEventForLevel(pXp.getLevel(job)) != null && !job.getJobsEventForLevel(pXp.getLevel(job)).isEmpty()) {
					ArrayList<JobsEvent> eventList = job.getJobsEventForLevel(pXp.getLevel(job));
					for (int j = 0; j < eventList.size(); j++) {
						JobsEvent event = eventList.get(j);
						if (event.getEventType() == Events.BREAK) {
							if (event.getId() == e.getBlock().getTypeId()
									&& event.getData() == e.getBlock().getData()) {
								pXp.addXp(job, event.getXpAmount());
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onKill(EntityDeathEvent e) {
		Player p = e.getEntity().getKiller();
		if (p != null && PlayerXpMgr.getPlayerXpByUUID(p.getUniqueId()) != null) {
			PlayerXp pXp = PlayerXpMgr.getPlayerXpByUUID(p.getUniqueId());
			for (int i = 0; i < ConfigMgr.getJobsList().size(); i++) {
				Jobs job = ConfigMgr.getJobsList().get(i);
				if (job.getJobsEventForLevel(pXp.getLevel(job)) != null && !job.getJobsEventForLevel(pXp.getLevel(job)).isEmpty()) {
					ArrayList<JobsEvent> eventList = job.getJobsEventForLevel(pXp.getLevel(job));
					for (int j = 0; j < eventList.size(); j++) {
						JobsEvent event = eventList.get(j);
						if (event.getEventType() == Events.KILL) {
							if (event.getEntityType().equals(e.getEntityType())) {
								pXp.addXp(job, event.getXpAmount());
							}
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		if(e.isCancelled())
			return;
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (PlayerXpMgr.getPlayerXpByUUID(p.getUniqueId()) != null) {
				PlayerXp pXp = PlayerXpMgr.getPlayerXpByUUID(p.getUniqueId());
				for (int i = 0; i < ConfigMgr.getJobsList().size(); i++) {
					Jobs job = ConfigMgr.getJobsList().get(i);
					if (job.getJobsEventForLevel(pXp.getLevel(job)) != null && !job.getJobsEventForLevel(pXp.getLevel(job)).isEmpty()) {
						ArrayList<JobsEvent> eventList = job.getJobsEventForLevel(pXp.getLevel(job));
						for (int j = 0; j < eventList.size(); j++) {
							JobsEvent event = eventList.get(j);
							if (event.getEventType() == Events.CRAFT) {

								if (event.getId() == e.getInventory().getResult().getTypeId()
									&& event.getData() == e.getInventory().getResult().getData().getData()) {
								
									ItemStack craftedItem = e.getInventory().getResult(); //Get result of recipe
									Inventory Inventory = e.getInventory(); //Get crafting inventory
									ClickType clickType = e.getClick();
									int realAmount = craftedItem.getAmount();
									if(clickType.isShiftClick()){
										int lowerAmount = craftedItem.getMaxStackSize() + 1000; //Set lower at recipe result max stack size + 1000 (or just highter max stacksize of reciped item)
										for(ItemStack actualItem : Inventory.getContents()) { //For each item in crafting inventory
											if(!(actualItem.getType() == Material.matchMaterial("AIR")) && lowerAmount > actualItem.getAmount() && !actualItem.getType().equals(craftedItem.getType())) //if slot is not air && lowerAmount is highter than this slot amount && it's not the recipe amount
												lowerAmount = actualItem.getAmount(); //Set new lower amount
										}
										//Calculate the final amount : lowerAmount * craftedItem.getAmount
										realAmount = lowerAmount * craftedItem.getAmount();
									}
								
									int amountCrafted = realAmount / e.getRecipe().getResult().clone().getAmount();
									pXp.addXp(job, event.getXpAmount() * amountCrafted);
								}
							}
						}
					}
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.isCancelled())
			return;
		if(e.getInventory().getType() == InventoryType.FURNACE) {
			if(e.getClickedInventory() != null && e.getClickedInventory().equals(e.getView().getTopInventory())) {
				if(e.getSlot() == 2) {
					if (PlayerXpMgr.getPlayerXpByUUID(e.getWhoClicked().getUniqueId()) != null) {
						PlayerXp pXp = PlayerXpMgr.getPlayerXpByUUID(e.getWhoClicked().getUniqueId());
						for (int i = 0; i < ConfigMgr.getJobsList().size(); i++) {
							Jobs job = ConfigMgr.getJobsList().get(i);
							if (job.getJobsEventForLevel(pXp.getLevel(job)) != null && !job.getJobsEventForLevel(pXp.getLevel(job)).isEmpty()) {
								ArrayList<JobsEvent> eventList = job.getJobsEventForLevel(pXp.getLevel(job));
								for (int j = 0; j < eventList.size(); j++) {
									JobsEvent event = eventList.get(j);
									if (event.getEventType() == Events.COOK) {
										if (event.getId() == e.getCurrentItem().getTypeId() && event.getData() == e.getCurrentItem().getData().getData()) {
											pXp.addXp(job, event.getXpAmount() * e.getCurrentItem().getAmount());
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
