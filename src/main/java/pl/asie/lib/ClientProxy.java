package pl.asie.lib;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import pl.asie.lib.network.MessageHandlerBase;
import pl.asie.lib.network.Packet;
import pl.asie.lib.shinonome.EventKeyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	public boolean isClient() { return true; }
	
	public File getMinecraftDirectory() {
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public World getWorld(int dimensionId) {
		if(getCurrentClientDimension() != dimensionId) {
			return null;
		} else return Minecraft.getMinecraft().theWorld;
	}
	
	@Override
	public int getCurrentClientDimension() {
		return Minecraft.getMinecraft().theWorld.provider.dimensionId;
	}
	
	@Override
	public void handlePacket(MessageHandlerBase client, MessageHandlerBase server, Packet packet, INetHandler handler) {
        switch (FMLCommonHandler.instance().getEffectiveSide()) {
	        case CLIENT:
	            if(client != null)
	            	client.onMessage(packet, handler, (EntityPlayer)Minecraft.getMinecraft().thePlayer);
	            break;
	        case SERVER:
	        	super.handlePacket(client, server, packet, handler);
	        	break;
	    }
	}
}
