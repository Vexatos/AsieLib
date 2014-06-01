package pl.asie.lib;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

public class CC16PeripheralHandler implements IPeripheralProvider {

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te.getClass().getSimpleName().startsWith("pl.asie") && te instanceof IPeripheral) return (IPeripheral)te;
		return null;
	}

	public static void registerPeripheralProvider() {
		ComputerCraftAPI.registerPeripheralProvider(new CC16PeripheralHandler());
	}
	
}
