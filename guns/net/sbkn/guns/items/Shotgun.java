package net.sbkn.guns.items;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Shotgun
  extends Gun {
  public Shotgun(int id, String NAME, float damage, int scope_level, int ammo_type, int mag_size, String sound, Vector recoil_vector, int delay, String kfs) {
    super(id, NAME, damage, scope_level, ammo_type, mag_size, sound, recoil_vector, delay, kfs);
  }
  
  public void shootAsPlayer(Player p, float impreciseness) {
    for (int i = 8; i > 0; i--)
      super.shootAsPlayer(p, impreciseness + 1.0F); 
  }
}
