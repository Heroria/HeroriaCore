package eu.heroria;

import java.util.function.Function;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.White;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import eu.heroria.gui.CustomScoreBoardManager;
import eu.heroria.playerdata.Rank;

public class Listener implements org.bukkit.event.Listener, CommandExecutor {
	private Main pl;
	
	public Listener(Main pl) {
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
		Player player = event.getPlayer();
		if(pl.sql.isBanned(player)) {
			player.kickPlayer("Vous êtes bannis\nVous avez été bannis pour la raison suivante: " + pl.sql.getBanReason(player));
		}
		player.setCustomName(player.getDisplayName());
		pl.sql.createAccount(player);
		pl.dataManager.loadPlayerData(player);
		pl.setupPermissions(player);
		if(pl.getRank(player).getJoinMessage()) event.setJoinMessage(player.getCustomName() + " vient de se connecter");
		else event.setJoinMessage(null);
		CustomScoreBoardManager board = new CustomScoreBoardManager(pl, player);
		board.sendLine();
		board.setScoreboard();
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
		pl.dataManager.savePlayerData(player);
		pl.scoreBoard.remove(player);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		pl.dataManager.savePlayerData(player);
		pl.scoreBoard.remove(player);
	}
	
	@EventHandler
	public void onMelt(BlockFromToEvent event) {
		if(event.getToBlock().getType() == Material.WATER) {
			event.setCancelled(true);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getLabel().equalsIgnoreCase("player")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("Action impossible");
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
						pl.warnPlayer(pTo, null, player.getName());
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
						pl.warnPlayer(pTo, reason, player.getName());
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
						pl.warnPlayer(pTo, null, "Heroria");
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
						pl.warnPlayer(pTo, reason, "Heroria");
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
					pl.setRank(pTo, rank);
				} catch (NullPointerException e) {
					sender.sendMessage("§4[Heroria]§r Joueur introuvable !");
				}
			}
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
