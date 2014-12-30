package pl.asie.lib.chat;

import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.ServerChatEvent;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.util.ChatUtils;

import java.util.Calendar;
import java.util.HashMap;

public class ChatHandler {
	private HashMap<String, String> actions = new HashMap<String, String>();
	public boolean enableChatFeatures, enableShout, enableGreentext, enableColor;
	public int CHAT_RADIUS;
	public String colorAction, messageFormat, shoutPrefix;
	
	public ChatHandler(Configuration config) {
		CHAT_RADIUS = config.get("chat", "chatRadius", 0).getInt();
		enableShout = config.get("chat", "enableShout", true).getBoolean(true);
		shoutPrefix = config.get("chat", "shoutPrefix", "[Shout]").getString();
		enableChatFeatures = config.get("base", "enableChatTweaks", false).getBoolean(false);
		enableColor = config.get("base", "enableColor", true).getBoolean(true);
		config.get("chat", "enableGreentext", false).comment = ">implying anyone will ever turn this on";
		enableGreentext = config.get("chat", "enableGreentext", false).getBoolean(false);
		colorAction = config.get("chat", "colorMe", "5").getString();
		messageFormat = config.get("chat", "formatMessage", "<%u> %m", "%u - username; %m - message; %w - dimension; %H - hours; %M - minutes; %S - seconds").getString();
	}
	
	public void registerCommands(FMLServerStartingEvent event) {
		if(enableChatFeatures) {
			event.registerServerCommand(new CommandMe());
			event.registerServerCommand(new CommandNick());
			event.registerServerCommand(new CommandRealname());
		}
	}
	
	private static String pad(int t) {
		if(t < 10) return "0"+t;
		else return ""+t;
	}
	
	@SubscribeEvent
	public void chatEvent(ServerChatEvent event) {
		if(CHAT_RADIUS < 0) { // Chat disabled altogether
			event.setCanceled(true);
			return;
		}
		
		ChatComponentText chat = null;
		boolean disableRadius = false;
		String username = ChatUtils.color(AsieLibMod.nick.getNickname(event.username));
		String message = event.message;
		int dimensionId = event.player.worldObj.provider.dimensionId;
		
		if(event.message.startsWith("!")) message = message.substring(1);
		
		if(enableGreentext && message.startsWith(">")) {
			message = EnumChatFormatting.GREEN + message;
		}
		
		Calendar now = Calendar.getInstance();
		String formattedMessage = EnumChatFormatting.RESET + messageFormat;
		try {
			formattedMessage = formattedMessage.replaceAll("%u", username)
				.replaceAll("%m", message)
				.replaceAll("%w", event.player.worldObj.provider.getDimensionName())
				.replaceAll("%H", pad(now.get(Calendar.HOUR_OF_DAY)))
				.replaceAll("%M", pad(now.get(Calendar.MINUTE)))
				.replaceAll("%S", pad(now.get(Calendar.SECOND)));
		} catch(Exception e) {
			e.printStackTrace();
			formattedMessage = EnumChatFormatting.RESET + "<" + username + "" + EnumChatFormatting.RESET + "> " + message;
		}
		
		try {
			formattedMessage = formattedMessage.replaceAll("&", "\u00a7");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(event.message.startsWith("!") && enableShout) {
			chat = new ChatComponentText(EnumChatFormatting.YELLOW + shoutPrefix + " " + formattedMessage);
			disableRadius = true;
		} else {
			chat = new ChatComponentText(formattedMessage);
		}
		
		boolean useRadius = CHAT_RADIUS > 0 && !disableRadius;
		event.setCanceled(true); // Override regular sending
		
		if(!useRadius && ModAPIManager.INSTANCE.hasAPI("EiraIRC|API")) {
			ChatHandlerEiraIRC.eiraircRelay(event.player, username, message);
		}
		
		if(MinecraftServer.getServer() == null) return;
		for(WorldServer ws: MinecraftServer.getServer().worldServers) {
			if(useRadius && ws.provider.dimensionId != dimensionId) continue;
			for(Object o: ws.playerEntities) {
				if(o instanceof EntityPlayer) {
					EntityPlayer target = (EntityPlayer)o;
					if(!useRadius || event.player == target || event.player.getDistanceToEntity(target) <= CHAT_RADIUS) {
						target.addChatMessage(chat);
					}
				}
			}
		}
	}
}
