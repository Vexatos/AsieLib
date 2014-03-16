package pl.asie.lib.chat;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pl.asie.lib.AsieLibMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class CommandRealname extends CommandBase
{
	public String getCommandName()
    {
        return "realname";
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
        return "commands.realname.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
    	sender.addChatMessage(new ChatComponentText("TODO")); // TODO
        /*if (args.length > 0 && args[0].length() > 0) {
        	String realname = AsieLibMod.nick.getUsername(args[0]);
        	ChatComponentTranslation = new ChatCompoent();
        	if(realname != null) {
        		cmc.addKey("commands.realname.is");
        		cmc.addText(" " + realname);
        	} else {
        		cmc.addKey("commands.realname.isNot");
        	}
    		sender.sendChatToPlayer(cmc);
        } else throw new WrongUsageException("commands.realname.usage", new Object[0]);*/
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
