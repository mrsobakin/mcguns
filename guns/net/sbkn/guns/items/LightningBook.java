package net.sbkn.guns.items;

import java.util.function.Predicate;
import net.sbkn.customthings.items.BowItem;
import net.sbkn.guns.utils.PredicateNonUUID;
import net.sbkn.guns.utils.Utils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class LightningBook
  extends BowItem
{
  public LightningBook(int id) {
    super(id, "Book of Zeus");
  }
  
  public void onRelease(EntityShootBowEvent e, ItemStack item) {
    if (!(e.getEntity() instanceof Player))
      return; 
    Player p = (Player)e.getEntity();
    
    e.setCancelled(true);
    
    RayTraceResult rt = p.getWorld().rayTrace(p.getEyeLocation(), p.getLocation().getDirection(), 256.0D, FluidCollisionMode.NEVER, true, 0.1D, (Predicate)new PredicateNonUUID(p.getUniqueId()));
    
    if (rt == null)
      return;  Location loc = Utils.vec2loc(p.getWorld(), rt.getHitPosition());
    
    if (rt.getHitEntity() != null && rt.getHitEntity() instanceof LivingEntity) {
      ((LivingEntity)rt.getHitEntity()).damage(10.0D);
    }
    
    for (int i = 0; i < 8; i++)
      p.getWorld().strikeLightning(loc.add(Vector.getRandom().multiply(2))); 
  }
}
