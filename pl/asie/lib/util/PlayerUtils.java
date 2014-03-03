package pl.asie.lib.util;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class PlayerUtils {
	public static EntityPlayer find(String name) {
		if(MinecraftServer.getServer() == null) return null;
		
		for(WorldServer ws: MinecraftServer.getServer().worldServers) {
			for(Object o: ws.playerEntities) {
				if(o instanceof EntityPlayer) {
					EntityPlayer target = (EntityPlayer)o;
					if(target.username.equals(name)) return target;
				}
			}
		}
		return null;
	}
	
	public static PlayerIterator iterator() {
		return new PlayerIterator();
	}
}
