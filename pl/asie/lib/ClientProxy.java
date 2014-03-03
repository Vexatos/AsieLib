package pl.asie.lib;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import pl.asie.lib.shinonome.EventKey;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	private static EventKey key = new EventKey();
	
	public boolean isClient() { return true; }
	
	public void registerEvents() {
		super.registerEvents();
		
		MinecraftForge.EVENT_BUS.register(key);
	}
	
	public void registerTickHandlers() {
		super.registerTickHandlers();
		
		TickRegistry.registerTickHandler(key, Side.CLIENT);	
		TickRegistry.registerTickHandler(key, Side.SERVER);	
	}
	
	public File getMinecraftDirectory() {
		return Minecraft.getMinecraft().mcDataDir;
	}
}
