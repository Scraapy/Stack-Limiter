package me.Scrap.Limiter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Events
  implements Listener
{
  private Plugin plugin;
  
  public Events(Plugin plugin)
  {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event)
  {
    Player player = (Player)event.getWhoClicked();
    
    ItemStack m1 = event.getCursor();
    ItemStack m2 = event.getCurrentItem();
    


    int id1 = m1.getType().getId();
    int id2 = m2.getType().getId();
    
    int stackSum = m1.getAmount() + m2.getAmount();
    

    HashMap<Integer, Integer> limitMap = StackLimiter.getInstance().getLimitMap();
    for (Iterator localIterator = limitMap.keySet().iterator(); localIterator.hasNext();)
    {
      int i = ((Integer)localIterator.next()).intValue();
      if ((id1 == i) || (id2 == i)) {
        if ((!m1.getType().equals(Material.AIR)) && (!m2.getType().equals(Material.AIR))) {
          if (stackSum > ((Integer)limitMap.get(Integer.valueOf(i))).intValue())
          {
            player.sendMessage(Constants.PLUGIN_PREFIX + ChatColor.YELLOW + " You may not combine these two itemstacks.");
            event.setCancelled(true);
          }
        }
      }
    }
  }
}
