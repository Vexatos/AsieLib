package pl.asie.lib.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container {
	private final int containerSize;
	private final TileEntityBase entity;
	private final IInventory inventory;
	
	public ContainerBase(TileEntityBase entity, InventoryPlayer inventoryPlayer){
		this.entity = entity;
		if(entity instanceof IInventory) {
			this.inventory = ((IInventory)entity);
		} else this.inventory = null;
		
		if(inventory != null) {
			this.containerSize = inventory.getSizeInventory();
		} else {
			this.containerSize = 0;
		}
		entity.openInventory();
	}
	public int getSize() { return containerSize; }
	public TileEntityBase getEntity() { return entity; }
	public IInventory getInventoryObject() { return inventory; }
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.entity.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		if(inventory == null) return null;
		
		ItemStack stack = null;
		Slot slotObject = (Slot)inventorySlots.get(slot);
		if(slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			if(slot < getSize()) {
				if(!this.mergeItemStack(stackInSlot, getSize(), inventorySlots.size(), true)) {
					return null;
				}
			}
			else if(!this.mergeItemStack(stackInSlot, 0, getSize(), false)) {
				return null;
			}
			if(stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}
		}
		return stack;
	}

	public void bindPlayerInventory(InventoryPlayer inventoryPlayer, int startX, int startY) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						startX + j * 18, startY + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, startX + i * 18, startY + 58));
		}
	}
	
	@Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        this.entity.closeInventory();
    }
}
