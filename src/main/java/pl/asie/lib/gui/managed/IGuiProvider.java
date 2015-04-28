package pl.asie.lib.gui.managed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * @author Vexatos
 */
public interface IGuiProvider {

	@SideOnly(Side.CLIENT)
	public GuiContainer makeGui(int ID, EntityPlayer player, World world, int x, int y, int z);

	public Container makeContainer(int ID, EntityPlayer player, World world, int x, int y, int z);

	public boolean canOpen(World world, int x, int y, int z, EntityPlayer player, int side);

	public void setGuiID(int guiID);

	public int getGuiID();
}
