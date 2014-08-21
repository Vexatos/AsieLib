package pl.asie.lib.tile;

import pl.asie.lib.api.provider.IBatteryProvider;

public class BatteryProviderBasic implements IBatteryProvider {
	private double energy, maxEnergy, maxInsert, maxExtract;
	
	public BatteryProviderBasic(double maxEng, double maxExt, double maxIns) {
		this.maxEnergy = maxEng;
		this.maxInsert = maxExt;
		this.maxExtract = maxIns;
	}
	
	public BatteryProviderBasic(double maxEng, double maxIO) {
		this(maxEng, maxIO, maxIO);
	}
	
	public BatteryProviderBasic(double maxEng) {
		this(maxEng, maxEng, maxEng);
	}
	
	@Override
	public double insert(int side, double maximum, boolean simulate) {
		if(maximum > maxInsert) maximum = maxInsert;
		if(energy + maximum > maxEnergy) {
			if(!simulate) energy = maxEnergy;
			return (maxEnergy - energy);
		} else {
			if(!simulate) energy += maximum;
			return maximum;
		}
	}

	@Override
	public double extract(int side, double maximum, boolean simulate) {
		double amount = Math.min(energy, Math.min(maximum, maxExtract));
		if(!simulate) energy -= amount;
		return amount;
	}

	@Override
	public double getEnergyStored() {
		return energy;
	}

	@Override
	public double getMaxEnergyStored() {
		return maxEnergy;
	}

	@Override
	public double getMaxEnergyInserted() {
		return maxInsert;
	}

	@Override
	public double getMaxEnergyExtracted() {
		return maxExtract;
	}

	@Override
	public boolean canInsert(int side, String type) {
		return (maxInsert > 0.0);
	}

	@Override
	public boolean canExtract(int side, String type) {
		return (maxExtract > 0.0);
	}

}
