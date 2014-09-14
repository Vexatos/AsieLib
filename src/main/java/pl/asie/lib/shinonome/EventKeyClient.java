package pl.asie.lib.shinonome;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import pl.asie.lib.AsieLibMod;

public class EventKeyClient {
	private static boolean isSpinning = false;
	
	public void scheduleSpin() {
		isSpinning = true;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void asieEvent(RenderPlayerEvent.SetArmorModel event) {
		if(EventKeyServer.isEntityNano(event.entityPlayer) && event.slot == 1 && (event.stack == null || event.stack.getItem() == AsieLibMod.itemKey)) {
			// Render the key on top.
			ItemStack key = new ItemStack(AsieLibMod.itemKey, 1);
			try {
				event.renderer.bindTexture(RenderBiped.getArmorResource(event.entityPlayer, key, 1, null));
	            ModelBiped modelbiped = event.renderer.modelArmorChestplate;
	            modelbiped.bipedHead.showModel = false;
	            modelbiped.bipedHeadwear.showModel = false;
	            modelbiped.bipedBody.showModel = true;
	            modelbiped.bipedRightArm.showModel = false;
	            modelbiped.bipedLeftArm.showModel = false;
	            modelbiped.bipedRightLeg.showModel = false;
	            modelbiped.bipedLeftLeg.showModel = false;
	            modelbiped = ForgeHooksClient.getArmorModel(event.entityPlayer, key, 1, modelbiped);
	            event.renderer.setRenderPassModel(modelbiped);
	            ModelBase mainModel = event.renderer.mainModel;
	            modelbiped.onGround = mainModel.onGround;
	            modelbiped.isRiding = mainModel.isRiding;
	            modelbiped.isChild = mainModel.isChild;
	            event.result = 1;
			} catch(Exception e) {
				e.printStackTrace();
				event.result = 0;
			}
            return;
		} else if(event.stack != null && event.stack.getItem() == AsieLibMod.itemKey) {
			event.result = 0;
			return;
		}
	}

	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if(event.phase != Phase.START) return;
		if(isSpinning) {
			ModelKey.angle += 10.0F;
			if(ModelKey.angle >= 180.0F) {
				ModelKey.angle = 0.0F;
				isSpinning = false;
			}
		}
	}
}
