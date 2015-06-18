package me.Scrap.Limiter;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StackLimiter
  extends JavaPlugin
{
  private Plugin plugin = this;
  private HashMap<Integer, Integer> limitMap = new HashMap();
  private static StackLimiter instance;
  
  public void loadConfig()
  {
    getConfig().addDefault("itemids", "");
    
    getConfig().options().copyDefaults(true);
    saveConfig();
    for (String key : this.plugin.getConfig().getConfigurationSection("itemids").getKeys(false))
    {
      int itemId = Integer.parseInt(key);
      int stackLimit = this.plugin.getConfig().getInt("itemids." + key + ".stacklimit");
      

      addToLimitMap(itemId, stackLimit);
    }
    saveConfig();
  }
  
  public void onEnable()
  {
    getLogger().info("Stack Limiter is enabled!");
    instance = this;
    

    loadConfig();
    
    getServer().getPluginManager().registerEvents(new Events(this.plugin), this);
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("additem"))
    {
      Player player = (Player)sender;
      if (args.length != 2)
      {
       	  player.sendMessage("   ");
    	  player.sendMessage(ChatColor.RED + "      Error : Incorrect usage");
          player.sendMessage(ChatColor.RED + " Usage /additem <itemid> <stacklimit>");
          player.sendMessage("   ");
      }
      else if (args.length == 2)
      {
        addItemToConfig(args[0], args[1]);
        player.sendMessage(Constants.PLUGIN_PREFIX + ChatColor.YELLOW + " Item id " + args[0] + " added with a stack limit of " + args[1]);
      }
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("removeitem"))
    {
      Player player = (Player)sender;
      if (args.length != 1)
      {
        player.sendMessage(Constants.PLUGIN_PREFIX + ChatColor.RED + " Usage /removeitem <itemid>");
      }
      else if (args.length == 1)
      {
        removeItemFromConfig(args[0]);
        player.sendMessage(Constants.PLUGIN_PREFIX + ChatColor.YELLOW + " Item id " + args[0] + " removed.");
      }
      return true;
    }
    return false;
  }
  
  public void addItemToConfig(String itemId, String stackLimit)
  {
    if ((Integer.parseInt(stackLimit) < 0) || (Integer.parseInt(stackLimit) > 64)) {
      return;
    }
    getConfig().set("itemids." + itemId + ".stacklimit", Integer.valueOf(Integer.parseInt(stackLimit)));
    saveConfig();
  }
  
  public void removeItemFromConfig(String itemId)
  {
    getConfig().set("itemids." + itemId, null);
    saveConfig();
  }
  
  public static StackLimiter getInstance()
  {
    return instance;
  }
  
  public HashMap<Integer, Integer> getLimitMap()
  {
    return this.limitMap;
  }
  
  public void addToLimitMap(int itemId, int stackSize)
  {
    this.limitMap.put(Integer.valueOf(itemId), Integer.valueOf(stackSize));
  }
}
