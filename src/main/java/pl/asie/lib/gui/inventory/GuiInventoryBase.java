package pl.asie.lib.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author Vexatos
 */
public class GuiInventoryBase extends GuiContainer {
	private final ResourceLocation texture;
	public int xCenter, yCenter;
	public final ContainerInventoryBase container;

	public GuiInventoryBase(ContainerInventoryBase container, String textureName, int xSize, int ySize) {
		super(container);
		this.container = container;
		this.texture = new ResourceLocation(textureName.split(":")[0], "textures/gui/" + textureName.split(":")[1] + ".png");
		this.xSize = xSize;
		this.ySize = ySize;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.xCenter = (this.width - this.xSize) / 2;
		this.yCenter = (this.height - this.ySize) / 2;
		this.mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(this.xCenter, this.yCenter, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void handleMouseClick(Slot slot, int slotNumber, int button, int shift) {
		if(!(slot != null && container.getInventoryObject() != null
			&& container.getInventoryObject() instanceof ItemStackInventory
			&& slot.getStack() == ((ItemStackInventory) container.getInventoryObject()).container())) {
			super.handleMouseClick(slot, slotNumber, button, shift);
		}
	}

	@Override
	protected boolean checkHotbarKeys(int slot) {
		return !(container.getInventoryObject() != null
			&& container.getInventoryObject() instanceof ItemStackInventory);
	}
}
