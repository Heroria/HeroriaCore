package eu.heroria.playerdata;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

public enum Faction {
	NF(0, null, null),
	ALPHA(1, "α", ChatColor.RED),
	OMEGA(2, "Ω", ChatColor.BLUE);
	
	private int power;
	private String dysplayName;
	private ChatColor colorTag;
	public static Map<Integer, Faction> fac = new HashMap<>();
	
	Faction(int power, String dysplayName, ChatColor tag){
		this.power = power;
		this.dysplayName = dysplayName;
		this.colorTag = tag;
	}
	
	static {
		for(Faction r : Faction.values()) {
			fac.put(r.getPower(), r);
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
	
	public static Faction powerToFaction(int power) {
		return fac.get(power);
	}
}
