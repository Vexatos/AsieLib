package pl.asie.lib.shinonome;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.Packets;
import pl.asie.lib.network.Packet;

public class EventKeyServer {

	public static boolean isEntityNano(Entity entity) {
		return (entity != null && entity instanceof EntityPlayer && (entity.getCommandSenderName().equals("asiekierka")));
	}
	
	@SubscribeEvent
	public void containerEvent(PlayerInteractEvent event) {
		if(isEntityNano(event.entityPlayer) && event.action == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.entity.worldObj.getBlock(event.x, event.y, event.z);
			if(block instanceof BlockContainer || (Math.random() < 0.1))
				scheduleSpin();
		}
	}
	
	@SubscribeEvent
	public void damageEvent(LivingHurtEvent event) {
		if(isEntityNano(event.entity) && event.ammount > 0.0F) {
			scheduleSpin();
		}
	}
	
	public void scheduleSpin() {
		try {
			Packet packet = AsieLibMod.packet.create(Packets.NANO_NANO);
			AsieLibMod.packet.sendToAll(packet);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
