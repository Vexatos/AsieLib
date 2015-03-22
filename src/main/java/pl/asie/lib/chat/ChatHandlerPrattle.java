package pl.asie.lib.chat;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mcp.mobius.talkative.api.PrattleChatEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.util.ChatUtils;

import java.util.Calendar;

import static pl.asie.lib.AsieLibMod.chat;

/**
 * @author Vexatos
 */
public class ChatHandlerPrattle {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void chatEvent(PrattleChatEvent.ServerSendChatEvent event) {

		ChatComponentText chatmessage;
		String username = ChatUtils.color(AsieLibMod.nick.getNickname(event.sender));
		String message = event.displayMsg.getFormattedText();

		if(message.startsWith("!")) {
			message = message.substring(1);
		}

		if(chat.enableGreentext && message.startsWith(">")) {
			message = EnumChatFormatting.GREEN + message;
		}

		Calendar now = Calendar.getInstance();
		String formattedMessage = EnumChatFormatting.RESET + chat.messageFormat;
		try {
			formattedMessage = formattedMessage.replaceAll("%u", username)
				.replaceAll("%m", message)
				.replaceAll("%H", ChatHandler.pad(now.get(Calendar.HOUR_OF_DAY)))
				.replaceAll("%M", ChatHandler.pad(now.get(Calendar.MINUTE)))
				.replaceAll("%S", ChatHandler.pad(now.get(Calendar.SECOND)));
		} catch(Exception e) {
			e.printStackTrace();
			formattedMessage = EnumChatFormatting.RESET + "<" + username + "" + EnumChatFormatting.RESET + "> " + message;
		}

		if(chat.enableColor) {
			try {
				formattedMessage = ChatUtils.color(formattedMessage);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		if(event.displayMsg.getFormattedText().startsWith("!") && chat.enableShout) {
			chatmessage = new ChatComponentText(EnumChatFormatting.YELLOW + chat.shoutPrefix + " " + formattedMessage);
		} else {
			chatmessage = new ChatComponentText(formattedMessage);
		}
		event.displayMsg = chatmessage;
	}
}
