package eu.heroria.quest;

import java.util.ArrayList;

public class Quest {
	private String name;
	private ArrayList<String> description = new ArrayList<String>();
	private ArrayList<String> objective = new ArrayList<String>();
	private QuestExecutor executor;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<String> getDescription() {
		return description;
	}
	
	public void setDescription(ArrayList<String> description) {
		this.description = description;
	}
	
	public ArrayList<String> getObjective() {
		return objective;
	}
	
	public void setObjective(ArrayList<String> objective) {
		this.objective = objective;
	}
	
	public QuestExecutor getQuestExecutor() {
		return executor;
	}
	
	public void setQuestExecutor(QuestExecutor executor) {
		this.executor = executor;
	}
}
