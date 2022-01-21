package pl.org.mensa.rp.mc.CarrierPigeons;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Pigeon implements Runnable {
	Config config;
	
	int id;
	Player sender;
	Player target;
	String message;
	Vector pigeon_location;
	
	public Pigeon(Config config, Player sender, Player target, String message) {
		this.config = config;
		this.sender = sender;
		this.target = target;
		this.message = message;
		pigeon_location = sender.getLocation().toVector();
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public void run() {
		if (target == null || !target.isOnline()) {
			returnToSender();
			return;
		}
		
		Vector target_location = target.getLocation().toVector();
		if (pigeon_location.distance(target_location) < config.getInstantDistance()) {
			arriveNow();
			return;
		}
		
		double vector_length = Math.sqrt(sender.getLocation().distance(target.getLocation()) + config.getInstantDistance()) * (config.getPigeonSpeed() / 20.0D);
		Vector step = target_location.subtract(pigeon_location).normalize().multiply(vector_length);
		pigeon_location.add(step);
	}
	
	public void arriveNow() {
		Utils.sendMessage(target, "&bA carrier pigeon from &o" + sender.getDisplayName() + "&b lands on your shoulder:");
		Utils.sendMessage(target, "&7" + message);
		Bukkit.getScheduler().cancelTask(id);
	}
	
	public void returnToSender() {
		if (sender != null && sender.isOnline()) {
			Utils.sendMessage(sender, "&bNot able to find &o" + target.getDisplayName() + "&b, the pigeon returned.");
			
			if (sender.getInventory().firstEmpty() == -1) {
				sender.getLocation().getWorld().dropItem(sender.getLocation(), new ItemStack(config.getItemUsed(), 1));
			}
			else {
				sender.getInventory().addItem(new ItemStack(config.getItemUsed(), 1));
			}
		}
		
		Bukkit.getScheduler().cancelTask(id);
	}
}
