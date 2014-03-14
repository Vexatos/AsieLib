package pl.asie.lib.util;

import pl.asie.lib.block.TileEntityInventory;
import pl.asie.lib.gui.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiUtils {
	public static TileEntityInventory currentTileEntity() {
		GuiScreen gc = Minecraft.getMinecraft().currentScreen;
		if(gc instanceof GuiBase) {
			return ((GuiBase)gc).container.getEntity();
		}
		return null;
	}
	
	public static GuiBase currentGui() {
		GuiScreen gc = Minecraft.getMinecraft().currentScreen;
		if(gc instanceof GuiBase) return ((GuiBase)gc);
		return null;
	}
}
