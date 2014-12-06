package pl.asie.lib.gui.inventory;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.block.TileEntityBase;
import pl.asie.lib.item.IInventoryItem;

import java.util.ArrayList;

/**
 * @author Vexatos
 */
public class GuiInventoryHandler implements IGuiHandler {
	private final ArrayList<Class<? extends ContainerInventoryBase>> containers;
	private final ArrayList<Class<? extends GuiInventoryBase>> guis;

	public GuiInventoryHandler() {
		containers = new ArrayList<Class<? extends ContainerInventoryBase>>();
		guis = new ArrayList<Class<? extends GuiInventoryBase>>();
	}

	public int registerGui(Class<? extends ContainerInventoryBase> container, Class<? extends GuiInventoryBase> gui) {
		containers.add(container);
		guis.add(gui);
		return guis.size() - 1;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		try {
			if(ID < 0 || ID >= containers.size()) {
				return null;
			} else {
				TileEntity te = world.getTileEntity(x, y, z);
				if(te != null && te instanceof TileEntityBase && te instanceof IInventory) {
					return containers.get(ID).getConstructor(IInventory.class, InventoryPlayer.class)
						.newInstance(te, player.inventory);
				} else {
					ItemStack item = player.getCurrentEquippedItem();
					if(item != null && item.getItem() != null && item.getItem() instanceof IInventoryItem) {
						return containers.get(ID).getConstructor(IInventory.class, InventoryPlayer.class)
							.newInstance(((IInventoryItem) item.getItem()).createItemStackInventory(item), player.inventory);
					}
					AsieLibMod.log.warn("Invalid tile/item for requested ContainerBase! This is a bug!");
					return null;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		try {
			ContainerInventoryBase cb = (ContainerInventoryBase) getServerGuiElement(ID, player, world, x, y, z);
			if(ID < 0 || ID >= guis.size()) {
				return null;
			} else {
				return guis.get(ID).getConstructor(ContainerInventoryBase.class).newInstance(cb);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
