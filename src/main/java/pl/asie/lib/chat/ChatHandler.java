package pl.asie.lib.chat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mcp.mobius.talkative.api.PrattleAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.ServerChatEvent;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.chat.prattle.CommandMePrattle;
import pl.asie.lib.reference.Mods;
import pl.asie.lib.util.ChatUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChatHandler {
	private HashMap<String, String> actions = new HashMap<String, String>();
	public boolean enableChatFeatures, enableShout, enableGreentext, enableColor;
	public int CHAT_RADIUS, nickLevel;
	public String colorAction, messageFormat, shoutPrefix;

	public ChatHandler(Configuration config) {
		CHAT_RADIUS = config.get("chat", "chatRadius", 0).getInt();
		enableShout = config.get("chat", "enableShout", true).getBoolean(true);
		shoutPrefix = config.get("chat", "shoutPrefix", "[Shout]").getString();
		enableChatFeatures = config.get("base", "enableChatTweaks", false).getBoolean(false);
		enableColor = config.get("base", "enableColor", true).getBoolean(true);
		enableGreentext = config.get("chat", "enableGreentext", false, ">implying anyone will ever turn this on").getBoolean(false);
		nickLevel = config.get("chat", "nicknamesForEveryone", true, "Disable to make changing the own nickname require Op rights on a server").getBoolean(true) ? 0 : 2;
		colorAction = config.get("chat", "colorMe", "5").getString();
		messageFormat = config.get("chat", "formatMessage", "<%u> %m", "%u - username; %m - message; %w - dimension; %H - hours; %M - minutes; %S - seconds").getString();
	}

	public void registerCommands(FMLServerStartingEvent event) {
		if(enableChatFeatures) {
			if(Loader.isModLoaded(Mods.Prattle)) {
				registerPrattleCommands(event);
				return;
			}
			event.registerServerCommand(new CommandMe());
			event.registerServerCommand(new CommandNick());
			event.registerServerCommand(new CommandRealname());
		}
	}

	@Optional.Method(modid = Mods.Prattle)
	private void registerPrattleCommands(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandMePrattle());
		event.registerServerCommand(new CommandNick());
		event.registerServerCommand(new CommandRealname());
	}

	public static String pad(int t) {
		if(t < 10) {
			return "0" + t;
		} else {
			return "" + t;
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void chatEvent(ServerChatEvent event) {
		if(CHAT_RADIUS < 0) { // Chat disabled altogether
			event.setCanceled(true);
			return;
		}

		ChatComponentText chat;
		boolean disableRadius = false;
		String username = ChatUtils.color(AsieLibMod.nick.getNickname(event.username)) + EnumChatFormatting.RESET;
		String message = event.message;
		int dimensionId = event.player.worldObj.provider.dimensionId;

		if(event.message.startsWith("!") && enableShout) {
			message = message.substring(1);
		}

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

		if(enableColor) {
			try {
				formattedMessage = ChatUtils.color(formattedMessage);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		if(event.message.startsWith("!") && enableShout) {
			chat = new ChatComponentText(EnumChatFormatting.YELLOW + shoutPrefix + " " + formattedMessage);
			disableRadius = true;
		} else {
			chat = new ChatComponentText(formattedMessage);
		}

		boolean useRadius = CHAT_RADIUS > 0 && !disableRadius;
		event.setCanceled(true); // Override regular sending

		if(!useRadius && Mods.API.hasAPI("EiraIRC|API")) {
			ChatHandlerEiraIRC.eiraircRelay(event.player, username, message);
		}

		if(MinecraftServer.getServer() == null) {
			return;
		}
		for(WorldServer ws : MinecraftServer.getServer().worldServers) {
			if(useRadius && ws.provider.dimensionId != dimensionId) {
				continue;
			}
			for(Object o : ws.playerEntities) {
				if(o instanceof EntityPlayer) {
					EntityPlayer target = (EntityPlayer) o;
					if(!useRadius || event.player == target || event.player.getDistanceToEntity(target) <= CHAT_RADIUS) {
						target.addChatMessage(chat);
					}
				}
			}
		}
	}

	public static List addTabUsernameCompletionOptions(String[] args) {
		if(args == null || args.length < 1) {
			return null;
		}
		String[] names = MinecraftServer.getServer().getAllUsernames().clone();
		if(AsieLibMod.nick != null && AsieLibMod.nick.nicknames != null) {
			for(int i = 0; i < names.length; i++) {
				names[i] = AsieLibMod.nick.getRawNickname(names[i]);
			}
		}
		return CommandBase.getListOfStringsMatchingLastWord(args, names);
	}

	public void sendChatMessage(ICommandSender sender, IChatComponent message) {
		if(Loader.isModLoaded(Mods.Prattle)) {
			sendPrattleChatMessage(sender, message);
		} else {
			sender.addChatMessage(message);
		}
	}

	@Optional.Method(modid = Mods.Prattle)
	private void sendPrattleChatMessage(ICommandSender sender, IChatComponent message) {
		if(sender instanceof EntityPlayerMP) {
			PrattleAPI.sendMessage(sender, message, PrattleAPI.getDisplayChannel((EntityPlayerMP) sender));
		}
		sender.addChatMessage(message);
	}
}
