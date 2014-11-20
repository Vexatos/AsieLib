package pl.asie.lib.api.tile;

import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.tile.IEnergySink;

@Optional.InterfaceList({
	@Optional.Interface(iface = "cofh.api.tileentity.IEnergyInfo", modid = "CoFHAPI|tileentity"),
	@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
	@Optional.Interface(iface = "ic2classic.api.energy.tile.IEnergySink", modid = "IC2-Classic"),
	@Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHAPI|energy")
})
public interface IBatteryProvider extends
	IEnergyHandler, IEnergyInfo, /* RF */
	IEnergySink, ic2classic.api.energy.tile.IEnergySink /* IC2 */ {

}
