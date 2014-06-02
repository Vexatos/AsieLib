package pl.asie.lib.shinonome;

import java.util.Date;
import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.Packets;
import pl.asie.lib.network.PacketInput;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class EventKeyClient implements ITickHandler {
	private static boolean isSpinning = false;
	
	public void scheduleSpin() {
		isSpinning = true;
	}

	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void asieEvent(RenderPlayerEvent.SetArmorModel event) {
		if(EventKeyServer.isEntityNano(event.entityPlayer) && event.slot == 1 && (event.stack == null || event.stack.getItem() == AsieLibMod.itemKey)) {
			// Render the key on top.
			ItemStack key = new ItemStack(AsieLibMod.itemKey, 1);
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
            modelbiped.onGround = event.renderer.mainModel.onGround;
            modelbiped.isRiding = event.renderer.mainModel.isRiding;
            modelbiped.isChild = event.renderer.mainModel.isChild;
            event.result = 1;
            return;
		} else if(event.stack != null && event.stack.getItem() == AsieLibMod.itemKey) {
			event.result = 0;
			return;
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(isSpinning) {
			ModelKey.angle += 10.0F;
			if(ModelKey.angle >= 180.0F) {
				ModelKey.angle = 0.0F;
				isSpinning = false;
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "ShinonomeLaboratories";
	}
}
