package net.sbkn.guns.items;

import java.util.AbstractMap;
import java.util.function.Predicate;
import net.sbkn.customthings.DodikSnowball;
import net.sbkn.customthings.items.Item;
import net.sbkn.guns.Guns;
import net.sbkn.guns.utils.PredicateNonUUID;
import net.sbkn.guns.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Gun
  extends Item {
  final float DAMAGE;
  final int SCOPE_LEVEL;
  final int AMMO_TYPE;
  final int MAG_SIZE;
  
  public Gun(int ID, String NAME, float damage, int scope_level, int ammo_type, int mag_size, String sound, Vector recoil_vector, int delay, String kfs) {
    super(ID, NAME);
    this.DAMAGE = damage;
    this.SCOPE_LEVEL = scope_level;
    this.AMMO_TYPE = ammo_type;
    this.MAG_SIZE = mag_size;
    this.SHOOT_SOUND = sound;
    this.RECOIL_VECTOR = recoil_vector;
    this.SHOT_DELAY = delay;
    this.KILLFEED_SYMBOL = kfs;
  }
  final String SHOOT_SOUND; final Vector RECOIL_VECTOR; final int SHOT_DELAY; public final String KILLFEED_SYMBOL;
  
  public void onAttack(PlayerAnimationEvent e, ItemStack item) {
    ItemMeta meta = item.getItemMeta();
    meta.setCustomModelData(Integer.valueOf(meta.getCustomModelData() ^ 0x1));
    item.setItemMeta(meta);
    
    if ((meta.getCustomModelData() & 0x1) == 1) {
      e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, this.SCOPE_LEVEL, false, false, false), false);
    } else {
      e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
    } 
    
    e.setCancelled(true);
  }
  
  public void onUse(PlayerInteractEvent e, ItemStack item) {
    Guns.recharging.put(e.getPlayer().getUniqueId(), Boolean.valueOf(false));
    
    ItemMeta meta = item.getItemMeta();
    
    long lastShot = ((Long)meta.getPersistentDataContainer().get(new NamespacedKey((Plugin)Guns.instance, "lastShot"), PersistentDataType.LONG)).longValue();
    int ammo = ((Integer)meta.getPersistentDataContainer().get(new NamespacedKey((Plugin)Guns.instance, "ammo"), PersistentDataType.INTEGER)).intValue();
    
    if (System.currentTimeMillis() - lastShot < this.SHOT_DELAY)
      return;  meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)Guns.instance, "lastShot"), PersistentDataType.LONG, Long.valueOf(System.currentTimeMillis()));

    
    if (ammo <= 0) {
      e.getPlayer().getWorld().playSound(e.getPlayer().getEyeLocation(), "minecraft:block.metal_pressure_plate.click_on", SoundCategory.PLAYERS, 10.0F, 1.1F);
      return;
    } 
    ammo--;
    
    meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)Guns.instance, "ammo"), PersistentDataType.INTEGER, Integer.valueOf(ammo));
    
    AbstractMap.SimpleEntry<Float, Long> speedsEntry = (AbstractMap.SimpleEntry<Float, Long>)Guns.playersSpeeds.get(e.getPlayer().getUniqueId());
    float impreciseness = (System.currentTimeMillis() - ((Long)speedsEntry.getValue()).longValue() > 75L) ? 0.0F : ((Float)speedsEntry.getKey()).floatValue();
    if (!e.getPlayer().isOnGround()) impreciseness *= 2.0F; 
    impreciseness = (float)(impreciseness + 0.05D);
    
    shootAsPlayer(e.getPlayer(), impreciseness);

    
    e.getPlayer().getWorld().playSound(e.getPlayer().getEyeLocation(), this.SHOOT_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);

    
    item.setItemMeta(meta);
    e.setCancelled(true);
  }

  
  public void onSwitch(final PlayerSwapHandItemsEvent e, ItemStack item) {
    e.setCancelled(true);
    
    ItemMeta meta = item.getItemMeta();
    PersistentDataContainer dc = meta.getPersistentDataContainer();
    
    int ammo = ((Integer)dc.get(new NamespacedKey((Plugin)Guns.instance, "ammo"), PersistentDataType.INTEGER)).intValue();
    
    if (ammo != 0) {
      dc.set(new NamespacedKey((Plugin)Guns.instance, "ammo"), PersistentDataType.INTEGER, Integer.valueOf(1));
      int ammoSaved = ammo - 1;
    } else {
      dc.set(new NamespacedKey((Plugin)Guns.instance, "ammo"), PersistentDataType.INTEGER, Integer.valueOf(0));
      int ammoSaved = 0;
    } 
    e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
    
    Guns.recharging.put(e.getPlayer().getUniqueId(), Boolean.valueOf(true));

    
    Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)Guns.instance, new Runnable() {
          public void run() {
            if (((Boolean)Guns.recharging.getOrDefault(e.getPlayer().getUniqueId(), Boolean.valueOf(false))).booleanValue()) {
              ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
              PersistentDataContainer dc = meta.getPersistentDataContainer();
              int ammo = ((Integer)dc.getOrDefault(new NamespacedKey((Plugin)Guns.instance, "ammo"), PersistentDataType.INTEGER, Integer.valueOf(0))).intValue();
              dc.set(new NamespacedKey((Plugin)Guns.instance, "ammo"), PersistentDataType.INTEGER, Integer.valueOf(ammo + Gun.this.MAG_SIZE));
              e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
            } 
          }
        }30L);
  }
  
  public void onUnchooseInHotbar(PlayerItemHeldEvent e, ItemStack item) {
    ItemMeta meta = item.getItemMeta();
    meta.setCustomModelData(Integer.valueOf(meta.getCustomModelData() & 0xFFFFFFFE));
    item.setItemMeta(meta);
    
    e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
  }
  
  public void shootAsPlayer(Player p, float impreciseness) {
    int maxParticleDistance;
    Vector direction = p.getLocation().getDirection().add(
        Vector.getRandom().subtract(new Vector(0.5F, 0.5F, 0.5F))
        .multiply(2).multiply(impreciseness * 0.15F));
    
    RayTraceResult rt = p.getWorld().rayTrace(p.getEyeLocation(), direction, 1000.0D, FluidCollisionMode.NEVER, true, 0.0D, (Predicate)new PredicateNonUUID(p.getUniqueId()));
    
    if (rt != null) {
      Block hitBlock = rt.getHitBlock();
      if (hitBlock != null) {
        String blockname = hitBlock.getType().toString();
        if (blockname.endsWith("GLASS") || blockname.endsWith("GLASS_PANE")) {
          hitBlock.getWorld().playEffect(hitBlock.getLocation(), Effect.STEP_SOUND, hitBlock.getType());
          hitBlock.breakNaturally();
        } else {
          Location loc = new Location(p.getWorld(), rt.getHitPosition().getX(), rt.getHitPosition().getY(), rt.getHitPosition().getZ());
          p.getWorld().spawnParticle(Particle.BLOCK_DUST, loc, 100, rt.getHitBlock().getBlockData());
        } 
      } else if (rt.getHitEntity() != null) {
        Entity victim = rt.getHitEntity();
        
        System.out.println(victim);
        
        if (victim instanceof LivingEntity) {
          LivingEntity livingVictim = (LivingEntity)victim;
          
          boolean headshot = (Math.abs(livingVictim.getEyeLocation().getY() - rt.getHitPosition().getY()) < 0.2D);
          
          livingVictim.setNoDamageTicks(0);
          livingVictim.damage((headshot ? (this.DAMAGE * 4.0F) : this.DAMAGE), (Entity)p);
        } else if (((CraftEntity)victim).getHandle() instanceof DodikSnowball) {
          DodikSnowball ds = (DodikSnowball)((CraftEntity)victim).getHandle();
          ds.detonate();
        } 
      } 
    } 
    
    if (rt != null) {
      maxParticleDistance = (int)Utils.loc2vec(p.getLocation()).distance(rt.getHitPosition());
      if (maxParticleDistance > 128) maxParticleDistance = 128; 
    } else {
      maxParticleDistance = 128;
    } 
    
    Vector directionNorm = direction.normalize();
    Location bloc = p.getEyeLocation();
    float i;
    for (i = 1.0F; i < maxParticleDistance; i = (float)(i * 2.5D)) {
      Location particleLoc = bloc.clone();
      particleLoc.add(directionNorm.clone().multiply(i));
      
      p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, particleLoc, 1, 0.0D, 0.0D, 0.0D, 0.001D);
    } 
  }
  
  public ItemStack generateItem(int id) {
    ItemStack item = super.generateItem(id);
    ItemMeta meta = item.getItemMeta();
    
    PersistentDataContainer dc = meta.getPersistentDataContainer();
    dc.set(new NamespacedKey((Plugin)Guns.instance, "lastShot"), PersistentDataType.LONG, Long.valueOf(0L));
    dc.set(new NamespacedKey((Plugin)Guns.instance, "ammo"), PersistentDataType.INTEGER, Integer.valueOf(0));
    dc.set(new NamespacedKey((Plugin)Guns.instance, "isInfiniteAmmo"), PersistentDataType.SHORT, Short.valueOf((short)0));
    
    item.setItemMeta(meta);
    
    return item;
  }
}
