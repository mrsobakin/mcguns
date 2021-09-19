package net.sbkn.customthings;
import net.sbkn.customthings.items.BowItem;
import net.sbkn.customthings.items.Item;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class EventListener implements Listener {
  @EventHandler
  void onPlayerAnimation(PlayerAnimationEvent e) {
    if (e.getPlayer().getGameMode() != GameMode.ADVENTURE)
      return;  if (e.getAnimationType() != PlayerAnimationType.ARM_SWING)
      return; 
    ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
    Item customItem = API.getItemByItemStack(item);
    
    if (customItem == null)
      return; 
    customItem.onAttack(e, item);
  }
  
  @EventHandler
  void onPlayerInteract(PlayerInteractEvent e) {
    Action action = e.getAction();
    
    if (action == Action.PHYSICAL)
      return; 
    Item customItem = API.getItemByItemStack(e.getItem());
    
    if (customItem == null)
      return; 
    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
      customItem.onUse(e, e.getItem());
    } else if (e.getPlayer().getGameMode() != GameMode.ADVENTURE) {
      customItem.onAttack(new PlayerAnimationEvent(e.getPlayer()), e.getItem());
    } 
  }
  
  @EventHandler
  public void onChangeHeldItem(PlayerItemHeldEvent e) {
    PlayerInventory playerInventory = e.getPlayer().getInventory();
    
    ItemStack prevItem = playerInventory.getItem(e.getPreviousSlot());
    if (prevItem != null && prevItem.getType() == Material.IRON_HORSE_ARMOR) {
      Item customItem = API.getItemByItemStack(prevItem);
      
      if (customItem != null) customItem.onUnchooseInHotbar(e, prevItem);
    
    } 
    ItemStack newItem = playerInventory.getItem(e.getNewSlot());
    if (newItem != null && newItem.getType() == Material.IRON_HORSE_ARMOR) {
      Item customItem = API.getItemByItemStack(newItem);
      
      if (customItem != null) customItem.onChooseInHotbar(e, newItem); 
    } 
  }
  
  @EventHandler
  public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
    if (e.getMainHandItem().getType() == Material.IRON_HORSE_ARMOR) {
      int id = ((Integer)e.getMainHandItem().getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey((Plugin)CustomThings.instance, "id"), PersistentDataType.INTEGER, Integer.valueOf(-1))).intValue();
      if (id == -1)
        return; 
      Item customItem = CustomThings.items.getOrDefault(Integer.valueOf(id), null);
      if (customItem == null)
        return; 
      customItem.onSwitch(e, e.getMainHandItem());
    } 
    
    if (e.getOffHandItem().getType() == Material.IRON_HORSE_ARMOR) {
      int id = ((Integer)e.getOffHandItem().getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey((Plugin)CustomThings.instance, "id"), PersistentDataType.INTEGER, Integer.valueOf(-1))).intValue();
      if (id == -1)
        return; 
      Item customItem = CustomThings.items.getOrDefault(Integer.valueOf(id), null);
      if (customItem == null)
        return; 
      customItem.onSwitch(e, e.getOffHandItem());
    } 
  }

  
  @EventHandler
  public void onEntityShootBow(EntityShootBowEvent e) {
    if (!(e.getEntity() instanceof org.bukkit.entity.Player))
      return; 
    int id = ((Integer)e.getBow().getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey((Plugin)CustomThings.instance, "id"), PersistentDataType.INTEGER, Integer.valueOf(-1))).intValue();
    if (id == -1)
      return; 
    Item customItem = CustomThings.items.getOrDefault(Integer.valueOf(id), null);
    if (!(customItem instanceof BowItem))
      return; 
    BowItem chargableItem = (BowItem)customItem;
    
    chargableItem.onRelease(e, e.getBow());
    e.setCancelled(true);
  }
}
