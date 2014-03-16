package pl.asie.lib.util;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class PlayerUtils {
	public static EntityPlayer find(String name) {
		if(MinecraftServer.getServer() == null) return null;
		
		for(Object o: MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if(o instanceof EntityPlayer) {
				EntityPlayer target = (EntityPlayer)o;
				if(target.getCommandSenderName().equals(name)) return target;
			}
		}
		return null;
	}
}
