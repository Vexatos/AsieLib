package pl.asie.lib.gui;

import org.lwjgl.opengl.GL11;

import pl.asie.lib.block.ContainerBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiBase extends GuiContainer {
	private final ResourceLocation texture;
	public int xCenter, yCenter;
	public final ContainerBase container;
	
	public GuiBase(ContainerBase container, String textureName, int xSize, int ySize) {
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

}
