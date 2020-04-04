package eu.heroria.heroriacore;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Action {
	BAN(1, "Ban"),
	PARDON(2, "Pardon"),
	REPUTATION(3, "Reputation"),
	MONEY(4, "Money"),
	RANK(5, "Rank"),
	GAMEMODE(6, "Gamemode"),
	WARN(7, "Warn"),
	REC(8, "Reward"),
	NULL(0, null);
	
	private int power;
	private String name;
	public static Map<Integer, Action> action = new HashMap<>();
	
	Action(int power, String name){
		this.power = power;
		this.name = name;
	}
	
	static {
		for(Action r : Action.values()) {
			action.put(r.getPower(), r);
		}
	}
	
	public int getPower() {
		return power;
	}
	
	public String getName() {
		return name;
	}
	
	public static Action powerToAction(int power) {
		return action.get(power);
	}
}
