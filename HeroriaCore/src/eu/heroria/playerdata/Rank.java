package eu.heroria.playerdata;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

public enum Rank {
	JOUEUR(0, "§r", ChatColor.GRAY),
	VIP(10, "§6VIP ", ChatColor.YELLOW),
	MODO(50, "§3Modérateur ", ChatColor.AQUA),
	SM(60, "§1SuperModérateur ", ChatColor.BLUE),
	HM(90, "§4HyperModérateur ", ChatColor.RED),
	ADMINISTRATEUR(100, "§5AdministrateurOfTheDead ", ChatColor.LIGHT_PURPLE);
	
	private int power;
	private String dysplayName;
	private ChatColor colorTag;
	public static Map<Integer, Rank> grade = new HashMap<>();
	
	Rank(int power, String dysplayName, ChatColor tag){
		this.power = power;
		this.dysplayName = dysplayName;
		this.colorTag = tag;
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
	
	public static Rank powerToRank(int power) {
		return grade.get(power);
	}
}
