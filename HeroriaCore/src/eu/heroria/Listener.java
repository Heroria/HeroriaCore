package eu.heroria;

import java.util.function.Function;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listener implements org.bukkit.event.Listener, CommandExecutor {
	private Main pl;
	
	public Listener(Main pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.setCustomName(player.getDisplayName());
		pl.sql.createAccount(player);
		pl.dataManager.loadPlayerData(player);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		pl.dataManager.savePlayerData(player);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		pl.dataManager.savePlayerData(player);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getLabel().equalsIgnoreCase("test")) {
			Player player = (Player) sender;
			Function function;
			
		}
		return false;
	}
	
	private void test(Player player) {
		player.sendMessage("ttt");
	}
}
