package pl.asie.lib.item;

import net.minecraft.item.ItemStack;
import pl.asie.lib.gui.inventory.ItemStackInventory;

/**
 * @author Vexatos
 */
public interface IInventoryItem {

	public ItemStackInventory createItemStackInventory(ItemStack stack);
}
