package net.sbkn.guns.items;

import net.sbkn.customthings.items.BowItem;
import org.bukkit.entity.Egg;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class Molotov
  extends BowItem {
  public Molotov() {
    super(5, "Molotov");
  }
  
  public void onRelease(EntityShootBowEvent e, ItemStack item) {
    Egg egg = (Egg)e.getEntity().launchProjectile(Egg.class, e.getEntity().getLocation().getDirection().multiply(e.getForce() * 2.0F));
  }
}
