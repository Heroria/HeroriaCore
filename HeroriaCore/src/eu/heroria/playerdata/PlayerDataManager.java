package eu.heroria.playerdata;

import org.bukkit.entity.Player;

import eu.heroria.Main;

public class PlayerDataManager {

private Main pl;
	
	public PlayerDataManager(Main pl) {
		this.pl = pl;
	}

	public void loadPlayerData(Player player) {
		if(!pl.dataPlayers.containsKey(player)) {
			PlayerData playerData = pl.sql.createPlayerData(player);
			pl.dataPlayers.put(player, playerData);
		}
	}
	
	public void savePlayerData(Player player) {
		if(pl.dataPlayers.containsKey(player)) {
			pl.sql.updatePlayerData(player);
		}
	}

}
