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
import eu.heroria.npc.NpcManager;
import eu.heroria.playerdata.Faction;
import eu.heroria.playerdata.PlayerData;
import eu.heroria.playerdata.PlayerDataManager;
import eu.heroria.playerdata.Rank;
import eu.heroria.playerdata.Request;

public class Main extends JavaPlugin {
	public Request sql;
	public NpcManager npc = new NpcManager();
	public PlayerDataManager dataManager = new PlayerDataManager(this);
	public ProhibitedWordManager prohibitedWord = new ProhibitedWordManager();
	public Map<Player, PlayerData> dataPlayers = new HashMap<>();
	public Map<UUID, PermissionAttachment> playerPermission = new HashMap<>();
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new ChatManager(this), this);
		getServer().getPluginManager().registerEvents(new Listener(this), this);
		getCommand("test").setExecutor(new Listener(this));
		sql = new Request(this, "jdbc:mysql://", "localhost", "heroria", "root", "");
		sql.connection();
		prohibitedWord.addRule("bite");
	}
	
	public void onDisable() {
		for (World world : Bukkit.getServer().getWorlds()) {
			for(Entity entity : world.getEntities()) {
				entity.remove();
			}
		}
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.kickPlayer(getServer().getShutdownMessage());
		}
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
	
	public void setPrimaryQuest(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setPrimaryQuest(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getPrimaryQuest(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getPrimaryQuest();
		}
		return 0;
	}
	
	public void setPrimaryQuestStat(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setPrimaryQuestStat(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getPrimaryQuestStat(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getPrimaryQuestStat();
		}
		return 0;
	}
	
	public void setSecondaryQuest1(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setSecondaryQuest1(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getSecondaryQuest1(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getSecondaryQuest1();
		}
		return 0;
	}
	
	public void setSecondaryQuest1Stat(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setSecondaryQuest1Stat(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getSecondaryQuest1Stat(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getSecondaryQuest1Stat();
		}
		return 0;
	}
	
	public void setSecondaryQuest2(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setSecondaryQuest2(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getSecondaryQuest2(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getSecondaryQuest2();
		}
		return 0;
	}
	
	public void setSecondaryQuest2Stat(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setSecondaryQuest2Stat(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getSecondaryQuest2Stat(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getSecondaryQuest2Stat();
		}
		return 0;
	}
	
	public void setSecondaryQuest3(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setSecondaryQuest3(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getSecondaryQuest3(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getSecondaryQuest3();
		}
		return 0;
	}
	
	public void setSecondaryQuest3Stat(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setSecondaryQuest3Stat(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getSecondaryQuest3Stat(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getSecondaryQuest3Stat();
		}
		return 0;
	}
	
	public void setSecretQuest(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setSecretQuest(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getSecretQuest(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getSecretQuest();
		}
		return 0;
	}
	
	public void setSecretQuestStat(Player player, int power) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			playerData.setSecretQuestStat(power);
			dataPlayers.remove(player);
			dataPlayers.put(player, playerData);
		}
	}
	
	public int getSecretQuestStat(Player player) {
		if(dataPlayers.containsKey(player)) {
			PlayerData playerData = dataPlayers.get(player);
			return playerData.getSecretQuestStat();
		}
		return 0;
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
		player.kickPlayer("Vous avez été bannis " + duration + " jour(s) pour " + reason + " par " + by + ".");
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
		else player.sendTitle("Avertissement", "Vous avez reçu un avertissement pour:" + reason + ".");
		System.out.println("Player " + player.getDisplayName() + " (UUID: " + player.getUniqueId().toString() + ") has been warned by " + by + " for the following reason: '" + reason + "'.");
	}
}
