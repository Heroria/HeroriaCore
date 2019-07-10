package eu.heroria.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import eu.heroria.Main;

public class QuestManager {
	public <K, V> K getKey(Map<K, V> map, V value) {
	    for (Entry<K, V> entry : map.entrySet()) {
	        if (entry.getValue().equals(value)) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	private Map<Integer, Quest> quests = new HashMap<>();
	private Main pl;
	
	public QuestManager(Main pl) {
		this.pl = pl;
	}

	public QuestExecutor getExecutor(String name) {
		for (Map.Entry<Integer, Quest> e : quests.entrySet()) {
			if(e.getValue().getName() == name) {
				return quests.get(e.getKey()).getQuestExecutor();
			} else System.out.println("HeroriaCore can't get executor of the quest " + name + ". He probably does not exist.");
		}
		return null;
	}
	
	public QuestExecutor getExecutor(int id) {
		return quests.get(id).getQuestExecutor();
	}
	
	public ArrayList<String> getDescription(String name) {
		for (Map.Entry<Integer, Quest> e : quests.entrySet()) {
			if(e.getValue().getName() == name) {
				return quests.get(e.getKey()).getDescription();
			} else System.out.println("HeroriaCore can't get description of the quest " + name + ". He probably does not exist.");
		}
		return null;
	}
	
	public ArrayList<String> getDescription(int id) {
		return quests.get(id).getDescription();
	}
	
	public ArrayList<String> getObjective(String name) {
		for (Map.Entry<Integer, Quest> e : quests.entrySet()) {
			if(e.getValue().getName() == name) {
				return quests.get(e.getKey()).getObjective();
			} else System.out.println("HeroriaCore can't get objective of the quest " + name + ". He probably does not exist.");
		}
		return null;
	}
	
	public ArrayList<String> getObjective(int id) {
		return quests.get(id).getObjective();
	}
	
	public String getName(String name) {
		for (Map.Entry<Integer, Quest> e : quests.entrySet()) {
			if(e.getValue().getName() == name) {
				return quests.get(e.getKey()).getName();
			} else System.out.println("HeroriaCore can't get objective of the quest " + name + ". He probably does not exist.");
		}
		return null;
	}
	
	public String getName(int id) {
		return quests.get(id).getName();
	}
	
	public boolean hasQuest(Player player, Quest quest) {
		int id = getKey(quests, quest);
		if(pl.getPrimaryQuest(player) == id) return true;
		if(pl.getSecondaryQuest1(player) == id) return true;
		if(pl.getSecondaryQuest2(player) == id) return true;
		if(pl.getSecondaryQuest3(player) == id) return true;
		if(pl.getSecretQuest(player) == id) return true;
		return false;
	}
}
