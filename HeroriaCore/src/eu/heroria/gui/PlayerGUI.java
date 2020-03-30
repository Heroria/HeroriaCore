package eu.heroria.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import eu.heroria.Main;
import eu.heroria.playerdata.Faction;
import eu.heroria.playerdata.Rank;

public class PlayerGUI implements Listener {
	private Main pl;
	
	public PlayerGUI(Main pl) {
		this.pl = pl;
	}
	
	public void openPlayerInterface(Player player, Player pTo) {
		player.openInventory(userInterface(pTo));
	}
	
	public Inventory userInterface(Player player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		Inventory inv = Bukkit.createInventory(null, 27, "§1Modération - " + player.getName());
		inv.setItem(0, setIS("Grade", Material.CHAINMAIL_CHESTPLATE, pl.getRank(player).getName()));
		if(pl.getFaction(player) == Faction.NF) inv.setItem(1, setIS("Faction", Material.WHITE_BANNER, pl.getFaction(player).getName()));
		else if(pl.getFaction(player) == Faction.ALPHA) inv.setItem(1, setIS("Faction", Material.RED_BANNER, pl.getFaction(player).getName()));
		else if(pl.getFaction(player) == Faction.OMEGA) inv.setItem(1, setIS("Faction", Material.BLUE_BANNER, pl.getFaction(player).getName()));
		inv.setItem(2, setIS("Argent", Material.GOLD_INGOT, pl.getMoney(player) + " HCoin(s)"));
		inv.setItem(3, setIS("Réputation", Material.SUNFLOWER, pl.getReputation(player) + " point(s)"));
		inv.setItem(4, setIS("Avertissement", Material.FIREWORK_ROCKET, pl.getWarn(player) + " avertissement(s)"));
		inv.setItem(5, setIS("Sanction", Material.DIAMOND_SWORD, "Aucune actuellement"));
		inv.setItem(18, skull);
		inv.setItem(22, setIS("§1Modération", Material.COMMAND_BLOCK, null));
		inv.setItem(26, setIS("Quitter", Material.SPRUCE_DOOR, "Quitter l'interface"));
		return inv;
	}
	
	public Inventory rankInterface(Player player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		Inventory inv = Bukkit.createInventory(null, 27, "§1Grade - " + player.getName());
		inv.setItem(0, setIS(Rank.JOUEUR.getName(), Material.WOODEN_SWORD, "Définir l'utilisateur en tant que " + Rank.JOUEUR.getName()));
		inv.setItem(1, setIS(Rank.VIP1.getName(), Material.STONE_SWORD, "Définir l'utilisateur en tant que " + Rank.VIP1.getName()));
		inv.setItem(2, setIS(Rank.VIP2.getName(), Material.IRON_SWORD, "Définir l'utilisateur en tant que " + Rank.VIP2.getName()));
		inv.setItem(3, setIS(Rank.VIP3.getName(), Material.GOLDEN_SWORD, "Définir l'utilisateur en tant que " + Rank.VIP3.getName()));
		inv.setItem(4, setIS(Rank.VIP4.getName(), Material.DIAMOND_SWORD, "Définir l'utilisateur en tant que " + Rank.VIP4.getName()));
		inv.setItem(18, skull);
		inv.setItem(22, setIS("§1Grade", Material.COMMAND_BLOCK, pl.getRank(player).getName()));
		inv.setItem(26, setIS("Retour", Material.SPRUCE_DOOR, "Retournez à la page d'accueil"));
		return inv;
	}
	
	public Inventory factionInterface(Player player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		Inventory inv = Bukkit.createInventory(null, 27, "§1Faction - " + player.getName());
		inv.setItem(0, setIS(Faction.ALPHA.getName(), Material.RED_BANNER, "Placer l'utilisateur dans la faction Alpha"));
		inv.setItem(1, setIS(Faction.OMEGA.getName(), Material.BLUE_BANNER, "Placer le joueur dans la faction Omega"));
		inv.setItem(2, setIS("Aucune", Material.WHITE_BANNER, "Sortir le joueur de toutes factions"));
		inv.setItem(18, skull);
		inv.setItem(22, setIS("§1Faction", Material.COMMAND_BLOCK, pl.getFaction(player).getName()));
		inv.setItem(26, setIS("Retour", Material.SPRUCE_DOOR, "Retournez à la page d'accueil"));
		return inv;
	}
	
	public Inventory moneyInterface(Player player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		Inventory inv = Bukkit.createInventory(null, 27, "§1Argent - " + player.getName());
		inv.setItem(3, setIS("+1", Material.GREEN_STAINED_GLASS_PANE, "Ajouter 1 HCoin à l'utilisateur"));
		inv.setItem(2, setIS("+10", Material.GREEN_STAINED_GLASS_PANE, "Ajouter 10 HCoins à l'utilisateur"));
		inv.setItem(1, setIS("+100", Material.GREEN_STAINED_GLASS_PANE, "Ajouter 100 HCoins à l'utilisateur"));
		inv.setItem(5, setIS("-1", Material.RED_STAINED_GLASS_PANE, "Retirer 1 HCoin à l'utilisateur"));
		inv.setItem(6, setIS("-10", Material.RED_STAINED_GLASS_PANE, "Retirer 10 HCoins à l'utilisateur"));
		inv.setItem(7, setIS("-100", Material.RED_STAINED_GLASS_PANE, "Retirer 100 HCoins à l'utilisateur"));
		inv.setItem(18, skull);
		inv.setItem(22, setIS("§1Argent", Material.COMMAND_BLOCK, pl.getMoney(player) + " HCoin(s)"));
		inv.setItem(26, setIS("Retour", Material.SPRUCE_DOOR, "Retournez à la page d'accueil"));
		return inv;
	}
	
	public Inventory reputationInterface(Player player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		Inventory inv = Bukkit.createInventory(null, 27, "§1Réputation - " + player.getName());
		inv.setItem(3, setIS("+1", Material.GREEN_STAINED_GLASS_PANE, "Ajouter 1 points de réputation à l'utilisateur"));
		inv.setItem(2, setIS("+10", Material.GREEN_STAINED_GLASS_PANE, "Ajouter 10 points de réputation à l'utilisateur"));
		inv.setItem(1, setIS("+100", Material.GREEN_STAINED_GLASS_PANE, "Ajouter 100 points de réputation à l'utilisateur"));
		inv.setItem(5, setIS("-1", Material.RED_STAINED_GLASS_PANE, "Retirer 1 point de réputation à l'utilisateur"));
		inv.setItem(6, setIS("-10", Material.RED_STAINED_GLASS_PANE, "Retirer 10 points de réputation à l'utilisateur"));
		inv.setItem(7, setIS("-100", Material.RED_STAINED_GLASS_PANE, "Retirer 100 points de réputation à l'utilisateur"));
		inv.setItem(18, skull);
		inv.setItem(22, setIS("§1Réputation", Material.COMMAND_BLOCK, pl.getReputation(player) + " point(s)"));
		inv.setItem(26, setIS("Retour", Material.SPRUCE_DOOR, "Retournez à la page d'accueil"));
		return inv;
	}
	
	public Inventory warnInterface(Player player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		Inventory inv = Bukkit.createInventory(null, 27, "§1Avertissement - " + player.getName());
		inv.setItem(0, setIS("Avertir", Material.FIREWORK_ROCKET, "Mettre un avertissement à ce joueur"));
		inv.setItem(18, skull);
		inv.setItem(22, setIS("§1Avertissement", Material.COMMAND_BLOCK, pl.getWarn(player) + " avertissement(s)"));
		inv.setItem(26, setIS("Retour", Material.SPRUCE_DOOR, "Retournez à la page d'accueil"));
		return inv;
	}
	
	public Inventory sanctionInterface(Player player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		Inventory inv = Bukkit.createInventory(null, 27, "§1Sanction - " + player.getName());
		inv.setItem(0, setIS("Anti-jeu", Material.TNT, "Bannir l'utilisateur 1 jour"));
		inv.setItem(1, setIS("Insulte", Material.OAK_SIGN, "Bannir l'utilisateur 1 jour"));
		inv.setItem(2, setIS("Insulte Staff", Material.DARK_OAK_SIGN, "Bannir l'utilisateur 5 jours"));
		inv.setItem(3, setIS("Skin/Pseudo invalide", Material.NAME_TAG, "Bannir l'utilisateur 5 jours"));
		inv.setItem(4, setIS("Cheat", Material.CLOCK, "Bannir l'utilisateur 30 jours"));
		inv.setItem(5, setIS("Autre", Material.WRITABLE_BOOK, "Bannir l'utilisateur 1 jour"));
		inv.setItem(6, setIS("Autre", Material.WRITABLE_BOOK, "Bannir l'utilisateur 5 jours"));
		inv.setItem(7, setIS("Autre", Material.WRITABLE_BOOK, "Bannir l'utilisateur 30 jours"));
		inv.setItem(18, skull);
		inv.setItem(22, setIS("§1Sanction", Material.COMMAND_BLOCK, "Aucune actuellement"));
		inv.setItem(26, setIS("Retour", Material.SPRUCE_DOOR, "Retournez à la page d'accueil"));
		return inv;
	}
	
	public Inventory sanctionConfirm(Player player, int id) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		String sanction = null;
		if(id == 0) sanction = "§1Anti-jeu - 1 jour";
		else if(id == 1) sanction = "§1Insulte - 1 jour";
		else if(id == 2) sanction = "§1Insulte Staff - 5 jours";
		else if(id == 3) sanction = "§1Skin/Pseudo invalide - 5 jours";
		else if(id == 4) sanction = "§1Cheat - 30 jours";
		else if(id == 5) sanction = "§1Motif Indéfini - 1 jour";
		else if(id == 6) sanction = "§1Motif Indéfini - 5 jours";
		else if(id == 7) sanction = "§1Motif Indéfini - 30 jours";
				
		Inventory inv = Bukkit.createInventory(null, 27, "§1Sanction - " + player.getName());
		inv.setItem(3, setIS("Confirmer", Material.GREEN_DYE, "Confirmer la sanction"));
		inv.setItem(5, setIS("Annuler", Material.BARRIER, "Annuler la sanction"));
		inv.setItem(18, skull);
		inv.setItem(22, setIS("§1Confirmer la Sanction", Material.COMMAND_BLOCK, sanction));
		inv.setItem(26, setIS("Retour", Material.SPRUCE_DOOR, "Retournez à la page d'accueil"));
		return inv;
	}
	
	@EventHandler
	public void onInterract(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		Player pTo;
		if(item == null) return;
		else if (item.getType() == Material.AIR) return;
		Inventory inv = event.getInventory();
		if(inv.getSize() < 27) return;
		try {
			pTo = Bukkit.getPlayer(inv.getItem(18).getItemMeta().getDisplayName());
		} catch (NullPointerException e) {
			return;
		}
		if(pl.getRank(player).getPower() < pl.getRank(pTo).getPower()) {
			pl.heroriaMessage("Vous n'avez pas la permission d'exécuter cette commande !", player);
			return;
		}
		if(inv.getItem(22).getItemMeta().getDisplayName().equals("§1Modération")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Modération")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Grade")) {
				player.openInventory(rankInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Faction")) {
				player.openInventory(factionInterface(pTo));
			}	
			else if(item.getItemMeta().getDisplayName().equals("Argent")) {
				player.openInventory(moneyInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Réputation")) {
				player.openInventory(reputationInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Avertissement")) {
				player.openInventory(warnInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Sanction")) {
				player.openInventory(sanctionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Quitter")) {
				player.closeInventory();
			}
		}
		else if(inv.getItem(22).getItemMeta().getDisplayName().equals("§1Grade")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Grade")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals(Rank.JOUEUR.getName())) {
				pl.setRank(pTo, Rank.JOUEUR);
				pl.heroriaMessage("Le grade " + Rank.JOUEUR.getName() + " a été défini pour l'utilisateur " + pTo.getName() + ".", player);
				player.openInventory(rankInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals(Rank.VIP1.getName())) {
				pl.setRank(pTo, Rank.VIP1);
				pl.heroriaMessage("Le grade " + Rank.VIP1.getName() + " a été défini pour l'utilisateur " + pTo.getName() + ".", player);
				player.openInventory(rankInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals(Rank.VIP2.getName())) {
				pl.setRank(pTo, Rank.VIP2);
				pl.heroriaMessage("Le grade " + Rank.VIP2.getName() + " a été défini pour l'utilisateur " + pTo.getName() + ".", player);
				player.openInventory(rankInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals(Rank.VIP3.getName())) {
				pl.setRank(pTo, Rank.VIP3);
				pl.heroriaMessage("Le grade " + Rank.VIP3.getName() + " a été défini pour l'utilisateur " + pTo.getName() + ".", player);
				player.openInventory(rankInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals(Rank.VIP4.getName())) {
				pl.setRank(pTo, Rank.VIP4);
				pl.heroriaMessage("Le grade " + Rank.VIP4.getName() + " a été défini pour l'utilisateur " + pTo.getName() + ".", player);
				player.openInventory(rankInterface(pTo));
			}
		}
		else if(inv.getItem(22).getItemMeta().getDisplayName().equals("§1Faction")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Faction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals(Faction.ALPHA.getName())) {
				pl.setFaction(pTo, Faction.ALPHA);
				pl.heroriaMessage("La faction " + Faction.ALPHA.getName() + " a été défini pour l'utilisateur " + pTo.getName() + ".", player);
				player.openInventory(factionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals(Faction.OMEGA.getName())) {
				pl.setFaction(pTo, Faction.OMEGA);
				pl.heroriaMessage("La faction " + Faction.OMEGA.getName() + " a été défini pour l'utilisateur " + pTo.getName() + ".", player);
				player.openInventory(factionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals(Faction.NF.getName())) {
				pl.setFaction(pTo, Faction.NF);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien été retiré de toutes factions.", player);
				player.openInventory(factionInterface(pTo));
			}
		}
		else if(inv.getItem(22).getItemMeta().getDisplayName().equals("§1Argent")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Argent")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("+1")) {
				pl.setMoney(pTo, pl.getMoney(pTo) + 1);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien reçu 1 HCoin.", player);
				player.openInventory(moneyInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("+10")) {
				pl.setMoney(pTo, pl.getMoney(pTo) + 10);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien reçu 10 HCoins.", player);
				player.openInventory(moneyInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("+100")) {
				pl.setMoney(pTo, pl.getMoney(pTo) + 100);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien reçu 100 HCoins.", player);
				player.openInventory(moneyInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("-1")) {
				if((pl.getMoney(pTo) - 1) < 0) {
					pl.heroriaMessage("Le joueur n'a pas assez d'argent !", player);
					return;
				}
				pl.setMoney(pTo, pl.getMoney(pTo) - 1);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien perdu 1 HCoin.", player);
				player.openInventory(moneyInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("-10")) {
				if((pl.getMoney(pTo) - 10) < 0) {
					pl.heroriaMessage("Le joueur n'a pas assez d'argent !", player);
					return;
				}
				pl.setMoney(pTo, pl.getMoney(pTo) - 10);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien perdu 10 HCoins.", player);
				player.openInventory(moneyInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("-100")) {
				if((pl.getMoney(pTo) - 100) < 0) {
					pl.heroriaMessage("Le joueur n'a pas assez d'argent !", player);
					return;
				}
				pl.setMoney(pTo, pl.getMoney(pTo) - 100);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien perdu 100 HCoins.", player);
				player.openInventory(moneyInterface(pTo));
			}
		}
		else if(inv.getItem(22).getItemMeta().getDisplayName().equals("§1Réputation")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Réputation")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("+1")) {
				pl.setReputation(pTo, pl.getReputation(pTo) + 1);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien reçu 1 point de réputation.", player);
				player.openInventory(reputationInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("+10")) {
				pl.setReputation(pTo, pl.getReputation(pTo) + 10);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien reçu 10 points de réputation.", player);
				player.openInventory(reputationInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("+100")) {
				pl.setReputation(pTo, pl.getReputation(pTo) + 100);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien reçu 100 points de réputation.", player);
				player.openInventory(reputationInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("-1")) {
				pl.setReputation(pTo, pl.getReputation(pTo) - 1);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien perdu 1 point de réputation.", player);
				player.openInventory(reputationInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("-10")) {
				pl.setReputation(pTo, pl.getReputation(pTo) - 10);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien perdu 10 points de réputation.", player);
				player.openInventory(reputationInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("-100")) {
				pl.setReputation(pTo, pl.getReputation(pTo) - 100);
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien perdu 100 points de réputation.", player);
				player.openInventory(reputationInterface(pTo));
			}
		}
		else if(inv.getItem(22).getItemMeta().getDisplayName().equals("§1Avertissement")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Avertissement")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Avertir")) {
				pl.warnPlayer(pTo, null, player.getCustomName());
				pl.heroriaMessage("L'utilisateur " + pTo.getName() + " a bien reçu un avertissement.", player);
				player.openInventory(warnInterface(pTo));
			}
		}
		else if(inv.getItem(22).getItemMeta().getDisplayName().equals("§1Sanction")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Sanction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Anti-jeu")) {
				player.openInventory(sanctionConfirm(pTo, 0));
			}
			else if(item.getItemMeta().getDisplayName().equals("Insulte")) {
				player.openInventory(sanctionConfirm(pTo, 1));
			}
			else if(item.getItemMeta().getDisplayName().equals("Insulte Staff")) {
				player.openInventory(sanctionConfirm(pTo, 2));
			}
			else if(item.getItemMeta().getDisplayName().equals("Skin/Pseudo invalide")) {
				player.openInventory(sanctionConfirm(pTo, 3));
			}
			else if(item.getItemMeta().getDisplayName().equals("Cheat")) {
				player.openInventory(sanctionConfirm(pTo, 4));
			}
			else if(item.getItemMeta().getDisplayName().equals("Autre")) {
				if(item.getItemMeta().getLore().contains("Bannir l'utilisateur 1 jour")) {
					player.openInventory(sanctionConfirm(pTo, 5));
				}
				else if(item.getItemMeta().getLore().contains("Bannir l'utilisateur 5 jours")) {
					player.openInventory(sanctionConfirm(pTo, 6));
				}
				else if(item.getItemMeta().getLore().contains("Bannir l'utilisateur 30 jours")) {
					player.openInventory(sanctionConfirm(pTo, 7));
				}
			}
			
		}
		else if(inv.getItem(22).getItemMeta().getLore().contains("§1Anti-jeu - 1 jour")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Confirmer la Sanction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Annuler")) {
				player.openInventory(sanctionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Confirmer")) {
				pl.ban(pTo, 1, "Anti-jeu", player.getName());
				pl.heroriaMessage("Le joueur " + pTo.getName() + " a bien été banni.", player);
			}
		}
		else if(inv.getItem(22).getItemMeta().getLore().contains("§1Insulte - 1 jour")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Confirmer la Sanction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Annuler")) {
				player.openInventory(sanctionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Confirmer")) {
				pl.ban(pTo, 5, "Insulte", player.getName());
				pl.heroriaMessage("Le joueur " + pTo.getName() + " a bien été banni.", player);
			}
		}
		else if(inv.getItem(22).getItemMeta().getLore().contains("§1Insulte Staff - 5 jours")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Confirmer la Sanction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Annuler")) {
				player.openInventory(sanctionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Confirmer")) {
				pl.ban(pTo, 5, "Insulte Staff", player.getName());
				pl.heroriaMessage("Le joueur " + pTo.getName() + " a bien été banni.", player);
			}
		}
		else if(inv.getItem(22).getItemMeta().getLore().contains("§1Skin/Pseudo invalide - 5 jours")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Confirmer la Sanction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Annuler")) {
				player.openInventory(sanctionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Confirmer")) {
				pl.ban(pTo, 5, "Skin/Pseudo invalide", player.getName());
				pl.heroriaMessage("Le joueur " + pTo.getName() + " a bien été banni.", player);
			}
		}
		else if(inv.getItem(22).getItemMeta().getLore().contains("§1Cheat - 30 jours")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Confirmer la Sanction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Annuler")) {
				player.openInventory(sanctionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Confirmer")) {
				pl.ban(pTo, 30, "Cheat", player.getName());
				pl.heroriaMessage("Le joueur " + pTo.getName() + " a bien été banni.", player);
			}
		}
		else if(inv.getItem(22).getItemMeta().getLore().contains("§1Motif Indéfini - 1 jour")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Confirmer la Sanction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Annuler")) {
				player.openInventory(sanctionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Confirmer")) {
				pl.ban(pTo, 1, "Motif Indéfini", player.getName());
				pl.heroriaMessage("Le joueur " + pTo.getName() + " a bien été banni.", player);
			}
		}
		else if(inv.getItem(22).getItemMeta().getLore().contains("§1Motif Indéfini - 5 jours")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Confirmer la Sanction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Annuler")) {
				player.openInventory(sanctionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Confirmer")) {
				pl.ban(pTo, 5, "Motif Indéfini", player.getName());
				pl.heroriaMessage("Le joueur " + pTo.getName() + " a bien été banni.", player);
			}
		}
		else if(inv.getItem(22).getItemMeta().getLore().contains("§1Motif Indéfini - 30 jours")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("§1Confirmer la Sanction")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(pTo.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Retour")) {
				player.openInventory(userInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Annuler")) {
				player.openInventory(sanctionInterface(pTo));
			}
			else if(item.getItemMeta().getDisplayName().equals("Confirmer")) {
				pl.ban(pTo, 30, "Motif Indéfini", player.getName());
				pl.heroriaMessage("Le joueur " + pTo.getName() + " a bien été banni.", player);
			}
		}
	}
	
	public ItemStack setIS(String name, Material material, String lore) {
		ItemStack it = new ItemStack(material);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(name);
		itM.setLore(Arrays.asList(lore));
		it.setItemMeta(itM);
		return it;
	}
}
