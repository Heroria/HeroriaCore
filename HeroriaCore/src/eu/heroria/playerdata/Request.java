package eu.heroria.playerdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

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
                PreparedStatement q = connection.prepareStatement("INSERT INTO players(uuid, balance, rank, warn, reputation, faction) VALUES (?,?,?,?,?,?)");
                q.setString(1, player.getUniqueId().toString());
                q.setInt(2, 100);
                q.setInt(3, Rank.JOUEUR.getPower());
                q.setInt(4, 0);
                q.setInt(5, 500);
                q.setInt(6, Faction.NF.getPower());
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
				PreparedStatement rs = connection.prepareStatement("SELECT balance, rank, faction, warn, reputation FROM players WHERE uuid = ?");
				rs.setString(1, player.getUniqueId().toString());
				ResultSet resultats = rs.executeQuery();
				int balance = 0;
				Rank rank = Rank.JOUEUR;
				Faction fac = Faction.NF;
				int warn = 0;
				int reputation = 0;
				while(resultats.next()) {
					balance = resultats.getInt("balance");
					rank = Rank.powerToRank(resultats.getInt("rank"));
					fac = Faction.powerToFaction(resultats.getInt("faction"));
					warn = resultats.getInt("warn");
					reputation = resultats.getInt("reputation");
				}
				PlayerData playerData = new PlayerData();
				playerData.setBalance(balance);
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
			int rank = playerData.getRank().getPower();
			int faction = playerData.getFaction().getPower();
			int warn = playerData.getWarn();
			int reputation = playerData.getReputation();
			PreparedStatement q;
			try {
				q = connection.prepareStatement("UPDATE players SET rank = ?, faction = ?, balance = ?, warn = ?, reputation = ? WHERE uuid = ?");
				q.setInt(1, rank);
				q.setInt(2, faction);
				q.setInt(3, balance);
				q.setInt(4, warn);
				q.setInt(5, reputation);
				q.setString(6, player.getUniqueId().toString());
				q.executeUpdate();
				q.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void unBan(String uuid) {
		try {
			PreparedStatement q = connection.prepareStatement("UPDATE ban SET duration = ? WHERE uuid = ?");
			q.setInt(1, 0);
			q.setString(2, uuid);
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isBanned(String uuid) {
		//SELECT
		try {
            PreparedStatement q = connection.prepareStatement("SELECT date,duration FROM ban WHERE uuid = ?");
            q.setString(1, uuid);
            ResultSet resultat = q.executeQuery();;
            Time expireDate = new Time(0);
            while(resultat.next()) {
            	expireDate.setTime(resultat.getTimestamp("date").getTime());
            	long time = resultat.getInt("duration") * 1000 * 60 * 60 * 24;
            	expireDate.setTime(time + expireDate.getTime());
            	if(expireDate.getTime() > new Date().getTime()) return true;
            }
            q.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
        return false;
	}
	
	public String getBanReason(String uuid) {
		try {
            PreparedStatement q = connection.prepareStatement("SELECT date,duration,reason FROM ban WHERE uuid = ?");
            q.setString(1, uuid);
            ResultSet resultat = q.executeQuery();
            Time expireDate = new Time(0);
            while(resultat.next()) {
            	expireDate.setTime(resultat.getTimestamp("date").getTime());
            	long time = resultat.getInt("duration") * 1000 * 60 * 60 * 24;
            	expireDate.setTime(time + expireDate.getTime());
            	if(expireDate.getTime() > new Date().getTime()) return resultat.getString("reason");
            }
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public void ban(String uuid, int duration, String reason, String by) {
		try {
			System.out.println("Ban " + uuid + " during " + duration + "by " + by + " because: " + reason + ".");
            PreparedStatement q = connection.prepareStatement("INSERT INTO ban(uuid,duration,reason,bannedby) VALUES (?,?,?,?)");
            q.setString(1, uuid);
            q.setInt(2, duration);
            q.setString(3, reason);
            q.setString(4, by);
            q.execute();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public Connection getConnection() {
		return connection;
	}
}
