package pl.asie.lib.api.tile;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import buildcraft.api.power.IPowerReceptor;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Optional;

@Optional.InterfaceList({
	@Optional.Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Core"),
	@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
	@Optional.Interface(iface = "ic2classic.api.energy.tile.IEnergySink", modid = "IC2-Classic")
})
public interface IBatteryProvider extends
	IEnergyHandler, /* RF */
	IEnergySink, ic2classic.api.energy.tile.IEnergySink, /* IC2 */
	IPowerReceptor /* BuildCraft */
{

}
