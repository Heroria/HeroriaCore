package eu.heroria.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import eu.heroria.Main;
import eu.heroria.playerdata.Rank;

public class ShopGUI implements Listener {
	private Main pl;
	
	public ShopGUI(Main pl) {
		this.pl = pl;
	}
	
	public void openShopInterface(Player player) {
		player.openInventory(shopInterface(player));
	}
	
	public Inventory shopInterface(Player player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		Inventory inv = Bukkit.createInventory(null, 36, "§1Boutique - " + player.getName());
		inv.setItem(0, setIS(Rank.VIP1.getName(), Material.STONE_SWORD, "999 HCoins"));
		inv.setItem(1, setIS(Rank.VIP2.getName(), Material.IRON_SWORD, "1999 HCoins"));
		inv.setItem(2, setIS(Rank.VIP3.getName(), Material.GOLDEN_SWORD, "2999 HCoins"));
		inv.setItem(3, setIS(Rank.VIP4.getName(), Material.DIAMOND_SWORD, "3999 HCoins"));
		inv.setItem(13, setIS("Argent", Material.GOLD_NUGGET, pl.getMoney(player) + " HCoin(s)"));
		inv.setItem(27, skull);
		inv.setItem(31, setIS("§1Boutique", Material.COMMAND_BLOCK, null));
		inv.setItem(35, setIS("Quitter", Material.SPRUCE_DOOR, "Quitter l'interface"));
		if(pl.getRank(player).getPower() >= Rank.VIP4.getPower()) inv.setItem(0, setISEnchanted(Rank.VIP4.getName(), Material.DIAMOND_SWORD, "Vous possédez déjà cet article"));
		if(pl.getRank(player).getPower() >= Rank.VIP3.getPower()) inv.setItem(1, setISEnchanted(Rank.VIP3.getName(), Material.GOLDEN_SWORD, "Vous possédez déjà cet article"));
		if(pl.getRank(player).getPower() >= Rank.VIP2.getPower()) inv.setItem(2, setISEnchanted(Rank.VIP2.getName(), Material.IRON_SWORD, "Vous possédez déjà cet article"));
		if(pl.getRank(player).getPower() >= Rank.VIP1.getPower()) inv.setItem(3, setISEnchanted(Rank.VIP1.getName(), Material.STONE_SWORD, "Vous possédez déjà cet article"));
		return inv;
	}
	
	public Inventory shopConfirmInterface(Player player, int id) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName(player.getName());
		SkullMeta skullThisMeta = (SkullMeta) skullMeta;
		skullThisMeta.setOwningPlayer(player);
		skull.setItemMeta(skullThisMeta);
		String buy = null;
		int cost = 0;
		if(id == 1) {
			buy = Rank.VIP1.getName();
			cost = 999;
		}
		else if(id == 2) {
			buy = Rank.VIP2.getName();
			cost = 1999;
		}
		else if(id == 3) {
			buy = Rank.VIP3.getName();
			cost = 2999;
		}
		else if(id == 4) {
			buy = Rank.VIP4.getName();
			cost = 3999;
		}
		Inventory inv = Bukkit.createInventory(null, 36, "§1Boutique - " + player.getName());
		inv.setItem(3, setIS("Confirmer", Material.GREEN_DYE, "Confirmer l'achat du grade " + ChatColor.stripColor(buy) + " pour " + cost + " Hcoins"));
		inv.setItem(5, setIS("Annuler", Material.BARRIER, "Annuler l'achat"));
		inv.setItem(13, setIS("Argent", Material.GOLD_NUGGET, pl.getMoney(player) + " HCoin(s)"));
		inv.setItem(27, skull);
		inv.setItem(31, setIS("§1Confirmer Achat", Material.COMMAND_BLOCK, buy));
		inv.setItem(35, setIS("Quitter", Material.SPRUCE_DOOR, "Quitter l'interface"));
		return inv;
	}
	
	@EventHandler
	public void onInterract(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		if(item == null) return;
		else if (item.getType() == Material.AIR) return;
		Inventory inv = event.getInventory();
		if(inv.getSize() != 36) return;
		if(inv.getItem(31).getItemMeta().getDisplayName().equals("§1Boutique")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("Argent")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals("§1Boutique")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(player.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals(Rank.VIP1.getName()) && item.getItemMeta().getLore().equals(Arrays.asList("999 HCoins"))) {
				player.openInventory(shopConfirmInterface(player, 1));
			}
			else if(item.getItemMeta().getDisplayName().equals(Rank.VIP2.getName()) && item.getItemMeta().getLore().equals(Arrays.asList("1999 HCoins"))) {
				player.openInventory(shopConfirmInterface(player, 2));
			}
			else if(item.getItemMeta().getDisplayName().equals(Rank.VIP3.getName()) && item.getItemMeta().getLore().equals(Arrays.asList("2999 HCoins"))) {
				player.openInventory(shopConfirmInterface(player, 3));
			}
			else if(item.getItemMeta().getDisplayName().equals(Rank.VIP4.getName()) && item.getItemMeta().getLore().equals(Arrays.asList("3999 HCoins"))) {
				player.openInventory(shopConfirmInterface(player, 4));
			}
			else if(item.getItemMeta().getDisplayName().equals("Quitter")) {
				player.closeInventory();
			}
		}
		else if(inv.getItem(31).getItemMeta().getDisplayName().equals("§1Confirmer Achat")) {
			event.setCancelled(true);
			if(item.getItemMeta().getDisplayName().equals("Argent")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals("§1Confirmer Achat")) {
				return;
			}
			else if(item.getItemMeta().getDisplayName().equals(player.getName())) {
				return;
			}
			if(item.getItemMeta().getDisplayName().equals("Confirmer")) {
				Rank rank = null;
				int cost = 0;
				if(inv.getItem(31).getItemMeta().getLore().contains(Rank.VIP1.getName())) {
					rank = Rank.VIP1;
					cost = 999;
				}
				else if(inv.getItem(31).getItemMeta().getLore().contains(Rank.VIP2.getName())) {
					rank = Rank.VIP2;
					cost = 1999;
				}
				else if(inv.getItem(31).getItemMeta().getLore().contains(Rank.VIP3.getName())) {
					rank = Rank.VIP3;
					cost = 2999;
				}
				else if(inv.getItem(31).getItemMeta().getLore().contains(Rank.VIP4.getName())) {
					rank = Rank.VIP4;
					cost = 3999;
				}
				else return;
				player.closeInventory();
				if(pl.getMoney(player) < cost) {
					pl.heroriaMessage("Vous n'avez pas assez d'argent !", player);
					return;
				}
				pl.setMoney(player, pl.getMoney(player) - cost);
				pl.setRank(player, rank);
				pl.heroriaMessage("Félicitation, vous avez obtenu le grade " + rank.getName() + " !", player);
			}
			else if(item.getItemMeta().getDisplayName().equals("Annuler")) {
				player.openInventory(shopInterface(player));
			}
			else if(item.getItemMeta().getDisplayName().equals("Quitter")) {
				player.closeInventory();
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
	
	public ItemStack setISEnchanted(String name, Material material, String lore) {
		ItemStack it = new ItemStack(material);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(name);
		itM.setLore(Arrays.asList(lore));
		itM.addEnchant(Enchantment.LUCK, 1, false);
		it.setItemMeta(itM);
		return it;
	}
}
