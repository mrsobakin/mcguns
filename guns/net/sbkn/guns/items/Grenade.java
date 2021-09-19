package net.sbkn.guns.items;

import net.sbkn.customthings.API;
import net.sbkn.customthings.GrenadeProjectile;
import net.sbkn.customthings.items.BowItem;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;

public class Grenade
  extends BowItem {
  public Grenade(int id) {
    super(id, "Grenade");
  }
  
  public void onRelease(EntityShootBowEvent e, ItemStack item) {
    API.launchProjectile(GrenadeProjectile.class, (Player)e.getEntity(), e.getForce() * 2.0F);
    
    if (((Player)e.getEntity()).getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount() - 1); 
  }
  
  public void onAttack(PlayerAnimationEvent e, ItemStack item) {
    API.launchProjectile(GrenadeProjectile.class, e.getPlayer(), 0.15F);
    
    if (e.getPlayer().getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount() - 1);
    
    e.setCancelled(true);
  }
}
