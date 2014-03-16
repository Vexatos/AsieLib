package pl.asie.lib.chat;

import net.minecraft.command.ICommand;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pl.asie.lib.AsieLibMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class CommandMe extends CommandBase
{
	public String getCommandName()
    {
        return "me";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.me.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
    	String nickname = "Console";
    	if(sender instanceof EntityPlayer) {
    		nickname = ((EntityPlayer)sender).getCommandSenderName();
    	}
        if (args.length > 0 && args[0].length() > 0) sendAction(nickname, StringUtils.join(args, " "));
        else throw new WrongUsageException("commands.me.usage", new Object[0]);
    }
    
    public static void sendAction(String nickname, String action) {
        ChatComponentText cct = new ChatComponentText("\u00a7" + AsieLibMod.instance.chat.colorAction + "* " + nickname + " " + action);
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(cct);
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length >= 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    public int compareTo(Object par1Obj)
    {
        return ((ICommand)par1Obj).getCommandName().compareTo(this.getCommandName());
    }
}
