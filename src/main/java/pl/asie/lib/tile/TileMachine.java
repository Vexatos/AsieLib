package pl.asie.lib.tile;

import cofh.api.energy.IEnergyHandler;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2classic.api.Direction;
import pl.asie.lib.api.provider.IBundledRedstoneProvider;
import pl.asie.lib.api.provider.IInventoryProvider;
import pl.asie.lib.api.provider.IBatteryProvider;
import pl.asie.lib.block.BlockBase;
import pl.asie.lib.block.TileEntityBase;
import pl.asie.lib.util.EnergyConverter;
import mods.immibis.redlogic.api.wiring.IBareRedstoneWire;
import mods.immibis.redlogic.api.wiring.IBundledEmitter;
import mods.immibis.redlogic.api.wiring.IBundledUpdatable;
import mods.immibis.redlogic.api.wiring.IBundledWire;
import mods.immibis.redlogic.api.wiring.IConnectable;
import mods.immibis.redlogic.api.wiring.IRedstoneWire;
import mods.immibis.redlogic.api.wiring.IWire;
import mrtjp.projectred.api.IBundledTile;
import mrtjp.projectred.api.ProjectRedAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

@Optional.InterfaceList({
	@Optional.Interface(iface = "mods.immibis.redlogic.api.wiring.IConnectable", modid = "RedLogic")
})
public class TileMachine extends TileEntityBase implements
	IConnectable /* RedLogic */
{
	private IBundledRedstoneProvider brP;
	private IInventoryProvider invP;
	private IBatteryProvider pP;
	
	public TileMachine() {
	}
	
	public IBundledRedstoneProvider getBundledRedstoneProvider() { return brP; }
	public IInventoryProvider getInventoryProvider() { return invP; }
	public IBatteryProvider getBatteryProvider() { return pP; }
	
	public void registerBundledRedstoneProvider(IBundledRedstoneProvider p) {
		this.brP = p;
	}
	
	public void registerInventoryProvider(IInventoryProvider p) {
		this.invP = p;
	}
	
	public void registerBatteryProvider(IBatteryProvider p) {
		this.pP = p;
	}
	
	@Override
	public void validate() {
		super.validate();
		if(Loader.isModLoaded("BuildCraft|Core") && this.pP != null) this.initBC();
		if(Loader.isModLoaded("IC2") && this.pP != null) this.initIC();
		if(Loader.isModLoaded("IC2-Classic") && this.pP != null) this.initICClassic();
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if(Loader.isModLoaded("IC2") && this.pP != null) this.deinitIC();
		if(Loader.isModLoaded("IC2-Classic") && this.pP != null) this.deinitICClassic();
	}

	
	// (Bundled) Redstone
	
	@Optional.Method(modid = "RedLogic")
	public boolean connects(IWire wire, int blockFace, int fromDirection) {
		if(wire instanceof IBareRedstoneWire && this.blockType != null
				&& ((BlockBase)this.blockType).emitsRedstone(worldObj, xCoord, yCoord, zCoord, fromDirection))
			return true;
		else if(wire instanceof IBundledWire) {
			if(this.brP != null) return this.brP.canConnectTo(fromDirection, blockFace);
			else return false;
		}
		
		return false;
	}
	
	@Optional.Method(modid = "RedLogic")
	public boolean connectsAroundCorner(IWire wire, int blockFace,
			int fromDirection) {
		return false;
	}
	
	@Optional.Method(modid = "RedLogic")
	public void onBundledInputChanged() {
		if(this.brP != null) {
			for(int side = 0; side < 6; side++) {
				IBundledEmitter be = null;
				for(int face = -1; face < 6; face++) {
					if(this.brP.canConnectTo(side, face)) {
						if(be == null) {
							ForgeDirection fd = ForgeDirection.values()[side];
							TileEntity t = worldObj.getTileEntity(xCoord+fd.offsetX, yCoord+fd.offsetY, zCoord+fd.offsetZ);
							if(t != null && t instanceof IBundledEmitter) {
								be = (IBundledEmitter)t;
							} else break;
						}
						this.brP.onBundledInputChange(side, face, be.getBundledCableStrength(face, side));
					}
				}
			}
		}
	}

	@Optional.Method(modid = "RedLogic")
	public byte[] getBundledCableStrength(int blockFace, int toDirection) {
		if(this.brP != null && this.brP.canConnectTo(toDirection, blockFace))
			return this.brP.getBundledOutput(toDirection, blockFace);
		else return null;
	}
	
	@Optional.Method(modid = "ProjRed|Core")
	public byte[] getBundledSignal(int side) {
		if(this.brP != null && this.brP.canConnectTo(side, -1))
			return this.brP.getBundledOutput(side, -1);
		else return null;
	}

	@Optional.Method(modid = "ProjRed|Core")
	public boolean canConnectBundled(int side) {
		return this.brP.canConnectTo(side, -1);
	}
	
	@Optional.Method(modid = "ProjRed|Core")
	public void onProjectRedBundledInputChanged() {
		if(this.brP != null) {
			for(int i = 0; i < 6; i++) {
				if(!this.brP.canConnectTo(i, -1)) continue;
				byte[] data = ProjectRedAPI.transmissionAPI.getBundledInput(worldObj, xCoord, yCoord, zCoord, i);
				if(data != null) this.brP.onBundledInputChange(i, -1, data);
			}
		}
	}

	// Inventory
	
	public void closeInventory() {
		if(this.invP != null) this.invP.onClose();
	}
	
	public ItemStack decrStackSize(int arg0, int arg1) {
		if(this.invP != null && arg0 >= 0 && arg0 < this.invP.getSize())
			return this.invP.decrStackSize(arg0, arg1);
		else return null;
	}
	
	public String getInventoryName() {
		return this.blockType.getUnlocalizedName() + ".inventory";
	}
	
	public int getInventoryStackLimit() {
		return 64;
	}
	
	public int getSizeInventory() {
		if(this.invP != null) return this.invP.getSize();
		else return 0;
	}
	
	public ItemStack getStackInSlot(int arg0) {
		if(this.invP != null && arg0 >= 0 && arg0 < this.invP.getSize())
			return this.invP.getStackInSlot(arg0);
		else return null;
	}
	
	public ItemStack getStackInSlotOnClosing(int arg0) {
		if(this.invP != null && arg0 >= 0 && arg0 < this.invP.getSize()) {
			ItemStack is = this.invP.getStackInSlot(arg0);
			this.setInventorySlotContents(arg0, null);
			return is;
		} else return null;
	}
	
	public boolean hasCustomInventoryName() {
		return false;
	}
	
	public boolean isItemValidForSlot(int arg0, ItemStack arg1) {
		if(this.invP != null && arg0 >= 0 && arg0 < this.invP.getSize())
			return this.invP.isItemValidForSlot(arg0, arg1);
		else return false;
	}
	
	public boolean isUseableByPlayer(EntityPlayer arg0) {
		if(this.invP != null) return this.invP.isUseableByPlayer(arg0);
		else return false;
	}

	public void openInventory() {
		if(this.invP != null) this.invP.onOpen();
	}
	
	public void setInventorySlotContents(int arg0, ItemStack arg1) {
		if(this.invP != null && arg0 >= 0 && arg0 < this.invP.getSize()) {
			this.invP.setInventorySlotContents(arg0, arg1);
			this.invP.onSlotUpdate(arg0);
		}
	}
	
	public boolean canExtractItem(int arg0, ItemStack arg1, int arg2) {
		if(this.invP != null && arg0 >= 0 && arg0 < 6 && arg2 >= 0 && arg2 < this.invP.getSize())
			return this.invP.canExtractItem(arg0, arg1, arg2);
		else return false;
	}
	
	public boolean canInsertItem(int arg0, ItemStack arg1, int arg2) {
		if(this.invP != null && arg0 >= 0 && arg0 < 6 && arg2  >= 0 && arg2 < this.invP.getSize())
			return this.invP.canInsertItem(arg0, arg1, arg2);
		else return false;
	}
	
	public int[] getAccessibleSlotsFromSide(int arg0) {
		if(this.invP != null && arg0 >= 0 && arg0 < 6)
			return this.invP.getAccessibleSlotsFromSide(arg0);
		else return new int[]{};
	}

	// Energy (MJ)
	
	public void initializeBC(PowerHandler power) { }
	
	private PowerHandler mjPower;
	
	@Optional.Method(modid = "BuildCraft|Core")
	private void initBC() {
		mjPower = new PowerHandler((IPowerReceptor)this, Type.MACHINE);
		mjPower.configure(1.0, 100.0, 25.0, 100.0);
		mjPower.configurePowerPerdition(0, 0);
		this.initializeBC(mjPower);
	}

	@Optional.Method(modid = "BuildCraft|Core")
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		return mjPower.getPowerReceiver();
	}

	@Optional.Method(modid = "BuildCraft|Core")
	public void doWork(PowerHandler workProvider) {
		double insertedMj = mjPower.useEnergy(0.0, mjPower.getMaxEnergyStored(), true);
		if(this.pP != null) {
			this.pP.insert(-1, EnergyConverter.convertEnergy(insertedMj, "MJ", "RF"), true);
		}
	}

	// Why the...!?
	@Optional.Method(modid = "BuildCraft|Core")
	public World getWorld() {
		return this.worldObj;
	}
	
	// Energy (RF)
	
	public boolean canConnectEnergy(ForgeDirection from) {
		if(this.pP != null) return this.pP.canInsert(from.ordinal(), "RF");
		else return false;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		if(this.pP != null && this.pP.canInsert(from.ordinal(), "RF"))
			return (int)Math.floor(this.pP.insert(from.ordinal(), maxReceive, simulate));
		else return 0;
	}
	
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		if(this.pP != null && this.pP.canExtract(from.ordinal(), "RF"))
			return (int)Math.floor(this.pP.extract(from.ordinal(), maxExtract, simulate));
		else return 0;
	}
	
	public int getEnergyStored(ForgeDirection from) {
		if(this.pP != null) return (int)Math.floor(this.pP.getEnergyStored());
		else return 0;
	}
	
	public int getMaxEnergyStored(ForgeDirection from) {
		if(this.pP != null) return (int)Math.floor(this.pP.getMaxEnergyStored());
		else return 0;
	}
	
	// Energy (EU - IC2 Experimental)

	private boolean didInitIC2 = false;
	
	@Optional.Method(modid = "IC2")
	public void initIC() {
		if(!didInitIC2) MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile)this));
		didInitIC2 = true;
	}
	
	@Optional.Method(modid = "IC2")
	public void deinitIC() {
		if(didInitIC2) MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile)this));
		didInitIC2 = false;
	}
	
	@Optional.Method(modid = "IC2")
	public boolean acceptsEnergyFrom(TileEntity emitter,
			ForgeDirection direction) {
		if(this.pP != null) return this.pP.canInsert(direction.ordinal(), "EU");
		else return false;
	}

	@Optional.Method(modid = "IC2")
	public double injectEnergy(ForgeDirection directionFrom, double amount,
			double voltage) {
		if(this.pP != null) {
			double amountRF = EnergyConverter.convertEnergy(amount, "EU", "RF");
			double injectedRF = this.pP.insert(directionFrom.ordinal(), amountRF, false);
			return amount - EnergyConverter.convertEnergy(injectedRF, "RF", "EU");
		} else return amount;
	}

	@Optional.Method(modid = "IC2")
	public double getDemandedEnergy() {
		if(this.pP != null)
			return EnergyConverter.convertEnergy(pP.getMaxEnergyInserted(), "RF", "EU");
		else return 0.0;
	}

	@Optional.Method(modid = "IC2")
	public int getSinkTier() {
		return Integer.MAX_VALUE;
	}
	
	// Energy (EU - IC2 Classic)
	
	private boolean didInitIC2C = false;
	
	@Optional.Method(modid = "IC2-Classic")
	private void initICClassic() {
		if(!didInitIC2C) MinecraftForge.EVENT_BUS.post(new ic2classic.api.energy.event.EnergyTileLoadEvent((ic2classic.api.energy.tile.IEnergyTile)this));
		didInitIC2C = true;
	}
	
	@Optional.Method(modid = "IC2-Classic")
	private void deinitICClassic() {
		if(didInitIC2C) MinecraftForge.EVENT_BUS.post(new ic2classic.api.energy.event.EnergyTileUnloadEvent((ic2classic.api.energy.tile.IEnergyTile)this));
		didInitIC2C = false;
	}

	@Optional.Method(modid = "IC2-Classic")
	public boolean acceptsEnergyFrom(TileEntity arg0, Direction arg1) {
		if(this.pP != null) return this.pP.canInsert(arg1.toSideValue(), "EU");
		else return false;
	}

	@Optional.Method(modid = "IC2-Classic")
	public boolean isAddedToEnergyNet() {
		return didInitIC2C;
	}

	@Optional.Method(modid = "IC2-Classic")
	public int demandsEnergy() {
		if(this.pP != null) return (int)Math.floor(EnergyConverter.convertEnergy(pP.getMaxEnergyInserted(), "RF", "EU"));
		else return 0;
	}

	@Optional.Method(modid = "IC2-Classic")
	public int getMaxSafeInput() {
		return Integer.MAX_VALUE;
	}

	@Optional.Method(modid = "IC2-Classic")
	public int injectEnergy(Direction arg0, int amount) {
		if(this.pP != null) {
			double amountRF = EnergyConverter.convertEnergy(amount, "EU", "RF");
			double injectedRF = this.pP.insert(arg0.toSideValue(), amountRF, false);
			return amount - (int)Math.floor(EnergyConverter.convertEnergy(injectedRF, "RF", "EU"));
		} else return amount;
	}
}
