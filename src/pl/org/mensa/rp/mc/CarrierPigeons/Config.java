package pl.org.mensa.rp.mc.CarrierPigeons;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	private final CarrierPigeonsPlugin plugin_instance;
	private final FileConfiguration bukkit_config;
	
	private Material item_used = Material.PAPER;
	private long pigeon_speed = 20;
	private int instant_distance = 50;
	private long guild_pigeon_delay = 3;
	private long royal_pigeon_delay = 3;
	
	private Material copy_item_used;
	private long copy_pigeon_speed;
	private int copy_instant_distance;
	private long copy_guild_pigeon_delay;
	private long copy_royal_pigeon_delay;
	
	public Config(FileConfiguration bukkit_config, CarrierPigeonsPlugin plugin_instance) {
		this.bukkit_config = bukkit_config;
		this.plugin_instance = plugin_instance;
		
		makeCopy();
	}
	
	public void save() {
		bukkit_config.set("bird_speed", pigeon_speed);
		bukkit_config.set("item_used", item_used);
		bukkit_config.set("instant_distance", instant_distance);
		bukkit_config.set("guild_pigeon_delay", guild_pigeon_delay);
		bukkit_config.set("royal_pigeon_delay", royal_pigeon_delay);
		
		plugin_instance.saveConfig();
	}
	private void makeCopy() {
		copy_item_used = item_used;
		copy_pigeon_speed = pigeon_speed;
		copy_instant_distance = instant_distance;
		copy_guild_pigeon_delay = guild_pigeon_delay;
		copy_royal_pigeon_delay = royal_pigeon_delay;
	}
	private void loadCopy() {
		item_used = copy_item_used;
		pigeon_speed = copy_pigeon_speed;
		instant_distance = copy_instant_distance;
		guild_pigeon_delay = copy_guild_pigeon_delay;
		royal_pigeon_delay = copy_royal_pigeon_delay;
	}
	public boolean reload() {
		makeCopy();
		
		plugin_instance.reloadConfig();
		
		item_used = Material.matchMaterial(bukkit_config.getString("item_used"));
		pigeon_speed = bukkit_config.getLong("pigeon_speed");
		instant_distance = bukkit_config.getInt("instant_distance");
		guild_pigeon_delay = bukkit_config.getLong("guild_pigeon_delay");
		royal_pigeon_delay = bukkit_config.getLong("royal_pigeon_delay");
		
		if(detectErrors()) {
			loadCopy();
			Utils.log(Level.SEVERE, "&ePrevious configuration loaded.");
			return true;
		}
		
		return false;
	}
	private boolean detectErrors() {
		if (pigeon_speed <= 0) {
			Utils.log(Level.SEVERE, "&cConfig error: bird_speed has to be positive");
			return true;
		}
		if (item_used == null) {
			Utils.log(Level.SEVERE, "&cConfig error: item_used specified an incorrect material");
			return true;
		}
		if (instant_distance <= 0) {
			Utils.log(Level.SEVERE, "&cConfig error: instant_distance has to be positive");
			return true;
		}
		if (guild_pigeon_delay >= pigeon_speed) {
			Utils.log(Level.SEVERE, "&cConfig error: royal_pigeon_delay has to be equal to or bigger than bird_speed");
			return true;
		}
		if (royal_pigeon_delay >= pigeon_speed) {
			Utils.log(Level.SEVERE, "&cConfig error: royal_pigeon_delay has to be equal to or bigger than bird_speed");
			return true;
		}
		
		return false;
	}
	
	public Material getItemUsed() {
		return item_used;
	}
	public long getPigeonSpeed() {
		return pigeon_speed;
	}
	public int getInstantDistance() {
		return instant_distance;
	}
	public long getGuildPigeonDelay() {
		return guild_pigeon_delay;
	}
	public long getRoyalPigeonDelay() {
		return royal_pigeon_delay;
	}
	
//	public void setItemUsed(Material item_used) {
//		this.item_used = item_used;
//	}
//	public void setPigeonSpeed(long birb_speed) {
//		this.pigeon_speed = birb_speed;
//	}
//	public void setInstantDistance(int instant_distance) {
//		this.instant_distance = instant_distance;
//	}
//	public void setGuildPigeonDelay(long guild_pigeon_delay) {
//		this.guild_pigeon_delay = guild_pigeon_delay;
//	}
//	public void setRoyalPigeonDelay(long royal_pigeon_delay) {
//		this.royal_pigeon_delay = royal_pigeon_delay;
//	}
}
