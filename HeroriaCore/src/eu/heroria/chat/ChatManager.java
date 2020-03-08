package eu.heroria.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import eu.heroria.Main;
import eu.heroria.playerdata.Faction;

public class ChatManager implements Listener {
	private Main pl;
	
	public ChatManager(Main pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String msg = event.getMessage();
		if(pl.prohibitedWord.containsProhibitedWord(msg)) {
			event.setCancelled(true);
			pl.heroriaMessage("Votre message ne peut pas être envoyé, il contient des éléments prohibé.", player);
			pl.warnPlayer(player, "message prohibé", "HeroriaCore");
		}
		if(pl.getFaction(player) == Faction.NF) event.setFormat(pl.getRank(player).getName() + pl.getRank(player).getTag() + " " + player.getCustomName() + "§8\\§r " + msg);
		else {
			String faction = "§r[" + pl.getFaction(player).getTag() + pl.getFaction(player).getName() + "§r] ";
			if(pl.getSeeFaction()) event.setFormat(faction + pl.getRank(player).getName() + pl.getRank(player).getTag() + " " + player.getCustomName() + "§8\\§r " + msg);
			else event.setFormat(pl.getRank(player).getName() + pl.getRank(player).getTag() + " " + player.getCustomName() + "§8\\§r " + msg);
		}
	}
}