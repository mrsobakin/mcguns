package net.sbkn.guns;

import net.sbkn.customthings.API;
import net.sbkn.guns.items.Gun;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class KillfeedListener implements Listener {
  @EventHandler
  public void onEntityDeath(EntityDeathEvent e) {
    LivingEntity livingEntity = e.getEntity();
    if (!(livingEntity.getLastDamageCause() instanceof EntityDamageByEntityEvent))
      return; 
    Entity entityKiller = ((EntityDamageByEntityEvent)livingEntity.getLastDamageCause()).getDamager();
    
    if (entityKiller instanceof Player) {
      Player killer = (Player)entityKiller;
      
      ItemStack item = killer.getInventory().getItemInMainHand();
      
      Gun gun = (Gun)API.getItemByItemStack(item);
      if (gun == null)
        return; 
      Bukkit.broadcastMessage(" [ " + killer.getName() + " " + gun.KILLFEED_SYMBOL + " " + e.getEntity().getName() + " ] ");
    } else if (entityKiller instanceof org.bukkit.entity.Snowball) {
      ProjectileSource ps = ((Projectile)entityKiller).getShooter();
      if (ps instanceof Player)
        Bukkit.broadcastMessage(" [ " + ((Player)ps).getName() + " Blewed up " + e.getEntity().getName() + " ] "); 
    } 
  }
}
