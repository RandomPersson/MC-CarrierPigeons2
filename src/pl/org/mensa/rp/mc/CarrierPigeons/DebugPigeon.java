package pl.org.mensa.rp.mc.CarrierPigeons;

import org.bukkit.entity.Player;

public class DebugPigeon extends Pigeon {
	
	public DebugPigeon(Config config, Player sender, Player target, String message) {
		super(config, sender, target, message);
	}
	
	@Override
	public void run() {
		super.run();
		
		Utils.sendMessage(sender, "&8[&bDebug&8]&e Pigeon location: &b" + this.pigeon_location.getX() + " " + this.pigeon_location.getZ());
		Utils.sendMessage(sender, "&8[&bDebug&8]&e Target location: &b" + this.target.getLocation().getX() + " " + this.target.getLocation().getZ());
		Utils.sendMessage(sender, "&8[&bDebug&8]&c Distance: " + this.target.getLocation().toVector().distance(pigeon_location));
	}
	
	@Override
	public void arriveNow() {
		super.arriveNow();
		
		Utils.sendMessage(sender, "&8[&bDebug&8]&6 Pigeon arrived!");
		Utils.sendMessage(sender, "&8[&bDebug&8]&e Pigeon location: &b" + this.pigeon_location.getX() + " " + this.pigeon_location.getZ());
		Utils.sendMessage(sender, "&8[&bDebug&8]&e Target location: &b" + this.target.getLocation().getX() + " " + this.target.getLocation().getZ());
		Utils.sendMessage(sender, "&8[&bDebug&8]&e Distance: &3" + this.target.getLocation().toVector().distance(pigeon_location));
	}
}
