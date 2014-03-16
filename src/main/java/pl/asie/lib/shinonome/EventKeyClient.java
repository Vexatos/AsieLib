package pl.asie.lib.shinonome;

import java.util.Date;
import java.util.EnumSet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.Packets;
import pl.asie.lib.network.Packet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

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
