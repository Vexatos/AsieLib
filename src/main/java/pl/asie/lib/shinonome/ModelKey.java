package pl.asie.lib.shinonome;

import java.util.Date;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelKey extends ModelBiped {
	private static final int DELTA_Y = 0;
	
	public ModelKey() {
		super();

        this.bipedBody = new ModelRenderer(this, 40, 0);
        this.bipedBody.addBox(-4.0F, 0.0F - 3.0F, -2.0F, 8, 12, 4);
        
        // gear part 1: handle intensifies
        this.bipedBody.setTextureOffset(10, 0);
        this.bipedBody.addBox(-1.0F, 2.0F - 3.0F, 2.0F, 2, 2, 2);
        
        // gear part 2: spinny!
        this.bipedBody.setTextureOffset(0, 20);
        this.bipedBody.addBox(-1.0F, 2.0F - 3.0F, 4.0F, 2, 2, 2);
        
        // gear parts 3: the high parts
        this.bipedBody.setTextureOffset(0, 10);
        this.bipedBody.addBox(-1.0F, 0.0F - 3.0F, 3.0F, 2, 2, 1); // front top
        this.bipedBody.addBox(-1.0F, 0.0F - 3.0F, 6.0F, 2, 2, 1); // back top
        this.bipedBody.addBox(-1.0F, 4.0F - 3.0F, 3.0F, 2, 2, 1); // front bottom
        this.bipedBody.addBox(-1.0F, 4.0F - 3.0F, 6.0F, 2, 2, 1); // back bottom
        
		// gear parts 4: the ending parts
        this.bipedBody.setTextureOffset(0, 0);
        this.bipedBody.addBox(-1.0F, -1.0F - 3.0F, 4.0F, 2, 1, 2); // top
        this.bipedBody.addBox(-1.0F, 6.0F - 3.0F, 4.0F, 2, 1, 2); // bottom
	}
	
	public static float angle = 0.0F;
	
	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 3.0F * par7, 0.0F);
		GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
		bipedBody.render(par7);
		GL11.glPopMatrix();
	}
}
