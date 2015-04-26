package pl.asie.lib.gui.managed;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * @author Vexatos
 */
public interface IGuiProvider {

	public GuiContainer makeGui(int ID, EntityPlayer player, World world, int x, int y, int z);

	public Container makeContainer(int ID, EntityPlayer player, World world, int x, int y, int z);

	public void setGuiID(int guiID);

	public int getGuiID();
}
