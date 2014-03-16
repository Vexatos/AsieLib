package pl.asie.lib;

import java.io.File;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import pl.asie.lib.shinonome.EventKeyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class CommonProxy {
	public boolean isClient() { return false; }
	
	public File getMinecraftDirectory() {
		return new File(".");
	}

	public World getWorld(int dimensionId) {
		return MinecraftServer.getServer().worldServerForDimension(dimensionId);
	}

	public int getCurrentClientDimension() {
		return -9001;
	}
}
