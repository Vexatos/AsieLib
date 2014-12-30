package pl.asie.lib.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PlayerUtils {
	public static EntityPlayer find(String name) {
		//AsieLibMod.log.info("trying to find player " + name);
		if(MinecraftServer.getServer() == null) return null;

		for(Object o: MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if(o instanceof EntityPlayer) {
				EntityPlayer target = (EntityPlayer)o;
				if(target.getCommandSenderName().equals(name)) return target;
			}
		}
		//AsieLibMod.log.info(" [2] could not find player " + name);
		return null;
	}
}
