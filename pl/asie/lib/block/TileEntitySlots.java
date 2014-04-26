package pl.asie.lib.block;

import java.util.ArrayList;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntitySlots extends TileEntityInventory implements ISidedInventory {
	public enum EnumIO {
		NONE,
		INPUT,
		OUTPUT
	};
	
	public EnumIO[] sideConfig;
	public EnumIO[] slotConfig;
	
	public TileEntitySlots() {
		sideConfig = new EnumIO[6];
		slotConfig = new EnumIO[getSizeInventory()];
		
		// Set the default configuration
		for(int i = 0; i < slotConfig.length; i++) {
			slotConfig[i] = (i == 0 ? EnumIO.INPUT : EnumIO.OUTPUT);
		}
	}
	
	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public void onInventoryUpdate(int slot) {
		
	}
	
	public int[] getAccessibleSlotsForType(EnumIO conf) {
		ArrayList<Integer> slots = new ArrayList<Integer>();
		for(int i = 0; i < slotConfig.length; i++) {
			if(conf == slotConfig[i]) slots.add(i);
		}
		int[] slotsInt = new int[slots.size()];
		for(int i = 0; i < slots.size(); i++) {
			slotsInt[i] = slots.get(i);
		}
		return slotsInt;
	}

	public boolean isInputSlot(EnumIO slot) { return slot == EnumIO.INPUT; }
	public boolean isOutputSlot(EnumIO slot) { return slot == EnumIO.OUTPUT; }

	public EnumIO getSlotForSide(int side) { return sideConfig[side]; }
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return getAccessibleSlotsForType(sideConfig[var1 % 6]);
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return isInputSlot(slotConfig[i]) && (sideConfig[j] == slotConfig[i]);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return isOutputSlot(slotConfig[i]) && (sideConfig[j] == slotConfig[i]);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if(tag.hasKey("sides")) {
			EnumIO[] vals = EnumIO.values();
			byte[] sides = tag.getByteArray("sides");
			for(int i = 0; i < 6; i++) {
				sideConfig[i] = vals[sides[i]];
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		byte[] sides = new byte[6];
		for(int i = 0; i < 6; i++) {
			sides[i] = (byte)(sideConfig[i].ordinal());
		}
		tag.setByteArray("sides", sides);
	}
}
