package eu.heroria.heroriacore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import eu.heroria.heroriacore.chat.ChatManager;
import eu.heroria.heroriacore.chat.ProhibitedWordManager;
import eu.heroria.heroriacore.gui.CustomScoreBoardManager;
import eu.heroria.heroriacore.gui.FriendGUI;
import eu.heroria.heroriacore.gui.PlayerGUI;
import eu.heroria.heroriacore.gui.ShopGUI;
import eu.heroria.heroriacore.item.ItemListener;
import eu.heroria.heroriacore.playerdata.Faction;
import eu.heroria.heroriacore.playerdata.PlayerData;
import eu.heroria.heroriacore.playerdata.PlayerDataManager;
import eu.heroria.heroriacore.playerdata.Rank;
import eu.heroria.heroriacore.proxy.InterractOverProxy;

public class HeroriaCore extends JavaPlugin {
	private boolean seeFaction = false;
	private boolean hub = false;
	public static HeroriaCore api;
	public Listener listener = new Listener(this);
	public Refresh refresh = new Refresh(this);
	public Request sql;
	public PlayerDataManager dataManager = new PlayerDataManager(this);
	public ProhibitedWordManager prohibitedWord = new ProhibitedWordManager();
	public PlayerGUI playerGUI = new PlayerGUI(this);
	public ShopGUI shopGUI = new ShopGUI(this);
	public InterractOverProxy iop = new InterractOverProxy(this);
	public FriendGUI friendGUI = new FriendGUI(this);
	public Map<Player, PlayerData> dataPlayers = new HashMap<>();
	public Map<UUID, PermissionAttachment> playerPermission = new HashMap<>();
	public Map<Player, CustomScoreBoardManager> scoreBoard = new HashMap<>();
	public Map<String, Player> teleportIOP = new HashMap<>();
	public Map<String,String> playerList = new HashMap<>();
	
	@Override
	public void onEnable() {
		FileConfiguration config = this.getConfig();
		config.addDefault("DatabaseUrlBase", "jdbc:mysql://");
		config.addDefault("DatabaseHost", "localhost");
		config.addDefault("DatabaseName", "heroria");
		config.addDefault("DatabaseUser", "root");
		config.addDefault("DatabasePass", "");
		config.options().copyDefaults(true);
		saveConfig();
		api = this;
		getServer().getPluginManager().registerEvents(new ChatManager(this), this);
		getServer().getPluginManager().registerEvents(listener, this);
		getServer().getPluginManager().registerEvents(playerGUI, this);
		getServer().getPluginManager().registerEvents(shopGUI, this);
		getServer().getPluginManager().registerEvents(friendGUI, this);
		getServer().getPluginManager().registerEvents(new ItemListener(this), this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "heroria:iop");
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "heroria:iop", iop);
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", iop);
		getCommand("rec").setExecutor(listener);
		getCommand("player").setExecutor(listener);
		getCommand("shop").setExecutor(listener);
		getCommand("warn").setExecutor(listener);
		getCommand("rank").setExecutor(listener);
		getCommand("friend").setExecutor(listener);
		getCommand("test").setExecutor(listener);
		getCommand("test2").setExecutor(listener);
		sql = new Request(this, config.getString("DatabaseUrlBase"), config.getString("DatabaseHost"), config.getString("DatabaseName"), config.getString("DatabaseUser"), config.getString("DatabasePass"));
		sql.connection();
		prohibitedWord.addRule("bite");
		refresh.runTaskTimer(this, 0L, 20L);
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
			setupPermissions(player);
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
			if(getReputation(player) < 0) {
				if(getFaction(player) != Faction.NF) {
					setFaction(player, Faction.NF);
					heroriaMessage("Vous n'avez plus assez de points de réputation. Votre faction vous a abandonné...", player);
				}
			}
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
	
	public void ban(Player player, int duration, String reason, Player pFrom) {
		sql.ban(player.getUniqueId().toString(), duration, reason, pFrom);
		player.kickPlayer("Vous avez été bannis " + duration + " jour(s)" + " par " + pFrom.getName() + " pour la raison suivante\n" + reason);
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
	
	public void warnPlayer(Player player, String reason, Player pFrom) {
		int warn = getWarn(player) + 1;
		setWarn(player, warn);
		if(reason == null) player.sendTitle("§4Avertissement", ChatColor.GOLD + "Vous avez reçu un avertissement.");
		else player.sendTitle("§4Avertissement", ChatColor.GOLD + "Vous avez reçu un avertissement pour: ' " + reason + "'.");
		sql.logAction(Action.WARN, reason, pFrom, player);
	}
	
	public void setupPermissions(Player player) {
		permissionSetter(player);
	}

	private void permissionSetter(Player player) {
		PermissionAttachment attachment = player.addAttachment(this);
		Rank rank = getRank(player);
		switch(rank) {
			case JOUEUR:
				attachment.setPermission("minecraft.command.me", false);
				attachment.setPermission("minecraft.command.msg", true);
				attachment.setPermission("minecraft.command.help", true);
				attachment.setPermission("bukkit.command.version", false);
				attachment.setPermission("bukkit.command.plugins", false);
				break;
			case VIP1:
				attachment.setPermission("minecraft.command.me", true);
				attachment.setPermission("minecraft.command.msg", true);
				attachment.setPermission("minecraft.command.help", true);
				attachment.setPermission("bukkit.command.version", false);
				attachment.setPermission("bukkit.command.plugins", false);
				break;
			case VIP2:
				attachment.setPermission("minecraft.command.me", true);
				attachment.setPermission("minecraft.command.msg", true);
				attachment.setPermission("minecraft.command.help", true);
				attachment.setPermission("bukkit.command.version", false);
				attachment.setPermission("bukkit.command.plugins", false);
				break;
			case VIP3:
				attachment.setPermission("minecraft.command.me", true);
				attachment.setPermission("minecraft.command.msg", true);
				attachment.setPermission("minecraft.command.help", true);
				attachment.setPermission("bukkit.command.version", false);
				attachment.setPermission("bukkit.command.plugins", false);
				break;
			case VIP4:
				attachment.setPermission("minecraft.command.me", true);
				attachment.setPermission("minecraft.command.msg", true);
				attachment.setPermission("minecraft.command.help", true);
				attachment.setPermission("bukkit.command.version", false);
				attachment.setPermission("bukkit.command.plugins", false);
				break;
			case MODO:
				attachment.setPermission("minecraft.command.kill", true);
				attachment.setPermission("minecraft.command.me", true);
				attachment.setPermission("minecraft.command.msg", true);
				attachment.setPermission("bukkit.command.version", false);
				attachment.setPermission("bukkit.command.plugins", false);
				attachment.setPermission("minecraft.command.tp", true);
				break;
			
			case SM:
				attachment.setPermission("minecraft.command.kill", true);
				attachment.setPermission("minecraft.command.me", true);
				attachment.setPermission("minecraft.command.msg", true);
				attachment.setPermission("minecraft.command.ban", true);
				attachment.setPermission("minecraft.comman.pardon", true);
				attachment.setPermission("minecraft.command.tp", true);
				break;
				
			case CM:
				attachment.setPermission("minecraft.command.kill", true);
				attachment.setPermission("minecraft.command.me", true);
				attachment.setPermission("minecraft.command.msg", true);
				attachment.setPermission("minecraft.command.ban", true);
				attachment.setPermission("minecraft.comman.pardon", true);
				attachment.setPermission("bukkit.command.stop", true);
				attachment.setPermission("bukkit.command.restart", true);
				attachment.setPermission("minecraft.command.tp", true);
				attachment.setPermission("minecraft.command.whitelist", true);
				break;
				
			case ADMINISTRATEUR:
				attachment.setPermission("minecraft.command.kill", true);
				attachment.setPermission("minecraft.command.me", true);
				attachment.setPermission("minecraft.command.msg", true);
				attachment.setPermission("minecraft.command.ban", true);
				attachment.setPermission("minecraft.comman.pardon", true);
				attachment.setPermission("bukkit.command.stop", true);
				attachment.setPermission("bukkit.command.restart", true);
				attachment.setPermission("minecraft.command.tp", true);
				attachment.setPermission("minecraft.command.whitelist", true);
				break;
				
			default:
				break;
		
		}
		if(this.playerPermission.containsKey(player.getUniqueId())) this.playerPermission.remove(player.getUniqueId());
		this.playerPermission.put(player.getUniqueId(), attachment);
	}

	public boolean isHub() {
		return hub;
	}

	public void setHub(boolean hub) {
		this.hub = hub;
	}
}
