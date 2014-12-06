package pl.asie.lib.gui.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

/**
 * @author Vexatos
 */
public abstract class ItemStackInventory extends Inventory {

	protected ItemStack container;

	protected ItemStackInventory(ItemStack container) {
		super();
		this.container = container;
		if(container != null) {
			reinitialize();
		}
	}

	public ItemStack container() {
		return this.container;
	}

	protected void reinitialize() {
		if(!container.hasTagCompound()) {
			container.setTagCompound(new NBTTagCompound());
		}
		for(int i = 0; i < items.length; i++) {
			items[i] = null;
		}
		if(container.getTagCompound().hasKey("computronics:items")) {
			NBTTagList list = container.getTagCompound().getTagList("computronics:items", Constants.NBT.TAG_COMPOUND);
			for(int i = 0; i < Math.min(list.tagCount(), items.length); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				if(!tag.hasNoTags()) {
					items[i] = ItemStack.loadItemStackFromNBT(tag);
				}
			}
		}
	}

	@Override
	public void markDirty() {
		NBTTagList list = new NBTTagList();
		for(ItemStack item : items) {
			NBTTagCompound tag = new NBTTagCompound();
			if(item != null) {
				item.writeToNBT(tag);
			}
			list.appendTag(tag);
		}
		container.getTagCompound().setTag("computronics:items", list);
	}

	@Override
	public void closeInventory() {
		super.closeInventory();
		NBTTagCompound tag = container.getTagCompound();
		this.save(tag);
		container.setTagCompound(tag);
	}

	@Override
	public void openInventory() {
		super.openInventory();
		NBTTagCompound tag = container.getTagCompound();
		this.load(tag);
		container.setTagCompound(tag);
	}
}
