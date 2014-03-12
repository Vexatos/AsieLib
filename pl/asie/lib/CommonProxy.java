package pl.asie.lib;

import java.io.File;

import pl.asie.lib.shinonome.EventKey;
import net.minecraft.server.MinecraftServer;

public class CommonProxy {
	public boolean isClient() { return false; }
	
	public File getMinecraftDirectory() {
		return new File(".");
	}
}
