package eu.heroria.heroriacore;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Iterables;

import eu.heroria.heroriacore.gui.CustomScoreBoardManager;

public class Refresh extends BukkitRunnable {
	private HeroriaCore pl;
	
	public Refresh(HeroriaCore pl) {
		this.pl = pl;
	}

	@Override
	public void run() {
		for(Entry<Player, CustomScoreBoardManager> sc : pl.scoreBoard.entrySet()) {
			CustomScoreBoardManager boardManager = sc.getValue();
			boardManager.refresh();
		}
		Thread t = new Thread() {
			public void run() {
				if(Bukkit.getOnlinePlayers().size() == 0) return;
				int random = new Random().nextInt(Bukkit.getOnlinePlayers().size());
				Player player = Iterables.get(Bukkit.getOnlinePlayers(), random);
				String[] allProxiedPlayer = pl.iop.getPlayers(player, "ALL");
				pl.playerList.clear();
				for (String string : allProxiedPlayer) {
					pl.playerList.put(string, pl.iop.getServer(player, string).getStringResult());
				}
			}
		};
		t.start();
	}
}
