package pl.asie.lib;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import pl.asie.lib.shinonome.EventKeyClient;
import net.minecraft.client.Minecraft;
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
}
