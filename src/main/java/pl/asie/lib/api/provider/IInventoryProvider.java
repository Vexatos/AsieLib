package pl.asie.lib.api.provider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IInventoryProvider {
	public void onOpen();
	public void onClose();
	public void onSlotUpdate(int slot);
	
	public ItemStack decrStackSize(int arg0, int arg1);
	public int getSize();
	public ItemStack getStackInSlot(int slot);
	public boolean isItemValidForSlot(int slot, ItemStack stack);
	public boolean isUseableByPlayer(EntityPlayer arg0);
	public void setInventorySlotContents(int arg0, ItemStack arg1);
	
	public boolean canExtractItem(int arg0, ItemStack arg1, int arg2);
	public boolean canInsertItem(int arg0, ItemStack arg1, int arg2);
	public int[] getAccessibleSlotsFromSide(int arg0);
}
