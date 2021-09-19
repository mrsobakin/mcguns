package net.sbkn.guns.items;

import com.comphenix.packetwrapper.WrapperPlayServerAnimation;
import java.util.List;
import net.sbkn.customthings.items.BowItem;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class TennisRocket
  extends BowItem
{
  public TennisRocket(int id) {
    super(id, "Tennis Rocket");
  }
  
  public void onRelease(EntityShootBowEvent e, ItemStack item) {
    if (!(e.getEntity() instanceof Player))
      return;  Player p = (Player)e.getEntity();
    
    reflectProjectilesAs(p, e.getForce());
    
    boolean mainHand = p.getInventory().getItemInMainHand().equals(e.getBow());
    
    WrapperPlayServerAnimation packet = new WrapperPlayServerAnimation();
    packet.setAnimation(mainHand ? 0 : 3);
    packet.setEntityID(p.getEntityId());
    
    packet.broadcastPacket();
    
    e.setCancelled(true);
  }
  
  public void onAttack(PlayerAnimationEvent e, ItemStack item) {
    Player p = e.getPlayer();
    
    reflectProjectilesAs(p, 0.3F);
    
    e.setCancelled(true);
  }
  
  public void reflectProjectilesAs(Player p, float force) {
    Vector direction = p.getLocation().getDirection();
    
    boolean didHitSmth = false;
    
    List<Entity> entities = p.getNearbyEntities(2.5D, 2.5D, 2.5D);
    for (Entity entity : entities) {
      if (!(entity instanceof org.bukkit.entity.Projectile))
        continue; 
      Vector projectileDirection = entity.getLocation().subtract(p.getLocation()).toVector().normalize();
      if (projectileDirection.distance(direction) < 1.0D) {
        didHitSmth = true;
        
        Vector vel = entity.getVelocity();
        
        entity.setVelocity(direction.multiply(vel.length() + (force * 0.8F)));
      } 
    } 
    
    if (didHitSmth)
      p.getWorld().playSound(p.getLocation(), "guns:tennis_rocket", SoundCategory.PLAYERS, 1.0F, 1.0F); 
  }
}
