package pl.asie.lib.gui.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public abstract class Inventory implements IInventory {

	protected ItemStack[] items;

	protected Inventory() {
		this.items = new ItemStack[getSizeInventory()];
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(this.items != null && slot >= 0 && slot < this.items.length) {
			return this.items[slot];
		} else {
			return null;
		}
	}

	protected void onSlotUpdate(int slot) {

	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(this.items != null && this.items[slot] != null) {
			ItemStack stack;
			if(this.items[slot].stackSize <= amount) {
				stack = this.items[slot];
				this.items[slot] = null;
				this.onSlotUpdate(slot);
				return stack;
			} else {
				stack = this.items[slot].splitStack(amount);

				if(this.items[slot].stackSize == 0) {
					this.items[slot] = null;
				}

				this.onSlotUpdate(slot);

				return stack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(this.items != null && slot >= 0 && slot < this.items.length) {
			ItemStack is = this.getStackInSlot(slot);
			this.setInventorySlotContents(slot, null);
			return is;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(this.items != null && slot >= 0 && slot < this.items.length) {
			this.items[slot] = stack;
			this.onSlotUpdate(slot);
		}
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	public void load(NBTTagCompound tag) {
		NBTTagList list = tag.getTagList("computronics:items", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound slotNbt = list.getCompoundTagAt(i);
			int slot = slotNbt.getInteger("slot");
			if(slot >= 0 && slot < items.length && !slotNbt.hasNoTags()) {
				items[slot] = ItemStack.loadItemStackFromNBT(slotNbt.getCompoundTag("item"));
			}
		}
	}

	public void save(NBTTagCompound tag) {
		NBTTagList list = new NBTTagList();

		for(int slot = 0; slot < items.length; slot++) {
			ItemStack stack = items[slot];
			NBTTagCompound slotNbt = new NBTTagCompound();
			slotNbt.setInteger("slot", slot);
			slotNbt.setTag("item", stack != null
				? stack.writeToNBT(new NBTTagCompound()) : new NBTTagCompound());
			list.appendTag(slotNbt);
		}
		tag.setTag("computronics:items", list);
	}

	protected void onItemAdded(int slot, ItemStack stack) {

	}

	protected void onItemRemoved(int slot, ItemStack stack) {

	}
}
