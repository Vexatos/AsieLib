package pl.asie.lib;

import pl.asie.lib.block.TileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class AsieLibEvents {
	@ForgeSubscribe
	public void handleTileEntityUnload(WorldEvent.Unload event) {
		for(Object entity: event.world.loadedTileEntityList) {
			if(entity instanceof TileEntityBase) {
				((TileEntityBase)entity).onWorldUnload();
			}
		}
	}
}
