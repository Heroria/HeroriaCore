package eu.heroria.playerdata;

public class PlayerData {
	private int balance;
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
