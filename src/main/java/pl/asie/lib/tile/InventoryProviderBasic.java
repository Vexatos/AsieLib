package pl.asie.lib.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import pl.asie.lib.api.provider.IInventoryProvider;

public class InventoryProviderBasic implements IInventoryProvider {
	private ItemStack[] items;
	
	public InventoryProviderBasic(int size) {
		items = new ItemStack[size];
	}
	
	@Override
	public void onOpen() { }
	@Override
	public void onClose() { }
	@Override
	public void onSlotUpdate(int slot) { }

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.items[slot] != null) {
			ItemStack stack;
			if (this.items[slot].stackSize <= amount) {
				stack = this.items[slot];
				this.items[slot] = null;
		        this.onSlotUpdate(slot);
				return stack;
			} else {
				stack = this.items[slot].splitStack(amount);

				if (this.items[slot].stackSize == 0)
					this.items[slot] = null;

		        this.onSlotUpdate(slot);

				return stack;
			}
		} else return null;
	}

	@Override
	public int getSize() {
		return this.items.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.items[slot];
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer arg0) {
		return true;
	}

	@Override
	public void setInventorySlotContents(int arg0, ItemStack arg1) {
		this.items[arg0] = arg1;
	}

	@Override
	public boolean canExtractItem(int arg0, ItemStack arg1, int arg2) {
		return true;
	}

	@Override
	public boolean canInsertItem(int arg0, ItemStack arg1, int arg2) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int arg0) {
		int[] slots = new int[this.items.length];
		for(int i = 0; i < slots.length; i++) slots[i] = i;
		return slots;
	}

}
