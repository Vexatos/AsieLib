package pl.asie.lib.block;

import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {
	public void onWorldUnload() {
		
	}
	
	public void onBlockDestroy() {
		
	}
	
	public void onRedstoneSignal(int side, int signal) {
		
	}
	
	private int[] oldRedstoneSignal = new int[6];
	
	protected void onRedstoneSignal_internal(int side, int redstoneSignal) {
		if(redstoneSignal == oldRedstoneSignal[side % 6]) return;
		oldRedstoneSignal[side % 6] = redstoneSignal;
		this.onRedstoneSignal(side, redstoneSignal);
	}

	public int requestCurrentRedstoneValue(int side) {
		return 0;
	}
}
