package eu.heroria.heroriacore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;

import eu.heroria.heroriacore.gui.CustomScoreBoardManager;
import eu.heroria.heroriacore.playerdata.Rank;
import eu.heroria.heroriacore.proxy.ProxyResult;

public class Listener implements org.bukkit.event.Listener, CommandExecutor {
	private HeroriaCore pl;
	
	public Listener(HeroriaCore pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onDisable(PluginDisableEvent event) {
		for (World world : Bukkit.getServer().getWorlds()) {
			for(Entity entity : world.getEntities()) {
				entity.remove();
			}
		}
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.kickPlayer(Bukkit.getShutdownMessage());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		if(!pl.sql.isConnected()) {
			pl.sql.connection();
		}
		Player player = event.getPlayer();
		Thread t = new Thread() {
			public void run() {
				pl.sql.createAccount(player);
				pl.dataManager.loadPlayerData(player);
				pl.setupPermissions(player);
				if(pl.getRank(player).getJoinMessage() == true) event.setJoinMessage(player.getCustomName() + " vient de se connecter.");
				ProxyResult proxyResult = pl.iop.getIp(player);
				Bukkit.getLogger().info(player.getName() + " IP: " + proxyResult.getStringResult() + ":" + proxyResult.getIntResult());
				if(pl.isHub()) {
					int friendsRequest = pl.sql.getFriendRequest(player).size();
					if(friendsRequest > 1) player.sendMessage(new String[] {" ", " ", "Bon retour parmi nous " + player.getName() + " !", " ", "Vous avez " + friendsRequest + " requête(s) d'ami(s) en attente.", "Faites /friend pour en savoir plus", " ", " "});
					else player.sendMessage(new String[] {" ", " ", "Bon retour parmi nous " + player.getName() + " !", " ", " "});
				}
			}
		};
		if(pl.sql.isBanned(player.getUniqueId().toString())) {
			player.kickPlayer("Vous êtes bannis\nVous avez été bannis pour la raison suivante: " + pl.sql.getBanReason(player.getUniqueId().toString()));
		}
		t.start();
		player.setCustomName(player.getDisplayName());
		if(pl.isHub()) {
			CustomScoreBoardManager board = new CustomScoreBoardManager(pl, player);
			board.sendLine();
			board.setScoreboard();
		}
		if(pl.teleportIOP.containsKey(player.getName())) {
			player.teleport(pl.teleportIOP.get(player.getName()).getLocation());
			pl.teleportIOP.remove(player.getName());
		}
	}
	
	@EventHandler
	public void onDead(PlayerDeathEvent event) {
		event.setKeepLevel(true);
		event.setKeepInventory(true);
		event.setDeathMessage(event.getEntity().getCustomName() + " est mort.");
		if(pl.getReputation(event.getEntity()) >= 1) pl.setReputation(event.getEntity(), pl.getReputation(event.getEntity()) - 1);
		else pl.setReputation(event.getEntity(), 0);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Thread t = new Thread() {
			public void run() {
				pl.dataManager.savePlayerData(player);
				pl.scoreBoard.remove(player);
				pl.playerPermission.remove(player.getUniqueId());
				pl.dataPlayers.remove(player);
			}
		};
		t.start();
		event.setQuitMessage(null);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		pl.dataManager.savePlayerData(player);
		pl.scoreBoard.remove(player);
		pl.playerPermission.remove(player.getUniqueId());
		pl.dataPlayers.remove(player);
		event.setLeaveMessage(null);
	}
	
	@EventHandler
	public void onMelt(BlockFromToEvent event) {
		if(event.getToBlock().getType() == Material.WATER) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPreCommand(PlayerCommandPreprocessEvent event) {
		String[] label = event.getMessage().split(" ");
		Player player = event.getPlayer();
		if(label[0].contains("/help")) {
			event.setCancelled(true);
			player.sendMessage(new String[]{"§e--------------------------------------------------", " ", "§l/shop:§r Accéder à la boutique", " ", "§l/lobby:§r Retournez sur le lobby", " ", "§l/hub:§r Retournez sur le lobby", " ", "§l/me:§r Afficher un message de statut (grade " + ChatColor.stripColor(Rank.VIP1.getName()) +" minimum)", " ", "§e--------------------------------------------------"});
		}
		else if(label[0].contains("/ban")) {
			event.setCancelled(true);
			if(player.hasPermission("minecraft.command.ban")) {
				if(label.length == 1) {
					pl.heroriaMessage("Faites /ban [player] (reason).", player);
					return;
				}
				try {
					Player pTo = Bukkit.getPlayer(label[1]);
					if(label.length == 2) {
						player.openInventory(pl.playerGUI.sanctionInterface(pTo));
					}
					else if(label.length > 2) {
						label[0] = null;
						label[1] = null;
						String reason = null;
						for (String string : label) {
							reason = reason + string + " ";
						}
						pl.ban(pTo, 1000, reason, player);
						pl.heroriaMessage("Le joueur " + pTo.getName() + " a été banni pour très longtemps.", player);
					}
				} catch (NullPointerException e) {
					try{
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(label[1]);
						if(label.length == 2) {
							pl.sql.ban(offlinePlayer.getUniqueId().toString(), 1000, "Motif indéfini", player);
							pl.heroriaMessage("Le joueur " + offlinePlayer.getName() + " a été banni pour très longtemps.", player);
							return;
						}
						else if(label.length > 2) {
							label[0] = null;
							label[1] = null;
							String reason = null;
							for (String string : label) {
								reason = reason + string + " ";
							}
							pl.sql.ban(offlinePlayer.getUniqueId().toString(), 1000, reason, player);
							pl.heroriaMessage("Le joueur " + offlinePlayer.getName() + " a été banni pour très longtemps.", player);
							return;
						}
					} catch (NullPointerException e2) {
						pl.heroriaMessage("Joueur introuvable !", player);
					}
				}
				
			}
			else {
				pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", player);
			}
		}
		else if(label[0].contains("/tp")) {
			event.setCancelled(true);
			if(player.hasPermission("minecraft.command.tp")) {
				if(label.length == 1) {
					pl.heroriaMessage("Faites /tp [player].", player);
				}
				else if(label.length > 2) {
					pl.heroriaMessage("Faites simplement /tp [player].", player);
				}
				else if(label.length == 2) {
					try {
						player.teleport(Bukkit.getPlayer(label[2]).getLocation());
					} catch (NullPointerException e) {
						pl.iop.teleport(player, label[1]);
					}
				}
			}
		}
		else if(label[0].contains("/pardon")) {
			event.setCancelled(true);
			if(player.hasPermission("minecraft.command.pardon")) {
				if(label.length == 1) {
					pl.heroriaMessage("Faites /pardon [player].", player);
					return;
				}
				else if (label.length > 2) {
					pl.heroriaMessage("Faites simplement /pardon [player].", player);
					return;
				}
				try {
					Player pTo = Bukkit.getPlayer(label[1]);
					if(pl.sql.isBanned(pTo.getUniqueId().toString())) {
						pl.heroriaMessage("Le joueur n'est pas banni !", player);
						return;
					}
					pl.sql.unBan(pTo.getUniqueId().toString());
					pl.sql.logAction(Action.PARDON, null, player, pTo);
					pl.heroriaMessage("Le joueur " + pTo.getName() + " a été débanni.", player);
				} catch (NullPointerException e) {
					try{
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(label[1]);
						if(pl.sql.isBanned(offlinePlayer.getUniqueId().toString())) {
							pl.heroriaMessage("Le joueur n'est pas banni !", player);
							return;
						}
						pl.sql.unBan(offlinePlayer.getUniqueId().toString());
						pl.sql.logAction(Action.PARDON, null, player, offlinePlayer.getPlayer());
						pl.heroriaMessage("Le joueur " + offlinePlayer.getName() + " a été débanni.", player);
					} catch (NullPointerException e2) {
						pl.heroriaMessage("Joueur introuvable !", player);
					}
				}
			}
			else {
				pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", player);
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getLabel().equalsIgnoreCase("player")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("Action impossible !");
				return true;
			}
			
			Player player = (Player) sender;
			if(args.length !=  1) {
				pl.heroriaMessage("Faites simplement /player [user]", player);
				return true;
			}
			try {
				Player pTo = Bukkit.getPlayer(args[0]);
				if(pl.getRank(player).getPower() < Rank.MODO.getPower()) {
					pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", player);
					return true;
				}
				if(pl.getRank(player).getPower() < pl.getRank(pTo).getPower()) {
					pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", player);
					return true;
				}
				pl.playerGUI.openPlayerInterface(player, pTo);
			} catch (NullPointerException e) {
				pl.heroriaMessage("Joueur introuvable !", player);
			}
		}
		else if(cmd.getLabel().equalsIgnoreCase("friend")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("Action impossible !");
				return true;
			}
			
			Player player = (Player) sender;
			if(args.length == 0) {
				Inventory inv = pl.friendGUI.friendInterface(player);
				player.openInventory(inv);
				return false;
			}
			if(args.length == 1) {
				pl.heroriaMessage("Faites /friend [add/remove] [player]", player);
				return true;
			}
			else if(args.length == 2) {
				try{
					Player pTo = Bukkit.getPlayer(args[1]);
					if(args[0].equalsIgnoreCase("add")) {
						if(pl.sql.isRequested(player, pTo.getUniqueId().toString())) {
							pl.sql.acceptFriendRequest(pTo.getUniqueId().toString(), player);
							pl.heroriaMessage("Vous avez accepté la demande d'ami de " + pTo.getName() + " !", player);
						}
						else {
							pl.sql.requestFriend(player, pTo.getUniqueId().toString());
							pl.heroriaMessage("Une demande d'ami a été envoyé à " + pTo.getName() + " !", player);
						}
					}
					else if(args[0].equalsIgnoreCase("remove")) {
						if(pl.sql.isFriend(player, pTo.getUniqueId().toString())) {
							pl.sql.removeFriend(player.getUniqueId().toString(), pTo.getUniqueId().toString());
						}
					}
				} catch (NullPointerException e) {
					try {
						OfflinePlayer pTo = Bukkit.getOfflinePlayer(args[1]);
						if(args[0].equalsIgnoreCase("add")) {
							if(pl.sql.isRequested(player, pTo.getUniqueId().toString())) {
								pl.sql.acceptFriendRequest(pTo.getUniqueId().toString(), player);
								pl.heroriaMessage("Vous avez accepté la demande d'ami de " + pTo.getName() + " !", player);
							}
							else {
								pl.sql.requestFriend(player, pTo.getUniqueId().toString());
								pl.heroriaMessage("Une demande d'ami a été envoyé à " + pTo.getName() + " !", player);
							}
						}
						else if(args[0].equalsIgnoreCase("remove")) {
							if(pl.sql.isFriend(player, pTo.getUniqueId().toString())) {
								pl.sql.removeFriend(player.getUniqueId().toString(), pTo.getUniqueId().toString());
							}
						}
					} catch (NullPointerException e2) {
						e2.printStackTrace();
						pl.heroriaMessage("Joueur introuvable !", player);
						return true;
					}
				}
			}
		}
		else if(cmd.getLabel().equalsIgnoreCase("warn")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				int argsN = args.length;
				if(argsN == 0) {
					pl.heroriaMessage("Faites /warn [player] (reason).", player);
					return true;
				}
				try {
					Player pTo = Bukkit.getPlayer(args[0]);
					if(pl.getRank(player).getPower() < Rank.MODO.getPower()) {
						pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", player);
						return true;
					}
					if(pl.getRank(player).getPower() < pl.getRank(pTo).getPower()) {
						pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", player);
						return true;
					}
					if(argsN == 1) {
						pl.warnPlayer(pTo, null, player);
						pl.heroriaMessage("Le joueur a reçu un avertissement.", player);
						return false;
					}
					else {
						String reason = "";
						int current = 1;
						while(current < argsN) {
							reason = reason + args[current] + " ";
							current = current + 1;
						}
						pl.warnPlayer(pTo, reason, player);
						pl.heroriaMessage("Le joueur a reçu un avertissement pour: ' " + reason + "'.", player);
					}
				} catch (NullPointerException e) {
					pl.heroriaMessage("Joueur introuvable !", player);
				}
			}
			else {
				int argsN = args.length;
				if(argsN == 0) {
					sender.sendMessage("§4[Heroria]§r Faites /warn [player] (reason).");
					return true;
				}
				try {
					Player pTo = Bukkit.getPlayer(args[0]);
					if(argsN == 1) {
						pl.warnPlayer(pTo, null, null);
						sender.sendMessage("§4[Heroria]§r Le joueur a reçu un avertissement.");
						return false;
					}
					else {
						String reason = "";
						int current = 1;
						while(current < argsN) {
							reason = reason + args[current] + " ";
							current = current + 1;
						}
						pl.warnPlayer(pTo, reason, null);
						sender.sendMessage("§4[Heroria]§r Le joueur a reçu un avertissement pour: ' " + reason + "'.");
					}
				} catch (NullPointerException e) {
					sender.sendMessage("§4[Heroria]§r Joueur introuvable !");
				}
			}
		}
		else if(cmd.getLabel().equalsIgnoreCase("rec")) {
			if(sender instanceof Player ) {
				pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", (Player) sender);
				return true;
			}
			if(args.length !=  1) {
				sender.sendMessage("§4[Heroria] §rFaites simplement /player [user]");
				return true;
			}
			try {
				Player pTo = Bukkit.getPlayer(args[0]);
				pl.setMoney(pTo, pl.getMoney(pTo) + 50);
				pl.setReputation(pTo, pl.getReputation(pTo) + 10);
				pTo.setTotalExperience(pTo.getTotalExperience() + 10);
				pl.sql.logAction(Action.REC, null, null, pTo);
			} catch (NullPointerException e) {
				sender.sendMessage("§4[Heroria] §rJoueur introuvable !");
			}
		}
		else if(cmd.getLabel().equalsIgnoreCase("rank")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				int argsN = args.length;
				if(argsN != 2) {
					pl.heroriaMessage("Faites simplement /rank [player] [rank].", player);
					return true;
				}
				try {
					Player pTo = Bukkit.getPlayer(args[0]);
					if(pl.getRank(player).getPower() < Rank.MODO.getPower()) {
						pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", player);
						return true;
					}
					if(pl.getRank(player).getPower() < pl.getRank(pTo).getPower()) {
						pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", player);
						return true;
					}
					else {
						Rank rank = Rank.JOUEUR;
						if(args[1].equalsIgnoreCase("joueur")) {
							rank = Rank.JOUEUR;
						}
						else if(args[1].equalsIgnoreCase("vip1")) {
							rank = Rank.VIP1;
						}
						else if(args[1].equalsIgnoreCase("vip2")) {
							rank = Rank.VIP2;
						}
						else if(args[1].equalsIgnoreCase("vip3")) {
							rank = Rank.VIP3;
						}
						else if(args[1].equalsIgnoreCase("vip4")) {
							rank = Rank.VIP4;
						}
						else if(args[1].equalsIgnoreCase("modo")) {
							rank = Rank.MODO;
						}
						else if(args[1].equalsIgnoreCase("sm")) {
							rank = Rank.SM;
						}
						else if(args[1].equalsIgnoreCase("cm")) {
							rank = Rank.CM;
						}
						else if(args[1].equalsIgnoreCase("admin")) {
							rank = Rank.ADMINISTRATEUR;
						}
						else {
							pl.heroriaMessage("Grade introuvable !", player);
							return true;
						}
						
						pl.heroriaMessage("Le grade " + rank.getName() + " a été défini pour le joueur " + pTo.getName() + ".", player);
						pl.sql.logAction(Action.RANK, "set " + rank.getTechName() + " (before: " + pl.getRank(player) + ")", player, pTo);
						pl.setRank(pTo, rank);
					}
				} catch (NullPointerException e) {
					pl.heroriaMessage("Joueur introuvable !", player);
				}
			}
			else {
				int argsN = args.length;
				if(argsN != 2) {
					sender.sendMessage("§4[Heroria]§r Faites simplement /rank [player] [rank].");
					return true;
				}
				try {
					Player pTo = Bukkit.getPlayer(args[0]);
					Rank rank = Rank.JOUEUR;
					if(args[1].equalsIgnoreCase("joueur")) {
						rank = Rank.JOUEUR;
					}
					else if(args[1].equalsIgnoreCase("vip1")) {
						rank = Rank.VIP1;
					}
					else if(args[1].equalsIgnoreCase("vip2")) {
						rank = Rank.VIP2;
					}
					else if(args[1].equalsIgnoreCase("vip3")) {
						rank = Rank.VIP3;
					}
					else if(args[1].equalsIgnoreCase("vip4")) {
						rank = Rank.VIP4;
					}
					else if(args[1].equalsIgnoreCase("modo")) {
						rank = Rank.MODO;
					}
					else if(args[1].equalsIgnoreCase("sm")) {
						rank = Rank.SM;
					}
					else if(args[1].equalsIgnoreCase("cm")) {
						rank = Rank.CM;
					}
					else if(args[1].equalsIgnoreCase("admin")) {
						rank = Rank.ADMINISTRATEUR;
					}
					else {
						sender.sendMessage("§4[Heroria]§r Grade introuvable !");
						return true;
					}
					sender.sendMessage("§4[Heroria]§r Le grade " + rank.getName() + " a été défini pour le joueur " + pTo.getName() + ".");
					pl.sql.logAction(Action.RANK, "set " + rank.getTechName() + " (before: " + pl.getRank(pTo) + ")", null, pTo);
					pl.setRank(pTo, rank);
				} catch (NullPointerException e) {
					sender.sendMessage("§4[Heroria]§r Joueur introuvable !");
				}
			}
		}
		else if(cmd.getLabel().equalsIgnoreCase("test2")) {
			pl.iop.test((Player)sender);
			
		}
		else if(cmd.getLabel().equalsIgnoreCase("shop")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("Action impossible");
				return true;
			}
			Player player = (Player) sender;
			if(args.length !=  0) {
				pl.heroriaMessage("Faites simplement /shop", player);
				return true;
			}
			pl.shopGUI.openShopInterface(player);
		}
		return false;
	}
}
