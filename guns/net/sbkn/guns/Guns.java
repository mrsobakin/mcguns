package net.sbkn.guns;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.UUID;
import net.sbkn.customthings.API;
import net.sbkn.customthings.items.Item;
import net.sbkn.guns.commands.CGive;
import net.sbkn.guns.items.Grenade;
import net.sbkn.guns.items.Gun;
import net.sbkn.guns.items.LightningBook;
import net.sbkn.guns.items.Molotov;
import net.sbkn.guns.items.Shotgun;
import net.sbkn.guns.items.TennisRocket;
import net.sbkn.guns.utils.AmmoHudInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Guns extends JavaPlugin {
  public static JavaPlugin instance;
  public static ProtocolManager protocolManager;
  
  public void onEnable() {
    instance = this;
    protocolManager = ProtocolLibrary.getProtocolManager();
    playersSpeeds = new HashMap<>();
    recharging = new HashMap<>();
    
    ammoHud = new HashMap<>();
    
    API.addItem(1, (Item)new Gun(1, "Deagle", 12.0F, 2, 0, 7, "guns:deagle_shot", new Vector(-0.2D, 0.0D, -0.2D), 1000, ""));
    API.addItem(2, (Item)new Gun(2, "AK-47", 6.0F, 0, 0, 30, "guns:ak_shot", new Vector(-0.1D, 0.0D, -0.1D), 166, ""));
    API.addItem(3, (Item)new Shotgun(3, "Shotgun", 3.0F, 0, 1, 2, "guns:sawed_off_shot", new Vector(-0.5D, 0.0D, -0.5D), 2000, ""));
    API.addItem(4, (Item)new Grenade(4));
    API.addItem(5, (Item)new Molotov());
    API.addItem(7, (Item)new TennisRocket(7));
    API.addItem(8, (Item)new LightningBook(8));
    
    getCommand("cgive").setExecutor((CommandExecutor)new CGive());
    getServer().getPluginManager().registerEvents(new EventListener(), (Plugin)this);
    getServer().getPluginManager().registerEvents(new KillfeedListener(), (Plugin)this);
    
    Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)instance, new Runnable() {
          public void run() {
            for (Player p : Bukkit.getOnlinePlayers()) {
              int ammoNow;
              AmmoHudInfo hudInfo = Guns.ammoHud.getOrDefault(p.getUniqueId(), null);
              if (hudInfo == null) {
                hudInfo = new AmmoHudInfo();
                hudInfo.ammo = -1;
                hudInfo.ammoTotal = 0;
                hudInfo.lastSent = Long.valueOf(0L);
                Guns.ammoHud.put(p.getUniqueId(), hudInfo);
              } 
              
              ItemStack item = p.getInventory().getItemInMainHand();

              
              if (item != null && item.getType() != Material.AIR) {
                ammoNow = ((Integer)item.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey((Plugin)Guns.instance, "ammo"), PersistentDataType.INTEGER, Integer.valueOf(-1))).intValue();
              } else {
                ammoNow = -1;
              } 
              
              int ammoTotalNow = hudInfo.ammoTotal;
              
              if (ammoNow == -1 && hudInfo.ammo == -1) {
                continue;
              }
              
              if (System.currentTimeMillis() - hudInfo.lastSent.longValue() <= 1800L && hudInfo.ammo == ammoNow && hudInfo.ammoTotal == ammoTotalNow) {
                continue;
              }
              
              hudInfo.ammo = ammoNow;
              hudInfo.ammoTotal = ammoTotalNow;
              hudInfo.lastSent = Long.valueOf(System.currentTimeMillis());
              
              hudInfo.sendToPlayer(p);
            } 
          }
        }0L, 2L);
  }
  
  public static HashMap<UUID, AbstractMap.SimpleEntry<Float, Long>> playersSpeeds;
  public static HashMap<UUID, Boolean> recharging;
  public static HashMap<UUID, AmmoHudInfo> ammoHud;
}
