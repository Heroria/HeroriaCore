package eu.heroria.heroriacore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.heroria.heroriacore.playerdata.Faction;
import eu.heroria.heroriacore.playerdata.PlayerData;
import eu.heroria.heroriacore.playerdata.Rank;

public class Request {
	private Connection connection;
	private String urlbase, host, database, user, pass;
	private HeroriaCore pl;
	
	public Request(HeroriaCore pl, String urlbase, String host, String database, String user, String pass) {
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
		if(connection == null) return false;
		try {
			return connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
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
                player.sendMessage(new String[] {" ", " ", "Bienvenu sur Heroria !", "Pour obtenir des informations concernant les commandes, faites /help.", "Bon jeu !", " ", " "});
            } catch (SQLException e) {
                Bukkit.getLogger().info("Trying to reconnect to the database...");
                disconnect();
                connection();
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
                } catch (SQLException e2) {
                	e2.printStackTrace();
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
                }
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
            Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
            try {
                PreparedStatement q = connection.prepareStatement("SELECT uuid FROM players WHERE uuid = ?");
                q.setString(1, player.getUniqueId().toString());
                ResultSet resultat = q.executeQuery();
                boolean hasAccount = resultat.next();
                q.close();
                return hasAccount;
            } catch (SQLException e2) {
            	e2.printStackTrace();
            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
            }
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
				Bukkit.getLogger().info("Trying to reconnect to the database...");
	            disconnect();
	            connection();
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
				} catch (SQLException e2) {
					e2.printStackTrace();
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
				}
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
				Bukkit.getLogger().info("Trying to reconnect to the database...");
	            disconnect();
	            connection();
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
				} catch (SQLException e2) {
					e2.printStackTrace();
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
				}
			}
		}
	}
	
	public void requestFriend(Player player, String requestTo) {
		try {
			PreparedStatement q = connection.prepareStatement("INSERT INTO friends(player_1, player_2, stat) VALUES(?, ?, ?)");
			q.setString(1, player.getUniqueId().toString());
			q.setString(2, requestTo);
			q.setInt(3, 0);
			q.execute();
			q.close();
		} catch (SQLException e) {
			Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
            try {
    			PreparedStatement q = connection.prepareStatement("INSERT INTO friends(player_1, player_2, stat) VALUES(?, ?, ?)");
    			q.setString(1, player.getUniqueId().toString());
    			q.setString(2, requestTo);
    			q.setInt(3, 0);
    			q.execute();
    			q.close();
    		} catch (SQLException e2) {
    			e2.printStackTrace();
    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    		}
		}
	}
	
	public void acceptFriendRequest(String requestFrom, Player requestTo) {
		try {
			PreparedStatement q = connection.prepareStatement("UPDATE friends SET stat = ? WHERE player_1 = ? AND player_2 = ?");
			q.setInt(1, 1);
			q.setString(2, requestFrom);
			q.setString(3, requestTo.getUniqueId().toString());
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
			try {
				PreparedStatement q = connection.prepareStatement("UPDATE friends SET stat = ? WHERE player_1 = ? AND player_2 = ?");
				q.setInt(1, 1);
				q.setString(2, requestFrom);
				q.setString(3, requestTo.getUniqueId().toString());
				q.executeUpdate();
				q.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			}
		}
		
	}
	
	public void removeFriend(String player1, String player2) {
		try {
			PreparedStatement q = connection.prepareStatement("DELETE FROM friends WHERE (player_1 = ? AND player_2 = ?) OR (player_1 = ? AND player_2 = ?)");
			q.setString(1, player1);
			q.setString(2, player2);
			q.setString(3, player2);
			q.setString(4, player1);
			q.execute();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getFriendsList(Player player) {
		ArrayList<String> list = new ArrayList<>();
		int current = 0;
		String currentPlayer1 = null;
		String currentPlayer2 = null;
		try {
			PreparedStatement q = connection.prepareStatement("SELECT player_1, player_2 FROM friends WHERE (player_1 = ? OR player_2 = ?) AND stat = ?");
			q.setString(1, player.getUniqueId().toString());
			q.setString(2, player.getUniqueId().toString());
			q.setInt(3, 1);
			ResultSet rs = q.executeQuery();
			while(rs.next()) {
				currentPlayer1 = rs.getString("player_1");
				currentPlayer2 = rs.getString("player_2");
				if(!(currentPlayer1.equals(player.getUniqueId().toString()))) list.add(current, currentPlayer1);
				else if(!(currentPlayer2.equals(player.getUniqueId().toString()))) list.add(current, currentPlayer2);
				current = current + 1;
			}
			q.close();
		} catch (SQLException e) {
			Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
            try {
    			PreparedStatement q = connection.prepareStatement("SELECT player_1, player_2 FROM friends WHERE (player_1 = ? OR player_2 = ?) AND stat = ?");
    			q.setString(1, player.getUniqueId().toString());
    			q.setString(2, player.getUniqueId().toString());
    			q.setInt(3, 1);
    			ResultSet rs = q.executeQuery();
    			while(rs.next()) {
    				currentPlayer1 = rs.getString("player_1");
    				currentPlayer2 = rs.getString("player_2");
    				if(!(currentPlayer1.equals(player.getUniqueId().toString()))) list.add(current, currentPlayer1);
    				else if(!(currentPlayer2.equals(player.getUniqueId().toString()))) list.add(current, currentPlayer2);
    				current = current + 1;
    			}
    			q.close();
    		} catch (SQLException e2) {
    			e2.printStackTrace();
    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    		}
		}
		return list;
	}
	
	public ArrayList<String> getFriendRequest(Player player) {
		ArrayList<String> list = new ArrayList<>();
		int current = 0;
		String currentPlayer1 = null;
		try {
			PreparedStatement q = connection.prepareStatement("SELECT player_1 FROM friends WHERE player_2 = ? AND stat = ?");
			q.setString(1, player.getUniqueId().toString());
			q.setInt(2, 0);
			ResultSet rs = q.executeQuery();
			while(rs.next()) {
				currentPlayer1 = rs.getString("player_1");
				if(currentPlayer1 != player.getUniqueId().toString()) list.add(current, currentPlayer1);
				current = current + 1;
			}
			q.close();
		} catch (SQLException e) {
			Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
            try {
    			PreparedStatement q = connection.prepareStatement("SELECT player_1 FROM friends WHERE player_2 = ? AND stat = ?");
    			q.setString(1, player.getUniqueId().toString());
    			q.setInt(2, 0);
    			ResultSet rs = q.executeQuery();
    			while(rs.next()) {
    				currentPlayer1 = rs.getString("player_1");
    				if(currentPlayer1 != player.getUniqueId().toString()) list.add(current, currentPlayer1);
    				current = current + 1;
    			}
    			q.close();
    		} catch (SQLException e2) {
    			e2.printStackTrace();
    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    		}
		}
		return list;
	}
	
	public boolean isFriend(Player player1, String player2) {
		if(getFriendsList(player1).contains(player2)) return true;
		return false;
	}
	
	public boolean isRequested(Player player, String playerFrom) {
		if(getFriendRequest(player).contains(playerFrom)) return true;
		return false;
	}
	
	public void unBan(String uuid) {
		try {
			PreparedStatement q = connection.prepareStatement("UPDATE ban SET duration = ? WHERE uuid = ?");
			q.setInt(1, 0);
			q.setString(2, uuid);
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
            try {
    			PreparedStatement q = connection.prepareStatement("UPDATE ban SET duration = ? WHERE uuid = ?");
    			q.setInt(1, 0);
    			q.setString(2, uuid);
    			q.executeUpdate();
    			q.close();
    		} catch (SQLException e2) {
    			e2.printStackTrace();
    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    		}
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
            Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
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
            } catch (SQLException e2) {
            	e2.printStackTrace();
            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
            }
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
            Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
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
            } catch (SQLException e2) {
            	e2.printStackTrace();
            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
            }
        }
		return null;
	}
	
	public void ban(String uuid, int duration, String reason, Player pFrom) {
		try {
			System.out.println("Ban " + uuid + " during " + duration + "by " + pFrom.getName() + " because: " + reason + ".");
            PreparedStatement q = connection.prepareStatement("INSERT INTO ban(uuid,duration,reason,bannedby) VALUES (?,?,?,?)");
            q.setString(1, uuid);
            q.setInt(2, duration);
            q.setString(3, reason);
            q.setString(4, pFrom.getName());
            q.execute();
            q.close();
            logActionString(Action.BAN, reason, pFrom, uuid);
        } catch (SQLException e) {
            Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
            try {
    			System.out.println("Ban " + uuid + " during " + duration + "by " + pFrom.getName() + " because: " + reason + ".");
                PreparedStatement q = connection.prepareStatement("INSERT INTO ban(uuid,duration,reason,bannedby) VALUES (?,?,?,?)");
                q.setString(1, uuid);
                q.setInt(2, duration);
                q.setString(3, reason);
                q.setString(4, pFrom.getName());
                q.execute();
                q.close();
                logActionString(Action.BAN, reason, pFrom, uuid);
            } catch (SQLException e2) {
            	e2.printStackTrace();
            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
            }
        }
	}
	
	public void logAction(Action action, String label, Player player, Player pTo) {
		String uuid = "not_a_player";
		if(player != null) uuid = player.getUniqueId().toString();
		try {
            PreparedStatement q = connection.prepareStatement("INSERT INTO log(player,action,label,player_to) VALUES (?,?,?,?)");
            q.setString(1, uuid);
            q.setInt(2, action.getPower());
            q.setString(3, label);
            q.setString(4, pTo.getUniqueId().toString());
            q.execute();
            q.close();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
            try {
                PreparedStatement q = connection.prepareStatement("INSERT INTO log(player,action,label,player_to) VALUES (?,?,?,?)");
                q.setString(1, uuid);
                q.setInt(2, action.getPower());
                q.setString(3, label);
                q.setString(4, pTo.getUniqueId().toString());
                q.execute();
                q.close();
            } catch (SQLException e2) {
            	e2.printStackTrace();
            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
            }
        }
	}
	
	public void logActionString(Action action, String label, Player player, String pTo) {
		String uuid = null;
		if(player != null) uuid = player.getUniqueId().toString();
		try {
            PreparedStatement q = connection.prepareStatement("INSERT INTO ban(player,action,label,player_to) VALUES (?,?,?,?)");
            q.setString(1, uuid);
            q.setInt(2, action.getPower());
            q.setString(3, label);
            q.setString(4, pTo);
            q.execute();
            q.close();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Trying to reconnect to the database...");
            disconnect();
            connection();
            try {
                PreparedStatement q = connection.prepareStatement("INSERT INTO ban(player,action,label,player_to) VALUES (?,?,?,?)");
                q.setString(1, uuid);
                q.setInt(2, action.getPower());
                q.setString(3, label);
                q.setString(4, pTo);
                q.execute();
                q.close();
            } catch (SQLException e2) {
            	e2.printStackTrace();
            	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
            }
        }
	}
	
	public Connection getConnection() {
		return connection;
	}
}
