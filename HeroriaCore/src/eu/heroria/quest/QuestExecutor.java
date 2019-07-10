package eu.heroria.quest;

public interface QuestExecutor {
	public void onStart();
	public void onFinish();
	public void onEnable();
	//public void onLoot(Player event);
	public void onDead();
}
