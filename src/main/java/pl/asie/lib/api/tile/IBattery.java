package pl.asie.lib.api.tile;

import net.minecraftforge.common.util.ForgeDirection;

/*
 * Interface heavily inspired by CoFH (RF) and IC2 (EU) APIs.
 */
public interface IBattery {
	public double insert(int side, double maximum, boolean simulate);
	public double extract(int side, double maximum, boolean simulate);
	public double getEnergyStored();
	public double getMaxEnergyStored();
	public double getMaxEnergyInserted();
	public double getMaxEnergyExtracted();
	public boolean canInsert(int side, String type);
	public boolean canExtract(int side, String type);
}
