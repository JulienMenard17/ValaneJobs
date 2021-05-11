package ca.sharkyy.valanejobs;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Commande: /vjobs reload
		if(label.equalsIgnoreCase("valanejobs") || label.equalsIgnoreCase("vjobs")) {
			if(args.length == 1 && sender.hasPermission("valanejobs.edit")) {
				if(args[0].equalsIgnoreCase("reload")) {
					Main.inst().reload();
					Bukkit.broadcastMessage("§aConfig Reloaded");
					return true;
				}
			}
			
			// Commande: /vjobp look [NomDuJoueur]
			if(args.length == 2 && sender.hasPermission("valanejobs.edit")) {
				if(args[0].equalsIgnoreCase("look")) {
					if(Bukkit.getOfflinePlayer(args[1]) != null) {
						for(PlayerXp offp : PlayerXpMgr.getPlayerXpList()) {
							if(Bukkit.getOfflinePlayer(offp.getUUID()).getName().equals(args[1])) {
								OfflinePlayer p = Bukkit.getOfflinePlayer(offp.getUUID());
							
								PlayerXp pXp = PlayerXpMgr.getPlayerXpByUUID(p.getUniqueId());
								getPluginLine(sender, offp.getUUID(), 0 , false); // Print args[1](PlayerName typed) infos on top
								for(int i = 0; i < pXp.getJobsList().size(); i++) {
									Jobs job = ConfigMgr.getJobsList().get(i);
									int nextLevel = pXp.getLevel(job);
									if(pXp.getLevel(job) == ConfigMgr.getXpLevel().size())
										nextLevel--;
									sender.sendMessage("§a" + job.getDisplayName() + ": Level " + pXp.getLevel(job) + " - Xp: " + pXp.getXp(job)+"/"+ ConfigMgr.getXpLevel().get(nextLevel) + " - Xp Manq.: " + pXp.getXpNeeded(job));
								
								}
								getPluginLine(sender, offp.getUUID(), 1 , false); // Print args[1](PlayerName typed) infos on bottom
							}
						}
					}	
				}
			}
		}
		
			
		if(sender instanceof Player) {
			Player p = (Player) sender;
			//Player commands
			
			//Si c la commande
			if(label.equalsIgnoreCase("valanejobs") || label.equalsIgnoreCase("vjobs")) {
				
				//afficher tout les métiers
				if(args.length == 0) {
					PlayerXp pXp = PlayerXpMgr.getPlayerXpByUUID(p.getUniqueId());
					getPluginLine(p, p.getUniqueId(), 0, true);
					for(int i = 0; i < pXp.getJobsList().size(); i++) {
						Jobs job = ConfigMgr.getJobsList().get(i);
						int nextLevel = pXp.getLevel(job);
						if(pXp.getLevel(job) == ConfigMgr.getXpLevel().size())
							nextLevel--;
						p.sendMessage("§a" + job.getDisplayName() + ": Level " + pXp.getLevel(job) + " - Xp: " + pXp.getXp(job)+"/"+ ConfigMgr.getXpLevel().get(nextLevel) + " - Xp Manq.: " + pXp.getXpNeeded(job));
					}
					getPluginLine(p, p.getUniqueId(), 1, true);
					
					
				}
				//affiche le métier indiqué
				if(args.length == 1) {
					String jobName = args[0];
					if(ConfigMgr.getJobsByDisplayName(jobName) != null) {
						PlayerXp pXp = PlayerXpMgr.getPlayerXpByUUID(p.getUniqueId());
						Jobs job = ConfigMgr.getJobsByDisplayName(jobName);
						int nextLevel = pXp.getLevel(job);
						if(pXp.getLevel(job) == ConfigMgr.getXpLevel().size())
							nextLevel--;
						getPluginLine(p, p.getUniqueId(), 0, true);
						p.sendMessage("§a" + job.getDisplayName() + ": Level " + pXp.getLevel(job) + " - Xp: " + pXp.getXp(job)+"/"+ ConfigMgr.getXpLevel().get(nextLevel) + " - Xp Manq.: " + pXp.getXpNeeded(job));
						getPluginLine(p, p.getUniqueId(), 1, true);
					}else {
						p.sendMessage("§cCmd: /valanejobs | /valanejobs [NomDuJobs]");
						p.sendMessage("§7Liste des Jobs:");
						StringBuilder str = new StringBuilder();
						str.append("§7");
						for(int i = 0; i < ConfigMgr.getJobsList().size(); i++) {
							str.append(ConfigMgr.getJobsList().get(i).getDisplayName());
							if(i != ConfigMgr.getJobsList().size() -1) {
								str.append(" - ");
							}
						}
						p.sendMessage(str.toString());
					}
				}
				
			}
		}else {
			if(label.equalsIgnoreCase("valanejobs") || label.equalsIgnoreCase("vjobs")) {
				if(args.length == 0 || args.length == 1)
					sender.sendMessage("§c[Erreur] Cette commande doit exécuter par un Joueur !");
			}
		}
		
		//Admin Commands
		if((label.equalsIgnoreCase("valanejobs") || label.equalsIgnoreCase("vjobs")) && (sender.hasPermission("valanejobs.edit") || sender.hasPermission("valanejobs.*"))) {
			
			if(args.length == 2 && args[0].equalsIgnoreCase("edit") && args[1].equalsIgnoreCase("help")) {
				sender.sendMessage("§7Listes des cmds:");
				sender.sendMessage("§7/valanejobs | /vjobs");
				sender.sendMessage("§7/vjobs [NomDuJobs]");
				sender.sendMessage("§7/vjobs look [NomDuJoueur]");
				sender.sendMessage("§7/vjobs edit addxp [NomDuJob] [NomDuJoueur] [Xp]");
				sender.sendMessage("§7/vjobs edit remxp [NomDuJob] [NomDuJoueur] [Xp]");
			}
			
			if(args.length == 5 && args[0].equalsIgnoreCase("edit") && (args[1].equalsIgnoreCase("addxp") || args[1].equalsIgnoreCase("remxp"))) {
						if(ConfigMgr.getJobsByDisplayName(args[2]) != null) {
							Jobs job = ConfigMgr.getJobsByDisplayName(args[2]);
							if(Bukkit.getOfflinePlayer(args[3]) != null) {
								for(PlayerXp offp : PlayerXpMgr.getPlayerXpList()) {
									if(Bukkit.getOfflinePlayer(offp.getUUID()).getName().equals(args[3])) {
										sender.sendMessage("§aCommande effectuée avec succès !");
										if(args[1].equalsIgnoreCase("addxp"))
											offp.addXp(job, Integer.parseInt(args[4]));
										if(args[1].equalsIgnoreCase("remxp"))
											offp.removeXp(job, Integer.parseInt(args[4]));
										return true;
									}
								}
								sender.sendMessage("§cCe joueur n'existe pas !");
								
							}else {
								sender.sendMessage("§cCe joueur n'existe pas !");
							}
						}else {
							sender.sendMessage("§cCe job n'existe de pas !");
							sender.sendMessage("§7Liste des Jobs:");
							StringBuilder str = new StringBuilder();
							str.append("§7");
							for(int i = 0; i < ConfigMgr.getJobsList().size(); i++) {
								str.append(ConfigMgr.getJobsList().get(i).getDisplayName());
								if(i != ConfigMgr.getJobsList().size() -1) {
									str.append(" - ");
								}
							}
							sender.sendMessage(str.toString());
						}
					
					}
			
		}

		
		
		
		return false;
	}
	// Print Top(int 0) and Bottom(int 1) custom line for the plugin
	private void getPluginLine(CommandSender p,UUID pUUID, int line, boolean isUrInfo) {
		String infos;
		if(isUrInfo) {
			infos = "Vos Informations";
		}else {
			infos = Bukkit.getOfflinePlayer(pUUID).getName() + " Infos";
		}
		if(line == 0)
			p.sendMessage(ChatColor.GRAY + ""+ChatColor.BOLD+ChatColor.STRIKETHROUGH+"                "+"-[" +ChatColor.RED+ " " + infos +" "+ChatColor.GRAY + ""+ChatColor.BOLD+ChatColor.STRIKETHROUGH+"]-"+"                ");

		if(line == 1)
		p.sendMessage(ChatColor.GRAY + ""+ChatColor.BOLD+ChatColor.STRIKETHROUGH+"                   "+"-[" +ChatColor.RED+ " ValaneJobs "+ChatColor.GRAY + ""+ChatColor.BOLD+ChatColor.STRIKETHROUGH+"]-"+"                   ");
	}

}
