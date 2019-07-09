package eu.heroria.playerdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import eu.heroria.Main;

public class Request {
	private Connection connection;
	private String urlbase, host, database, user, pass;
	private Main pl;
	
	public Request(Main pl, String urlbase, String host, String database, String user, String pass) {
		this.pl = pl;
		this.urlbase = urlbase;
		this.host = host;
		this.database = database;
		this.user = user;
		this.pass = pass;
	}
	
	public void connection() {
		if(!isConnected()) {
			try {
				connection = DriverManager.getConnection(urlbase + host + "/" + database, user, pass);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void disconnect() {
		if(isConnected()) {
			try {
				connection.close();
				System.out.println("HeroriaCore is disconnected.");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else System.out.println("HeroriaCore was already disconnected.");
	}
	
	public boolean isConnected() {
		return connection != null;
	}
	
	public void createAccount(Player player) {
		if(!hasAccount(player)) {
			//INSERT
			try {
				System.out.println("HeroriaCore is creating account of " + player.getDisplayName() + ".");
                PreparedStatement q = connection.prepareStatement("INSERT INTO players(uuid,balance,rank,primaryquest,primaryqueststat,secondaryquest1,secondaryquest1stat,secondaryquest2,secondaryquest2stat,secondaryquest3,secondaryquest3stat,secretquest,secretqueststat,warn,reputation,faction) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                q.setString(1, player.getUniqueId().toString());
                q.setInt(2, 100);
                q.setInt(3, Rank.JOUEUR.getPower());
                q.setInt(4, 0);
                q.setInt(5, 0);
                q.setInt(6, 0);
                q.setInt(7, 0);
                q.setInt(8, 0);
                q.setInt(9, 0);
                q.setInt(10, 0);
                q.setInt(11, 0);
                q.setInt(12, 0);
                q.setInt(13, 0);
                q.setInt(14, 0);
                q.setInt(15, 500);
                q.setInt(16, Faction.NF.getPower());;
                q.execute();
                q.close();
                System.out.println("HeroriaCore created account of " + player + ".");
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
	}
	
	public boolean hasAccount(Player player) {
		//SELECT
		try {
            PreparedStatement q = connection.prepareStatement("SELECT uuid FROM players WHERE uuid = ?");
            q.setString(1, player.getUniqueId().toString());
            ResultSet resultat = q.executeQuery();
            boolean hasAccount = resultat.next();
            q.close();
            return hasAccount;
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
        return false;
	}
	
	public PlayerData createPlayerData(Player player) {
		if(!pl.dataPlayers.containsKey(player)) {
			try {
				PreparedStatement rs = connection.prepareStatement("SELECT balance, rank, faction, primaryquest, primaryqueststat, secondaryquest1, secondaryquest1stat, secondaryquest2, secondaryquest2stat, secondaryquest3, secondaryquest3stat, secretquest, secretqueststat, faction, warn, reputation FROM players WHERE uuid = ?");
				rs.setString(1, player.getUniqueId().toString());
				ResultSet resultats = rs.executeQuery();
				int balance = 0;
				int primaryQuest = 0, primaryQuestStat = 0;
				int secondaryQuest1 = 0, secondaryQuest1Stat = 0;
				int secondaryQuest2 = 0, secondaryQuest2Stat = 0;
				int secondaryQuest3 = 0, secondaryQuest3Stat = 0;
				int secretQuest = 0, secretQuestStat = 0;
				Rank rank = Rank.JOUEUR;
				Faction fac = Faction.NF;
				int warn = 0;
				int reputation = 0;
				while(resultats.next()) {
					balance = resultats.getInt("balance");
					primaryQuest = resultats.getInt("primaryQuest");
					primaryQuestStat = resultats.getInt("primaryQuestStat");
					secondaryQuest1 = resultats.getInt("secondaryQuest1");
					secondaryQuest1Stat = resultats.getInt("secondaryQuest1Stat");
					secondaryQuest2 = resultats.getInt("secondaryQuest2");
					secondaryQuest2Stat = resultats.getInt("secondaryQuest2Stat");
					secondaryQuest3 = resultats.getInt("secondaryQuest3");
					secondaryQuest3Stat = resultats.getInt("secondaryQuest3Stat");
					secretQuest = resultats.getInt("secretQuest");
					secretQuestStat = resultats.getInt("secretQuestStat");
					rank = Rank.powerToRank(resultats.getInt("rank"));
					fac = Faction.powerToFaction(resultats.getInt("faction"));
					warn = resultats.getInt("warn");
					reputation = resultats.getInt("reputation");
				}
				PlayerData playerData = new PlayerData();
				playerData.setBalance(balance);
				playerData.setPrimaryQuest(primaryQuest);
				playerData.setPrimaryQuestStat(primaryQuestStat);
				playerData.setSecondaryQuest1(secondaryQuest1);
				playerData.setSecondaryQuest1Stat(secondaryQuest1Stat);
				playerData.setSecondaryQuest2(secondaryQuest2);
				playerData.setSecondaryQuest2Stat(secondaryQuest2Stat);
				playerData.setSecondaryQuest3(secondaryQuest3);
				playerData.setSecondaryQuest3Stat(secondaryQuest3Stat);
				playerData.setSecretQuest(secretQuest);
				playerData.setSecretQuestStat(secretQuestStat);
				playerData.setRank(rank);
				playerData.setFaction(fac);
				playerData.setWarn(warn);
				playerData.setReputation(reputation);
				rs.close();
				return playerData;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new PlayerData();
			}
		}
		return null;
	}
	
	public void updatePlayerData(Player player) {
		if(pl.dataPlayers.containsKey(player)) {
			PlayerData playerData = pl.dataPlayers.get(player);
			int balance = playerData.getBalance();
			int primaryQuest = playerData.getPrimaryQuest(), primaryQuestStat = playerData.getPrimaryQuestStat();
			int secondaryQuest1 = playerData.getSecondaryQuest1(), secondaryQuest1Stat = playerData.getSecondaryQuest1Stat();
			int secondaryQuest2 = playerData.getSecondaryQuest2(), secondaryQuest2Stat = playerData.getSecondaryQuest2Stat();
			int secondaryQuest3 = playerData.getSecondaryQuest3(), secondaryQuest3Stat = playerData.getSecondaryQuest3Stat();
			int secretQuest = playerData.getSecretQuest(), secretQuestStat = playerData.getSecretQuestStat();
			int rank = playerData.getRank().getPower();
			int faction = playerData.getFaction().getPower();
			int warn = playerData.getWarn();
			int reputation = playerData.getReputation();
			PreparedStatement q;
			try {
				q = connection.prepareStatement("UPDATE players SET rank = ?, faction = ?, balance = ?, primaryQuest = ?, primaryQuestStat = ?, secondaryQuest1 = ?, secondaryQuest1Stat = ?, secondaryQuest2 = ?, secondaryQuest2Stat = ?, secondaryQuest3 = ?, secondaryQuest3Stat = ?, secretQuest = ?, secretQuestStat = ?, warn = ?, reputation = ? WHERE uuid = ?");
				q.setInt(1, rank);
				q.setInt(2, faction);
				q.setInt(3, balance);
				q.setInt(4, primaryQuest);
				q.setInt(5, primaryQuestStat);
				q.setInt(6, secondaryQuest1);
				q.setInt(7, secondaryQuest1Stat);
				q.setInt(8, secondaryQuest2);
				q.setInt(9, secondaryQuest2Stat);
				q.setInt(10, secondaryQuest3);
				q.setInt(11, secondaryQuest3Stat);
				q.setInt(12, secretQuest);
				q.setInt(13, secretQuestStat);
				q.setInt(14, warn);
				q.setInt(15, reputation);
				q.setString(16, player.getUniqueId().toString());
				q.executeUpdate();
				q.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean isBanned(Player player) {
		//SELECT
		try {
            PreparedStatement q = connection.prepareStatement("SELECT uuid FROM ban WHERE uuid = ?");
            q.setString(1, player.getUniqueId().toString());
            ResultSet resultat = q.executeQuery();
            boolean isBanned = resultat.next();
            q.close();
            return isBanned;
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
        return false;
	}
	
	public void ban(Player player, int duration, String reason, String by) {
		if(!hasAccount(player)) {
			//INSERT
			try {
				System.out.println("Ban " + player.getName() + " during " + duration + "by " + by + " because: " + reason + " ... ");
                PreparedStatement q = connection.prepareStatement("INSERT INTO players(uuid,duration,reason,bannedby) VALUES (?,?,?,?)");
                q.setString(1, player.getUniqueId().toString());
                q.setInt(2, duration);
                q.setString(3, reason);
                q.setString(4, by);
                q.execute();
                q.close();
                System.out.println(player.getName() + " was banned.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
}
