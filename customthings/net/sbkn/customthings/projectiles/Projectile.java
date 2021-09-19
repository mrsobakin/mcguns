package net.sbkn.customthings.projectiles;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Projectile {
  public static final EntityType BASE_PROJECTILE = EntityType.SNOWBALL;
  public int ID;
  
  public Projectile(int id) {
    this.ID = id;
  }
  
  void onHit(ProjectileHitEvent e) {}
  
  Entity spawnProjectile(Location loc) {
    return loc.getWorld().spawnEntity(loc, BASE_PROJECTILE);
  }
  
  Entity launchProjectile() {
    return null;
  }
}
