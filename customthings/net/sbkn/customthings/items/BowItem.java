package net.sbkn.customthings.items;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class BowItem extends Item {
  public static final Material BASE_MATERIAL = Material.BOW;
  
  public BowItem(int id, String NAME) {
    super(id, NAME);
  }
  
  public void onRelease(EntityShootBowEvent e, ItemStack item) {}
  
  public ItemStack generateItem(int id) {
    ItemStack item = super.generateItem(id);
    item.setType(BASE_MATERIAL);
    
    return item;
  }
}
