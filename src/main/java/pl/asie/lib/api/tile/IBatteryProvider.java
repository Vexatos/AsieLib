package pl.asie.lib.api.tile;

import buildcraft.api.power.IPowerReceptor;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.tile.IEnergySink;

@Optional.InterfaceList({
	@Optional.Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Core"),
	@Optional.Interface(iface = "cofh.api.tileentity.IEnergyInfo", modid = "CoFHLib"),
	@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
	@Optional.Interface(iface = "ic2classic.api.energy.tile.IEnergySink", modid = "IC2-Classic")
})
public interface IBatteryProvider extends
	IEnergyHandler, IEnergyInfo, /* RF */
	IEnergySink, ic2classic.api.energy.tile.IEnergySink, /* IC2 */
	IPowerReceptor /* BuildCraft */
{

}
