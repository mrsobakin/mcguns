package net.sbkn.customthings;

import java.util.HashMap;
import net.sbkn.customthings.items.Item;
import net.sbkn.customthings.projectiles.Projectile;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomThings extends JavaPlugin {
  public static CustomThings instance;
  protected static HashMap<Integer, Item> items;
  protected static HashMap<Integer, Projectile> projectiles;
  
  public void onEnable() {
    instance = this;
    
    items = new HashMap<>();
    projectiles = new HashMap<>();
    
    getServer().getPluginManager().registerEvents(new EventListener(), (Plugin)this);
  }
}
