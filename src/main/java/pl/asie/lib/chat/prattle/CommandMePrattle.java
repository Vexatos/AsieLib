package pl.asie.lib.chat.prattle;

import mcp.mobius.talkative.api.PrattleAPI;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.StringUtils;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.chat.CommandMe;

/**
 * @author Vexatos
 */
public class CommandMePrattle extends CommandMe {

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		String nickname = "Console";
		if(sender instanceof EntityPlayer) {
			nickname = sender.getCommandSenderName();
			if(AsieLibMod.nick != null && !AsieLibMod.nick.isNicknamesNull()) {
				nickname = AsieLibMod.nick.getRawNickname(nickname);
			}
		}

		if(args.length > 0 && args[0].length() > 0) {
			sendAction(sender, nickname, StringUtils.join(args, " "));
		} else {
			throw new WrongUsageException(this.getCommandUsage(sender));
		}
	}

	public static void sendAction(ICommandSender sender, String nickname, String action) {
		ChatComponentText cct = new ChatComponentText("\u00a7" + AsieLibMod.chat.colorAction + "* " + nickname + " " + action);
		if(sender instanceof EntityPlayerMP) {
			PrattleAPI.sendMessage(sender, cct, PrattleAPI.getDisplayChannel((EntityPlayerMP) sender));
		} else {
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(cct);
		}
	}
}
