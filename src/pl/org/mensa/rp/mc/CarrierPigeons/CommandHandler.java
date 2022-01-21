package pl.org.mensa.rp.mc.CarrierPigeons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHandler implements CommandExecutor {
	public static final String message_no_permission = "&cNo permission";
	
	private final CarrierPigeonsPlugin plugin;
	private final Config config;
	private final GuildTracker guild_tracker;
	
	private Map<Player, String> last_sent_guild_bird;
	
	public CommandHandler(CarrierPigeonsPlugin plugin, Config config, GuildTracker guild_tracker) {
		this.config = config;
		this.plugin = plugin;
		this.guild_tracker = guild_tracker;
		last_sent_guild_bird = new HashMap<Player, String>();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String used_alias, String[] args) {
		if (args.length == 0) {
			Utils.sendMessage(sender, "&aType &b/" + used_alias + " help&a to see command list");
		}
		else {
			switch (args[0]) {
				case "help": case "h": {
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.help")) break;
					
					printHelp(sender, used_alias);
				} break;
				case "info": case "i": {
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.info")) break;
					
					printInfo(sender, used_alias);
				} break;
				case "send": case "s": {
					if (noConsoleCheck(sender)) break;
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.send")) break;
					
					if (args.length < 2) {
						Utils.sendMessage(sender, "&cYou need to write a message");
						return true;
					}
					
					birbCommand((Player)sender, args, 2, false);
				} break;
				case "senddebug": {
					if (noConsoleCheck(sender)) break;
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.senddebug")) break;
					
					if (args.length < 2) {
						Utils.sendMessage(sender, "&cYou need to write a message");
						return true;
					}
					birbCommand((Player)sender, args, 2, true);
				} break;
				case "reload": case "r": {
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.reload")) break;
					
					if (config.reload()) {
						Utils.sendMessage(sender, "&aConfig reloaded");
					}
					else {
						Utils.sendMessage(sender, "&cConfiguration error - check console");
						Utils.sendMessage(sender, "&aPrevious configuration loaded");
					}
				} break;
				case "sendroyal": case "royal": case "broadcast": {
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.royal")) break;
					
					if (args.length < 2) {
						Utils.sendMessage(sender, "&cYou need to write a message");
						break;
					}
					royalBirbCommand(sender, Utils.mergeArray(args, 1));
				} break;
				case "guild": case "g": {
					if (noConsoleCheck(sender)) break;
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.guild")) break;
					
					if (args.length < 2) {
						Utils.sendMessage(sender, "&cYou need to write a message");
						return true;
					}
					
					guildBirbCommand((Player)sender, Utils.mergeArray(args, 1), used_alias, false);
				} break;
				case "who": case "w": {
					if (noConsoleCheck(sender)) break;
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.who")) break;
					
					whoCommand((Player)sender);
				} break;
				case "guilddebug": case "gd": {
					if (noConsoleCheck(sender)) break;
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.guilddebug")) break;
					
					if (args.length < 2) {
						Utils.sendMessage(sender, "&cYou need to write a message");
						return true;
					}
					
					guildBirbCommand((Player)sender, Utils.mergeArray(args, 1), used_alias, true);
				} break;
				default: {
					if (noConsoleCheck(sender)) break;
					if (noPermissionCheck(sender, "carrierpigeons.command.pigeon.send")) break;
					
					birbCommand((Player)sender, args, 1, false);
				}
			}
		}
		
		return true;
	}
	private boolean noConsoleCheck(CommandSender sender) {
		return !consoleCheck(sender, false);
	}
	@SuppressWarnings("unused")
	private boolean noConsoleCheck(CommandSender sender, boolean silent) {
		return !consoleCheck(sender, silent);
	}
	@SuppressWarnings("unused")
	private boolean consoleCheck(CommandSender sender) {
		return consoleCheck(sender, false);
	}
	private boolean consoleCheck(CommandSender sender, boolean silent) {
		if (sender instanceof Player) {
			return true;
		}
		
		if (!silent) Utils.sendMessage(sender, "&cPlayer-only command");
		return false;
	}
	private boolean noPermissionCheck(CommandSender sender, String permission) {
		return !permissionCheck(sender, permission, false);
	}
	@SuppressWarnings("unused")
	private boolean noPermissionCheck(CommandSender sender, String permission, boolean silent) {
		return !permissionCheck(sender, permission, silent);
	}
	@SuppressWarnings("unused")
	private boolean permissionCheck(CommandSender sender, String permission) {
		return permissionCheck(sender, permission, false);
	}
	private boolean permissionCheck(CommandSender sender, String permission, boolean silent) {
		if ((sender instanceof Player) && !sender.hasPermission(permission)) {
			if (!silent) Utils.sendMessage(sender, message_no_permission);
			return false;
		}
		
		return true;
	}
	
	private void printHelp(CommandSender sender, String alias) {
		Utils.sendMessage(sender, "&9==================[&bCarrierPigeons&9]===================");
		if (permissionCheck(sender, "carrierpigeons.command.pigeon.help", true))
			Utils.sendMessage(sender, "&a/" + alias + " help&e - command list");
		if (permissionCheck(sender, "carrierpigeons.command.pigeon.info", true))
			Utils.sendMessage(sender, "&a/" + alias + " info&e - plugin info");
		if (permissionCheck(sender, "carrierpigeons.command.pigeon.send", true))
			Utils.sendMessage(sender, "&a/" + alias + " send &b<player> <message>&e - send a letter");
		if (permissionCheck(sender, "carrierpigeons.command.pigeon.send", true))
			Utils.sendMessage(sender, "&a/" + alias + " &b<player> <message>&e - send a letter");
		if (permissionCheck(sender, "carrierpigeons.command.pigeon.guild", true))
			Utils.sendMessage(sender, "&a/" + alias + " guild &b<message>&e - broadcast to all guild members");
		if (permissionCheck(sender, "carrierpigeons.command.pigeon.royal", true))
			Utils.sendMessage(sender, "&a/" + alias + " royal &b<message>&e - broadcast to everyone");
		if (permissionCheck(sender, "carrierpigeons.command.pigeon.reload", true))
			Utils.sendMessage(sender, "&a/" + alias + " reload&e - reload config");
	}
	
	private void printInfo(CommandSender sender, String alias) {
		Utils.sendMessage(sender, "&9==================[&bCarrierPigeons&9]===================");
		Utils.sendMessage(sender, "&aVersion: &b" + plugin.getDescription().getVersion());
		Utils.sendMessage(sender, "&aAuthor: &bRandomPersson");
		Utils.sendMessage(sender, "&aCommand aliases: &bcarrierpigeons&a, &bcpigeons&a, &bpigeon&a, &bbird&a, &bbirb");
	}
	
	private void birbCommand(final Player sender, final String[] args, int message_start_index, boolean debug) {
		ItemStack item_in_hand = sender.getInventory().getItem(sender.getInventory().getHeldItemSlot());
		ItemStack item_in_offhand = sender.getInventory().getItemInOffHand();
		if (
				sender.getGameMode() != GameMode.CREATIVE &&
				sender.getGameMode() != GameMode.SPECTATOR && (
					item_in_hand == null ||
					item_in_hand.getType() != config.getItemUsed()
				) && (
					item_in_offhand == null ||
					item_in_offhand.getType() != config.getItemUsed()
				)
			) {
			Utils.sendMessage(sender, "&cYou need to hold &6" + config.getItemUsed().name().toLowerCase() + "&c to write your message on");
			return;
		}
		if (args.length < message_start_index) {
			Utils.sendMessage(sender, "&cYou need to specify a recipient");
			return;
		}
		
		final Player target = Bukkit.getPlayer(args[message_start_index-1]);
		if (target == null || !target.isOnline()) {
			if (sender.getGameMode() != GameMode.CREATIVE && sender.getGameMode() != GameMode.SPECTATOR) item_in_hand.setAmount(item_in_hand.getAmount()-1);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					if (sender != null && sender.isOnline()) {
						Utils.sendMessage(sender, "&bNot able to find &o" + args[message_start_index-1] + "&b, the pigeon returned");
						if (sender.getInventory().firstEmpty() == -1) {
							sender.getLocation().getWorld().dropItem(sender.getLocation(), new ItemStack(config.getItemUsed(), 1));
						}
						else {
							sender.getInventory().addItem(new ItemStack(config.getItemUsed(), 1));
						}
					}
				}
			}, 40L);
			
			return;
		}
		if (args.length < message_start_index+1) {
			Utils.sendMessage(sender, "&cYou need to write a message");
			return;
		}
		
		if (sender.getGameMode() != GameMode.CREATIVE && sender.getGameMode() != GameMode.SPECTATOR) item_in_hand.setAmount(item_in_hand.getAmount()-1);
		
		String message = Utils.mergeArray(args, message_start_index);
		if (permissionCheck(sender, "carrierpigeons.command.pigeon.send.color", true)) {
			message = message.replaceAll("&([0-9a-flomnk])", "§$1");
		}
		else {
			message = message.replaceAll("&([0-9a-flomnk])", "");
		}
		sendBirb(sender, target, message, debug);
		Utils.sendMessage(sender, "&aMessage sent to &o" + target.getDisplayName());
	}
	private void sendBirb(Player sender, Player target, String message, boolean debug) {
		//TODO [TSB][CP] [5] Move to Pigeon class for trackability
		//TODO [TSB][CP] [5] Add pigeon history
		//TODO [TSB][CP] [5] Add moderator tools
		//TODO [TSB][CP] [3] Detect player leaving and returning within a certain time
		if (!target.isOnline() || !sender.getLocation().getWorld().equals(target.getLocation().getWorld())) {
			if (sender != null && sender.isOnline()) {
				Utils.sendMessage(sender, "&bNot able to find &o" + target.getDisplayName() + "&b, the pigeon returned");
				if (sender.getInventory().firstEmpty() == -1) {
					sender.getLocation().getWorld().dropItem(sender.getLocation(), new ItemStack(config.getItemUsed(), 1));
				}
				else {
					sender.getInventory().addItem(new ItemStack(config.getItemUsed(), 1));
				}
			}
		}
		else { // target IS online and in the same world
			if (sender.getLocation().distance(target.getLocation()) < config.getInstantDistance()) {
				Utils.sendMessage(target, "&bA carrier pigeon from &o" + sender.getDisplayName() + "&b lands on your shoulder:");
				Utils.sendMessage(target, "&7" + message);
			}
			else {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						if (!target.isOnline() || !sender.getLocation().getWorld().equals(target.getLocation().getWorld())) {
							if (sender != null && sender.isOnline()) {
								Utils.sendMessage(sender, "&bNot able to find &o" + target.getDisplayName() + "&b, the pigeon returned");
								if (sender.getInventory().firstEmpty() == -1) {
									sender.getLocation().getWorld().dropItem(sender.getLocation(), new ItemStack(config.getItemUsed(), 1));
								}
								else {
									sender.getInventory().addItem(new ItemStack(config.getItemUsed(), 1));
								}
							}
						}
						else { // target IS online and in the same world
							Utils.sendMessage(target, "&bA carrier pigeon from &o" + sender.getDisplayName() + "&b lands on your shoulder:");
							Utils.sendMessage(target, "&7" + message);
						}
					}
				}, (long) (Math.sqrt(sender.getLocation().distance(target.getLocation()) + config.getInstantDistance()) * (config.getPigeonSpeed() / 20.0D)) * 20L);
				
				if (debug) Utils.sendMessage(sender, "&2The pigeon will arrive in &a" + (long) (Math.sqrt(sender.getLocation().distance(target.getLocation()) + config.getInstantDistance()) * (config.getPigeonSpeed() / 20.0D)) + "&2s");
			}
		}
	}
	
	private void guildBirbCommand(final Player sender, final String message, final String used_alias, boolean debug) {
		List<String> guilds_under_sender = guild_tracker.getGuildsByLeader(sender);
		if (guilds_under_sender.isEmpty()) {
			Utils.sendMessage(sender, "&cYou are not in high enough position to send announcements to subordinates!");
			return;
		}
		
		final Set<Player> recipients = new HashSet<Player>();
		for (String guild : guilds_under_sender) {
			for (Player recipient : guild_tracker.getGuildMembers(guild)) {
				recipients.add(recipient);
			}
		}
		recipients.remove(sender);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				for (Iterator<Player> recipient_iterator = recipients.iterator(); recipient_iterator.hasNext(); ) {
					Player recipient = recipient_iterator.next();
					
					if (recipient == null || !recipient.isOnline()) {
						recipient_iterator.remove();
						continue;
					}
					
					Utils.sendMessage(recipient, "&bA carrier pigeon with announcement from &o" + sender.getDisplayName() + "&b lands on your shoulder:");
					Utils.sendMessage(recipient, "&7" + message);
				}
				
				String recipient_list = "&7" + sender.getDisplayName();
				for (Player recipient : recipients) {
					recipient_list += "&8,&7 " + recipient.getDisplayName();
				}
				last_sent_guild_bird.put(sender, recipient_list);
				
				Utils.sendMessage(sender, "&aYour guild message has just been delivered. You can type &6/" + used_alias + " who&a to see who received your last announcement");
			}
		}, config.getGuildPigeonDelay()*20L);
		Utils.sendMessage(sender, "&aThe pigeon flew away carrying your announcement");
	}
	
	private void whoCommand(Player sender) {
		String last_announcement_recipient_list = last_sent_guild_bird.get(sender);
		
		if (last_announcement_recipient_list == null) {
			Utils.sendMessage(sender, "&cYou haven't sent any announcements");
		}
		else {
			Utils.sendMessage(sender, "&bYour last guild announcement was received by:");
			Utils.sendMessage(sender, last_announcement_recipient_list);
		}
	}
	
	private void royalBirbCommand(CommandSender sender, String message) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					Utils.sendMessage(player, "&bA royal carrier pigeon lands on your shoulder:");
					Utils.sendMessage(player, "&6" + message);
				}
				
				Utils.sendMessage(sender, "&aThe royal pigeon returned. Your message has been delivered");
			}
		}, config.getRoyalPigeonDelay()*20L);
		Utils.sendMessage(sender, "&aThe pigeon flew away carrying your royal decree");
	}
}
