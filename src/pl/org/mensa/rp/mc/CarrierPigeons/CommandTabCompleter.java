package pl.org.mensa.rp.mc.CarrierPigeons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandTabCompleter implements TabCompleter {
	private static final List<String> args1 = new ArrayList<String>(3);
	static {
		args1.add("help");
		args1.add("info");
		args1.add("send");
		args1.add("guild");
		args1.add("who");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> suggestions = new ArrayList<String>();
		
		for (int i=0; i<args.length; ++i) {
			args[i] = args[i].toLowerCase();
		}
		
		switch (args.length) {
			case 1: {
				suggestions.addAll(args1.stream().filter(arg -> arg.startsWith(args[0])).collect(Collectors.toList()));
				
				if (suggestions.isEmpty()) {
					suggestions.addAll(Bukkit.getOnlinePlayers().stream().filter(p -> p.getDisplayName().toLowerCase().startsWith(args[0])).map(p -> p.getDisplayName()).collect(Collectors.toList()));
				}
			} break;
			case 2: {
				if (args[0].equalsIgnoreCase("send")) {
					suggestions.addAll(Bukkit.getOnlinePlayers().stream().filter(p -> p.getDisplayName().toLowerCase().startsWith(args[1])).map(p -> p.getDisplayName()).collect(Collectors.toList()));
				}
			} break;
			default: {}
		}
		
		return suggestions;
	}
}
