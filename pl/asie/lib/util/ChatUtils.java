package pl.asie.lib.util;

import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {
	public static String color(String chat) {
		return chat.replaceAll("&", "\u00a7");
	}
}
