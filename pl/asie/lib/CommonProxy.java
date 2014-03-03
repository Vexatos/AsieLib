package pl.asie.lib;

import java.io.File;

import net.minecraft.server.MinecraftServer;

public class CommonProxy {
	public boolean isClient() { return false; }
	
	public void registerEvents() { }

	public void registerTickHandlers() { }
	
	public File getMinecraftDirectory() {
		return new File(".");
	}
}
