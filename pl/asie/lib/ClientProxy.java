package pl.asie.lib;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import pl.asie.lib.shinonome.EventKey;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	public boolean isClient() { return true; }
	
	public File getMinecraftDirectory() {
		return Minecraft.getMinecraft().mcDataDir;
	}
}
