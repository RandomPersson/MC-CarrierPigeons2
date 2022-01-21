package pl.org.mensa.rp.mc.CarrierPigeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GuildTracker implements Listener {
	private Map<String, List<Player>> guilds;
	private Map<Player, List<String>> leaders;
	
	public GuildTracker() {
		guilds = new HashMap<String, List<Player>>();
		leaders = new HashMap<Player, List<String>>();
	}
	
	public List<Player> getGuildMembers(String guild) {
		return guilds.get(guild);
	}
	public List<String> getGuildsByLeader(Player leader) {
		return leaders.get(leader);
	}
	
	public void registerAllOnlinePlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			registerPlayer(player);
		}
	}
	public void registerPlayer(Player player) {
		if (leaders.get(player) == null) {
			leaders.put(player, new ArrayList<String>(0));
		}
		
		player.getEffectivePermissions().stream()
			.map(perm -> perm.getPermission())
			.filter(string_perm -> string_perm.startsWith("carrierpigeons.guild."))
			.map(string_perm -> string_perm.substring(21))
			.forEach(string_perm_suffix -> {
				String[] split = string_perm_suffix.split("\\.", 2);
				
				if (split.length > 1) {
					leaders.get(player).add(split[0]);
				}
				
				if (guilds.get(split[0]) == null) {
					guilds.put(split[0], new ArrayList<Player>(1));
				}
				guilds.get(split[0]).add(player);
			})
		;
	}
	public void unregisterPlayer(Player player) {
		Iterator<Map.Entry<String, List<Player>>> guilds_entry_iterator = guilds.entrySet().iterator();
		
		while (guilds_entry_iterator.hasNext()) {
			List<Player> member_list = guilds_entry_iterator.next().getValue();
			
			if (member_list.contains(player)) {
				member_list.remove(player);
			}
		}
		
		leaders.remove(player);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		registerPlayer(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void onPlayerQuit(PlayerQuitEvent event) {
		unregisterPlayer(event.getPlayer());
	}
}
