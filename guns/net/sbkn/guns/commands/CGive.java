package net.sbkn.guns.commands;

import net.sbkn.customthings.API;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CGive
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length > 1) {
      Player player = Bukkit.getPlayer(args[0]);
      API.giveItemToPlayer(player, Integer.parseInt(args[1]));
    } else if (sender instanceof Player) {
      Player player = (Player)sender;
      API.giveItemToPlayer(player, Integer.parseInt(args[0]));
    } 
    
    return true;
  }
}
