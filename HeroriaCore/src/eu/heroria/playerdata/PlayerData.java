package eu.heroria.playerdata;

public class PlayerData {
	private int balance;
	private int primaryQuest, primaryQuestStat;
	private int secondaryQuest1, secondaryQuest1Stat;
	private int secondaryQuest2, secondaryQuest2Stat;
	private int secondaryQuest3, secondaryQuest3Stat;
	private int secretQuest, secretQuestStat;
	private int warn;
	private int reputation;
	private Rank rank;
	private Faction fac;
	
	public int getBalance() {
		return balance;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	 public Faction getFaction() {
		 return fac;
	 }
	 
	 public void setFaction(Faction fac) {
		 this.fac = fac;
	 }
	 
	 public int getPrimaryQuest() {
		 return primaryQuest;
	 }
	 
	 public void setPrimaryQuest(int value) {
		 this.primaryQuest = value;
	 }
	 
	 public int getPrimaryQuestStat() {
		 return primaryQuestStat;
	 }
	 
	 public void setPrimaryQuestStat(int value) {
		 this.primaryQuestStat = value;
	 }
	 
	 public int getSecondaryQuest1() {
		 return secondaryQuest1;
	 }
	 
	 public void setSecondaryQuest1(int value) {
		 this.secondaryQuest1 = value;
	 }
	 
	 public int getSecondaryQuest1Stat() {
		 return secondaryQuest1Stat;
	 }
	 
	 public void setSecondaryQuest1Stat(int value) {
		 this.secondaryQuest1Stat = value;
	 }
	 
	 public int getSecondaryQuest2() {
		 return secondaryQuest2;
	 }
	 
	 public void setSecondaryQuest2(int value) {
		 this.secondaryQuest2 = value;
	 }
	 
	 public int getSecondaryQuest2Stat() {
		 return secondaryQuest2Stat;
	 }
	 
	 public void setSecondaryQuest2Stat(int value) {
		 this.secondaryQuest2Stat = value;
	 }
	 
	 public int getSecondaryQuest3() {
		 return secondaryQuest3;
	 }
	 
	 public void setSecondaryQuest3(int value) {
		 this.secondaryQuest3 = value;
	 }
	 
	 public int getSecondaryQuest3Stat() {
		 return secondaryQuest3Stat;
	 }
	 
	 public void setSecondaryQuest3Stat(int value) {
		 this.secondaryQuest3Stat = value;
	 }
	 
	 public int getSecretQuest() {
		 return secretQuest;
	 }
	 
	 public void setSecretQuest(int value) {
		 this.secretQuest = value;
	 }
	 
	 public int getSecretQuestStat() {
		 return secretQuestStat;
	 }
	 
	 public void setSecretQuestStat(int value) {
		 this.secretQuestStat = value;
	 }
	 
	 public int getWarn() {
		 return warn;
	 }
	 
	 public void setWarn(int warn) {
		 this.warn = warn;
	 }
	 
	 public int getReputation() {
		 return reputation;
	 }
	 
	 public void setReputation(int reputation) {
		 this.reputation = reputation;
	 }
}
