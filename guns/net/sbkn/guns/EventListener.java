package net.sbkn.guns;

import java.util.AbstractMap;
import java.util.Random;
import net.sbkn.guns.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class EventListener
  implements Listener
{
  @EventHandler
  void onEntitySpawn(EntitySpawnEvent e) {
    if (e.getEntityType() != EntityType.DROPPED_ITEM)
      return;  Item entityItem = (Item)e.getEntity();
    
    if (entityItem.getItemStack().getType() != Material.STICK)
      return;  entityItem.setGravity(false);
    entityItem.setVelocity(Vector.getRandom().subtract(new Vector(0.5F, 0.5F, 0.5F)).multiply(0.25F));
    
    ItemMeta m = entityItem.getItemStack().getItemMeta();
    m.getPersistentDataContainer().set(new NamespacedKey((Plugin)Guns.instance, "random"), PersistentDataType.LONG, Long.valueOf((new Random()).nextLong()));
    entityItem.getItemStack().setItemMeta(m);
    
    int amount = entityItem.getItemStack().getAmount();
    if (amount > 1) {
      entityItem.getItemStack().setAmount(1);
      
      for (int i = 1; i < amount; i++) {
        Item entityItem2 = (Item)entityItem.getWorld().spawnEntity(entityItem.getLocation(), EntityType.DROPPED_ITEM);
        
        entityItem2.setItemStack(entityItem.getItemStack());
        
        entityItem2.setGravity(false);

        
        entityItem2.setVelocity(Vector.getRandom().subtract(new Vector(0.5F, 0.5F, 0.5F)).multiply(0.25F));
        
        ItemMeta m2 = entityItem2.getItemStack().getItemMeta();
        m2.getPersistentDataContainer().set(new NamespacedKey((Plugin)Guns.instance, "random"), PersistentDataType.LONG, Long.valueOf((new Random()).nextLong()));
        entityItem2.getItemStack().setItemMeta(m2);
      } 
    } 
  }
  
  void onProjectileHit(ProjectileHitEvent ev) {
    Vector vel;
    Projectile snowball;
    Vector normal, reflected;
    Projectile projectile = ev.getEntity();
    switch (projectile.getType()) {
      case EGG:
        projectile.remove();
        
        vel = projectile.getVelocity();
        
        if (vel.length() < 0.1D) {
          Projectile projectile1 = (Projectile)projectile.getWorld().spawnEntity(projectile.getLocation(), EntityType.EGG);
          projectile1.setGravity(false);
          break;
        } 
        normal = ev.getHitBlockFace().getDirection();
        reflected = Utils.reflectAlongNormal(vel, normal);
        
        snowball = (Projectile)projectile.getWorld().spawnEntity(projectile.getLocation(), EntityType.EGG);
        snowball.setVelocity(reflected.multiply(0.4F));
        break;
    } 
  }
  
  @EventHandler
  public void onPlayerEggThrow(PlayerEggThrowEvent e) {
    e.setHatching(false);
  }
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    Guns.playersSpeeds.put(e.getPlayer().getUniqueId(), new AbstractMap.SimpleEntry<>(
          
          Float.valueOf((float)e.getTo().distance(e.getFrom())), 
          Long.valueOf(System.currentTimeMillis())));
  }
}
