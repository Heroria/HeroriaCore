package eu.heroria;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import eu.heroria.chat.ChatManager;
import eu.heroria.chat.ProhibitedWordManager;
import eu.heroria.gui.CustomScoreBoardManager;
import eu.heroria.gui.PlayerGUI;
import eu.heroria.gui.ShopGUI;
import eu.heroria.playerdata.Faction;
import eu.heroria.playerdata.PlayerData;
import eu.heroria.playerdata.PlayerDataManager;
import eu.heroria.playerdata.Rank;
import eu.heroria.playerdata.Request;

public class Main extends JavaPlugin {
	private boolean seeFaction = true;
	public Request sql;
	public PlayerDataManager dataManager = new PlayerDataManager(this);
	public ProhibitedWordManager prohibitedWord = new ProhibitedWordManager();
	public PlayerGUI playerGUI = new PlayerGUI(this);
	public ShopGUI shopGUI = new ShopGUI(this);
	public Map<Player, PlayerData> dataPlayers = new HashMap<>();
	public Map<UUID, PermissionAttachment> playerPermission = new HashMap<>();
	public Map<Player, CustomScoreBoardManager> scoreBoard = new HashMap<>();
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new ChatManager(this), this);
		getServer().getPluginManager().registerEvents(new Listener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerGUI(this), this);
		getServer().getPluginManager().registerEvents(new ShopGUI(this), this);
		getCommand("rec").setExecutor(new Listener(this));
		getCommand("player").setExecutor(new Listener(this));
		getCommand("shop").setExecutor(new Listener(this));
		getCommand("warn").setExecutor(new Listener(this));
		getCommand("rank").setExecutor(new Listener(this));
		sql = new Request(this, "jdbc:mysql://", "localhost", "heroria", "root", "");
		sql.connection();
		prohibitedWord.addRule("bite");
		new Refresh(this).runTaskTimer(this, 0L, 20L);
	}
	
	public void seeFaction(boolean seeFaction) {
		this.seeFaction = seeFaction;
	}
	
	public boolean getSeeFaction() {
		return seeFaction;
	}
	
	public int getMoney(Player player) {
		//SELECT
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getBalance();
		}
		return 0;
	}
	
	public void setMoney(Player player, int amount) {
		if(dataPlayers.containsKey(player));{
			PlayerData playerData = dataPlayers.get(player);
			playerData.setBalance(amount);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public void setRank(Player player, Rank rank) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setRank(rank);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public Rank getRank(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getRank();
		}
		return Rank.JOUEUR;
	}
	
	public void setFaction(Player player, Faction fac) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setFaction(fac);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public Faction getFaction(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getFaction();
		}
		return Faction.NF;
	}
	
	public int getReputation(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getReputation();
		}
		return 500;
	}
	
	public void setReputation(Player player, int reputation) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setReputation(reputation);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getWarn(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getWarn();
		}
		return 0;
	}
	
	public void setWarn(Player player, int warn) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setWarn(warn);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public void ban(Player player, int duration, String reason, String by) {
		sql.ban(player, duration, reason, by);
		player.kickPlayer("Vous avez été bannis " + duration + " jour(s)" + " par " + by + " pour la raison suivante\n" + reason);
	}
	
	public ItemStack setIS(String name, Material material, String lore) {
		ItemStack it = new ItemStack(material);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(name);
		itM.setLore(Arrays.asList(lore));
		it.setItemMeta(itM);
		return it;
	}
	
	public void heroriaMessage(String msg, Player player) {
		player.sendMessage("§4[Heroria] §r" + msg);
	}
	
	public void warnPlayer(Player player, String reason, String by) {
		int warn = getWarn(player) + 1;
		setWarn(player, warn);
		if(reason == null) player.sendTitle("§4Avertissement", ChatColor.GOLD + "Vous avez reçu un avertissement.");
		else player.sendTitle("§4Avertissement", ChatColor.GOLD + "Vous avez reçu un avertissement pour: ' " + reason + "'.");
		System.out.println("Player " + player.getDisplayName() + " (UUID: " + player.getUniqueId().toString() + ") has been warned by " + by + " for the following reason: '" + reason + "'.");
	}
	
	public void setupPermissions(Player player) {
		permissionSetter(player);
	}

	private void permissionSetter(Player player) {
		PermissionAttachment attachment = player.addAttachment(this);
		Rank rank = getRank(player);
		switch(rank) {
			case JOUEUR:
				attachment.setPermission("heroria.player", true);
				break;
			case VIP1:
				attachment.setPermission("heroria.player", true);
				break;
			case VIP2:
				attachment.setPermission("heroria.player", true);
				break;
			case VIP3:
				attachment.setPermission("heroria.player", true);
				break;
			case VIP4:
				attachment.setPermission("heroria.player", true);
				break;
			case MODO:
				attachment.setPermission("minecraft.command.kill", true);
				attachment.setPermission("heroria.player", true);
				attachment.setPermission("heroria.modo", true);
				break;
			
			case SM:
				attachment.setPermission("minecraft.command.*", true);
				attachment.setPermission("heroria.player", true);
				attachment.setPermission("heroria.modo", true);
				break;
				
			case CM:
				attachment.setPermission("minecraft.command.*", true);
				attachment.setPermission("bukkit.command.restart", true);
				attachment.setPermission("heroria.*", true);
				break;
				
			case ADMINISTRATEUR:
				attachment.setPermission("*", true);
				break;
				
			default:
				break;
		
		}
		this.playerPermission.put(player.getUniqueId(), attachment);
	}
}
