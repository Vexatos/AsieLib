package pl.asie.lib.api.tile;

import mods.immibis.redlogic.api.wiring.IBundledEmitter;
import mods.immibis.redlogic.api.wiring.IBundledUpdatable;
import mods.immibis.redlogic.api.wiring.IConnectable;
import mods.immibis.redlogic.api.wiring.IRedstoneEmitter;
import mods.immibis.redlogic.api.wiring.IRedstoneUpdatable;
import mrtjp.projectred.api.IBundledTile;
import cpw.mods.fml.common.Optional;

@Optional.InterfaceList({
	@Optional.Interface(iface = "mods.immibis.redlogic.api.wiring.IRedstoneEmitter", modid = "RedLogic"),
	@Optional.Interface(iface = "mods.immibis.redlogic.api.wiring.IRedstoneUpdatable", modid = "RedLogic"),
	@Optional.Interface(iface = "mods.immibis.redlogic.api.wiring.IConnectable", modid = "RedLogic")
})
public interface IRedstoneProvider extends IRedstoneEmitter, IRedstoneUpdatable, IConnectable, IBundledTile {
	public boolean canRedstoneConnectTo(int side, int face);
	public int getRedstoneOutput(int side, int face);
	public void onRedstoneInputChange(int side, int face, int input);
}