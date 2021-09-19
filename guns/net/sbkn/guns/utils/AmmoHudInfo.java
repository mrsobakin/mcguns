package net.sbkn.guns.utils;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.entity.Player;

public class AmmoHudInfo {
  public Long lastSent;
  public int ammo;
  public int ammoTotal;
  
  public void sendToPlayer(Player p) {
    if (this.ammo != -1) {
      
      String msg = "[ " + this.ammo + " /  ]";
      
      StringBuilder sb = new StringBuilder();
      
      sb.append('');
      sb.append('');
      sb.append('');
      
      for (int i = 0; i < msg.length(); i++) {
        char c = msg.charAt(i);
        if (c >= '0' && c <= '9') {
          sb.append((char)(c + 61424 - 48));
          sb.append('');
        } else if (c == ' ') {
          sb.append('');
          sb.append('');
        } else if (c == '/') {
          sb.append('');
          sb.append('');
        } else if (c == '[') {
          sb.append('');
          sb.append('');
        } else if (c == ']') {
          sb.append('');
          sb.append('');
        } else {
          sb.append(c);
        } 
      } 
      sb.append('');
      
      ActionBarAPI.sendActionBar(p, sb.toString());
    } else {
      ActionBarAPI.sendActionBar(p, "");
    } 
  }
}
