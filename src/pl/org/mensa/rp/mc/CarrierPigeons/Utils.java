package pl.org.mensa.rp.mc.CarrierPigeons;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;

public class Utils {
	public static final String prefix = "&3[&bCarrierPigeons&3]&e";
	public static final Logger logger = Logger.getLogger("Minecraft");
	
	public static String colorize(String s) {
		return s==null ? "" : s.replaceAll("&", "§");
	}
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(colorize(message));
	}
	public static void log(Object object) {
		logger.log(Level.INFO, colorize(prefix + " " + object.toString()));
	}
	public static void log(Level level, Object object) {
		logger.log(level, colorize(prefix + " " + object.toString()));
	}
	
	public static String mergeArray(String[] strings) {
		return mergeArray(strings, 0);
	}
	public static String mergeArray(String[] strings, int startIndex) {
		return mergeArray(strings, startIndex, " ");
	}
	public static String mergeArray(String[] strings, int startIndex, String separator) {
		if (strings.length <= startIndex) return "";
		String msg = strings[startIndex];
		
		for (int i=startIndex+1; i<strings.length; ++i) {
			msg += separator + strings[i];
		}
		
		return msg;
	}
}
