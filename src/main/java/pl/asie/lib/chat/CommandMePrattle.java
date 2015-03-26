package pl.asie.lib.chat;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import pl.asie.lib.AsieLibMod;

/**
 * @author Vexatos
 */
public class CommandMePrattle extends CommandMe {

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		String nickname = "Console";
		if(sender instanceof EntityPlayer) {
			nickname = sender.getCommandSenderName();
			if(AsieLibMod.nick != null && AsieLibMod.nick.nicknames != null) {
				nickname = AsieLibMod.nick.getRawNickname(nickname);
			}
		}

		/* TODO Make this proper once Prattle adds a way to do it
		if(args.length > 1 && args[1].length() > 0) {
			String target = args[0];
			String action = StringUtils.join(args, " ", 1, args.length);
			ChatComponentText cct = new ChatComponentText("%MECOMMAND%" + "\u00a7" + AsieLibMod.chat.colorAction + "* " + nickname + " " + action);
			String message = cct.getFormattedText();
			ReturnStatus var5 = ChannelHandler.INSTANCE.sendMessageToChannel(target, message, sender);
			if(var5.status == ReturnStatus.Status.OK && target.startsWith("#")) {
				Talkative.log.info("[" + target + "]<" + sender.getCommandSenderName() + "> " + message);
			}

			NetworkHelper.sendSystemMessage(var5, sender);
		} else {
			throw new WrongUsageException(this.getCommandUsage(sender));
		}*/
	}
}
