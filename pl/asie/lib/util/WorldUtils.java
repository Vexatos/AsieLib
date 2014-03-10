package pl.asie.lib.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import pl.asie.lib.AsieLibMod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WorldUtils {
	public static World getWorld(int dimensionId) {
		if(AsieLibMod.proxy.isClient()) {
			if(getCurrentClientDimension() != dimensionId) {
				return null;
			} else return Minecraft.getMinecraft().theWorld;
		} else {
			return MinecraftServer.getServer().worldServerForDimension(dimensionId);
		}
	}
	
	public static TileEntity getTileEntity(int dimensionId, int x, int y, int z) {
		World world = getWorld(dimensionId);
		if(world == null) return null;
		return world.getBlockTileEntity(x, y, z);
	}
	
	public static Block getBlock(World world, int x, int y, int z) {
		return Block.blocksList[world.getBlockId(x, y, z)];
	}
	
	@SideOnly(Side.CLIENT)
	public static int getCurrentClientDimension() {
		return Minecraft.getMinecraft().theWorld.provider.dimensionId;
	}
}