package pl.asie.lib.api.tile;

import cpw.mods.fml.common.Optional;
import mods.immibis.redlogic.api.wiring.IBundledEmitter;
import mods.immibis.redlogic.api.wiring.IBundledUpdatable;
import mods.immibis.redlogic.api.wiring.IConnectable;
import mrtjp.projectred.api.IBundledTile;

@Optional.InterfaceList({
	@Optional.Interface(iface = "mods.immibis.redlogic.api.wiring.IBundledEmitter", modid = "RedLogic"),
	@Optional.Interface(iface = "mods.immibis.redlogic.api.wiring.IBundledUpdatable", modid = "RedLogic"),
	@Optional.Interface(iface = "mods.immibis.redlogic.api.wiring.IConnectable", modid = "RedLogic"),
	@Optional.Interface(iface = "mrtjp.projectred.api.IBundledTile", modid = "ProjRed|Core")
})
public interface IBundledRedstoneProvider extends IBundledEmitter, IBundledUpdatable, IConnectable, IBundledTile {
	public boolean canBundledConnectTo(int side, int face);
	public byte[] getBundledOutput(int side, int face);
	public void onBundledInputChange(int side, int face, byte[] data);
}
