package eu.heroria.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import eu.heroria.Main;
import eu.heroria.playerdata.Rank;

public class CustomScoreBoardManager implements ScoreboardManager {
	private Main pl;
	private Player player;
	private Scoreboard scoreboard;
	private Objective objective;
	
	public CustomScoreBoardManager(Main pl, Player player) {
		this.pl = pl;
		this.player = player;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective("sc." + player.getName(), "dummy");
		if(pl.scoreBoard.containsKey(player)) return;
		objective.setDisplayName("§b §lHeroria");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		pl.scoreBoard.put(player, this);
	}

	@Override
	public Scoreboard getMainScoreboard() {
		return scoreboard;
	}

	@Override
	public Scoreboard getNewScoreboard() {
		// TODO Auto-generated method stub
		return null;
	}

	public void refresh() {
		for(String line : scoreboard.getEntries()) {
			if(line.equals(ChatColor.DARK_GRAY + "------------------------------§8-")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(13);
			}
			else if(line.equals(ChatColor.DARK_GRAY + "§o§k§k§o ")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(12);
			}
			else if(line.equals(ChatColor.GOLD + "§lN'oublie pas de faire un")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(11);
			}
			else if(line.equals(ChatColor.GOLD + "§ltour par la boutique !")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(10);
			}
			else if(line.equals(ChatColor.YELLOW + "§l/shop")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(9);
			}
			else if(line.equals(ChatColor.DARK_GRAY + "§o§k§k ")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(8);
			}
			else if(line.equals(ChatColor.DARK_GRAY + "-------------------------------§8")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(7);
			}
			else if(line.equals(ChatColor.DARK_GRAY + "§k§k§o§k ")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(6);
			}
			else if(line.contains("Bonjour")) {
				scoreboard.resetScores(line);
				objective.getScore(ChatColor.DARK_GREEN + "Bonjour " + ChatColor.GREEN +player.getName()).setScore(5);
			}
			else if(line.contains("Ton grade:")) {
				scoreboard.resetScores(line);
				objective.getScore(ChatColor.DARK_BLUE + "Ton grade: " + ChatColor.BLUE + ChatColor.stripColor(pl.getRank(player).getName())).setScore(4);
			}
			else if(line.contains("Ton argent:")) {
				scoreboard.resetScores(line);
				objective.getScore(ChatColor.GOLD + "Ton argent: " + ChatColor.YELLOW + pl.getMoney(player) + " HCoin(s)").setScore(3);
			}
			else if(line.contains("Ta réputation:")) {
				scoreboard.resetScores(line);
				objective.getScore(ChatColor.DARK_PURPLE + "Ta réputation: " + ChatColor.LIGHT_PURPLE + pl.getReputation(player) + " Point(s)").setScore(2);
			}
			else if(line.contains("§k§o§k§k ")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(2);
			}
			else if(line.equals(ChatColor.DARK_GRAY + "§8-------------------------------")) {
				scoreboard.resetScores(line);
				objective.getScore(line).setScore(0);
			}
		}
	}
	
	public void sendLine() {
		objective.getScore(ChatColor.DARK_GRAY + "------------------------------§8-").setScore(13);
		objective.getScore(ChatColor.DARK_GRAY + "§o§k§k§o ").setScore(12);
		objective.getScore(ChatColor.GOLD + "§lN'oublie pas de faire un").setScore(11);
		objective.getScore(ChatColor.GOLD + "§ltour par la boutique !").setScore(10);
		objective.getScore(ChatColor.YELLOW + "§l/shop").setScore(9);
		objective.getScore(ChatColor.DARK_GRAY + "§o§k§k ").setScore(8);
		objective.getScore(ChatColor.DARK_GRAY + "-------------------------------§8").setScore(7);
		objective.getScore(ChatColor.DARK_GRAY + "§k§k§o§k ").setScore(6);
		objective.getScore(ChatColor.GRAY + "Bonjour humain").setScore(5);
		objective.getScore(ChatColor.GRAY + "Ton grade: " + Rank.JOUEUR.getName()).setScore(4);
		objective.getScore(ChatColor.GRAY + "Ton argent:§r 0").setScore(3);
		objective.getScore(ChatColor.GRAY + "Ta réputation:§r 0").setScore(2);
		objective.getScore(ChatColor.DARK_GRAY + "§k§o§k§k ").setScore(1);
		objective.getScore(ChatColor.DARK_GRAY + "§8-------------------------------").setScore(0);
	}
	
	public void setScoreboard() {
		player.setScoreboard(scoreboard);
	}
}
