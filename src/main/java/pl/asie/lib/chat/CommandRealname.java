package pl.asie.lib.chat;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import pl.asie.lib.AsieLibMod;

import java.util.List;

public class CommandRealname extends CommandBase {
	public String getCommandName() {
		return "realname";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 0;
	}

	public String getCommandUsage(ICommandSender par1ICommandSender) {
		return "commands.realname.usage";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length > 0 && args[0].length() > 0) {
			String realname = AsieLibMod.nick.getUsername(args[0]);
			String text;
			if(realname != null) {
				text = StatCollector.translateToLocalFormatted("commands.realname.is", realname);
			} else {
				text = StatCollector.translateToLocal("commands.realname.isNot");
			}
			sender.addChatMessage(new ChatComponentText(text));
		} else {
			throw new WrongUsageException("commands.realname.usage");
		}
	}

	/**
	 * Adds the strings available in this command to the given list of tab completion options.
	 */
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if(args == null || args.length < 1) {
			return null;
		}
		String[] names = MinecraftServer.getServer().getAllUsernames().clone();
		if(AsieLibMod.nick != null && AsieLibMod.nick.nicknames != null) {
			for(int i = 0; i < names.length; i++) {
				names[i] = AsieLibMod.nick.getNickname(names[i]);
			}
		}
		return getListOfStringsMatchingLastWord(args, names);
	}

	public int compareTo(Object par1Obj) {
		return ((ICommand) par1Obj).getCommandName().compareTo(this.getCommandName());
	}
}
