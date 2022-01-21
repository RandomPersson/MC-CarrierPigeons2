package pl.org.mensa.rp.mc.CarrierPigeons;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class CarrierPigeonsPlugin extends JavaPlugin {
	private Config config;
	private GuildTracker guild_tracker;
	
	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		config = new Config(getConfig(), this);
		if (config.reload()) {
			throw new IllegalArgumentException("Config contains incorrect values");
		}
		
		guild_tracker = new GuildTracker();
		guild_tracker.registerAllOnlinePlayers();
		getServer().getPluginManager().registerEvents(guild_tracker, this);
		
		getCommand("carrierpigeons").setExecutor(new CommandHandler(this, config, guild_tracker));
		getCommand("carrierpigeons").setTabCompleter(new CommandTabCompleter());
	}
	
	@Override
	public void onDisable() {
		config.save();
		HandlerList.unregisterAll(guild_tracker);
	}
}
