package net.sbkn.customthings.items;

import net.sbkn.customthings.CustomThings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class Item {
  public static final Material BASE_MATERIAL = Material.IRON_HORSE_ARMOR;
  public final int ID;
  public final String NAME;
  
  public Item(int id, String NAME) {
    this.ID = id;
    this.NAME = NAME;
  }
  
  public void onAttack(PlayerAnimationEvent e, ItemStack item) {}
  
  public void onUse(PlayerInteractEvent e, ItemStack item) {}
  
  public void onSwitch(PlayerSwapHandItemsEvent e, ItemStack item) {}
  
  public void onChooseInHotbar(PlayerItemHeldEvent e, ItemStack item) {}
  
  public void onUnchooseInHotbar(PlayerItemHeldEvent e, ItemStack item) {}
  
  public ItemStack generateItem(int id) {
    ItemStack item = new ItemStack(BASE_MATERIAL);
    ItemMeta meta = item.getItemMeta();
    
    meta.setCustomModelData(Integer.valueOf(id << 12));
    
    meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)CustomThings.instance, "id"), PersistentDataType.INTEGER, Integer.valueOf(id));
    meta.setDisplayName("" + ChatColor.WHITE + ChatColor.WHITE);
    
    item.setItemMeta(meta);
    
    return item;
  }
}
