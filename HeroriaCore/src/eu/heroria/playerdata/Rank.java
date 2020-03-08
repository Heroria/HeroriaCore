package eu.heroria.playerdata;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

public enum Rank {
	JOUEUR(0, "§rJoueur", ChatColor.GRAY, false),
	VIP1(10, "§6Maître§r", ChatColor.YELLOW, false),
	VIP2(11, "§6Héro§r", ChatColor.YELLOW , false),
	VIP3(12, "§6Paladin§r", ChatColor.YELLOW, true),
	VIP4(13, "§6Divin§r", ChatColor.YELLOW, true),
	MODO(50, "§3Modérateur§r", ChatColor.RED, true),
	SM(60, "§1SuperModérateur§r", ChatColor.RED, true),
	CM(90, "§4ChefModérateur§r", ChatColor.RED, true),
	ADMINISTRATEUR(100, "§5Administrateur§r", ChatColor.LIGHT_PURPLE, true);
	
	private int power;
	private String dysplayName;
	private ChatColor colorTag;
	private boolean joinMessage;
	public static Map<Integer, Rank> grade = new HashMap<>();
	
	Rank(int power, String dysplayName, ChatColor tag, boolean joinMessage){
		this.power = power;
		this.dysplayName = dysplayName;
		this.colorTag = tag;
		this.joinMessage = joinMessage;
	}
	
	static {
		for(Rank r : Rank.values()) {
			grade.put(r.getPower(), r);
		}
	}
	
	public int getPower() {
		return power;
	}
	
	public String getName() {
		return dysplayName;
	}
	
	public ChatColor getTag() {
		return colorTag;
	}
	
	public boolean getJoinMessage() {
		return joinMessage;
	}
	
	public static Rank powerToRank(int power) {
		return grade.get(power);
	}
}
