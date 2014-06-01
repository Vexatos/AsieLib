package pl.asie.lib.cable;

import net.minecraft.nbt.NBTTagCompound;
import pl.asie.lib.block.TileEntityBase;

public class TileEntityCable extends TileEntityBase implements ICable {
	private int networkId;
	private ICableManager manager;
	
	public TileEntityCable(ICableManager manager) {
		this.manager = manager;
	}
	
	public int getNetworkID() {
		return networkId;
	}

	@Override
	public void onAdded(int id) {
		networkId = id;
	}

	@Override
	public void onRemoved(int id) {
		networkId = 0;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound cpd) {
		super.readFromNBT(cpd);
		manager.onAdded(this);
	}
	
	@Override
	public void invalidate() {
		manager.onRemoved(this);
	}
	
	@Override
	public boolean canUpdate() { return false; }
}
