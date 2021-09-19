package net.sbkn.customthings;
import net.minecraft.server.v1_14_R1.Entity;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EntitySnowball;
import net.minecraft.server.v1_14_R1.World;
import net.sbkn.customthings.items.Item;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class API {
  public static void addItem(int id, Item item) {
    CustomThings.items.put(Integer.valueOf(id), item);
  }
  
  public static Item getItem(int id) {
    return CustomThings.items.getOrDefault(Integer.valueOf(id), null);
  }
  
  public static Item getItemByItemStack(ItemStack item) {
    if (item == null) return null; 
    if (item.getType() == Material.AIR) return null;
    
    int id = ((Integer)item.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey((Plugin)CustomThings.instance, "id"), PersistentDataType.INTEGER, Integer.valueOf(-1))).intValue();
    
    return getItem(id);
  }
  
  public static void giveItemToPlayer(Player p, int id) {
    ItemStack item = ((Item)CustomThings.items.get(Integer.valueOf(id))).generateItem(id);
    p.getInventory().addItem(new ItemStack[] { item });
  }
  
  public static void launchProjectile(Class<? extends EntitySnowball> projType, Player p, float vel) {
    Location loc = p.getEyeLocation();
    
    EntitySnowball proj = new GrenadeProjectile((World)((CraftWorld)loc.getWorld()).getHandle(), (EntityLiving)((CraftPlayer)p).getHandle(), vel);
    proj.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    ((CraftWorld)loc.getWorld()).getHandle().addEntity((Entity)proj, CreatureSpawnEvent.SpawnReason.CUSTOM);
  }
}
