package eu.heroria;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.heroria.gui.CustomScoreBoardManager;

public class Refresh extends BukkitRunnable {
	private Main pl;
	
	public Refresh(Main pl) {
		this.pl = pl;
	}

	@Override
	public void run() {
		for(Entry<Player, CustomScoreBoardManager> sc : pl.scoreBoard.entrySet()) {
			CustomScoreBoardManager boardManager = sc.getValue();
			boardManager.refresh();
		}
		
	}
}
