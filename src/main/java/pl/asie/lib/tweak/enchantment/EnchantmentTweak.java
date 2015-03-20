package pl.asie.lib.tweak.enchantment;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.AnvilUpdateEvent;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.util.RayTracer;

/**
 * @author Vexatos
 */
public class EnchantmentTweak {

	public static EnchantmentBetterBane bane;

	public static void registerBaneEnchantment(int enchID) {
		if(!(enchID < 0 || enchID >= 256)) {
			if(Enchantment.enchantmentsList[enchID] == null) {
				bane = new EnchantmentBetterBane(244);
				return;
			}
			for(int i = enchID; i < 256; i++) {
				if(Enchantment.enchantmentsList[i] == null) {
					AsieLibMod.log.info("Enchantment ID " + enchID + " already occupied, using " + i + " instead");
					bane = new EnchantmentBetterBane(i);
					return;
				}
			}
		}
		throw new IllegalArgumentException("No valid enchantment id! " + EnchantmentBetterBane.class + " Enchantment ID:" + enchID);
	}

	@SubscribeEvent
	public void anvilEvent(AnvilUpdateEvent e) {
		if(e.left.isItemStackDamageable() && e.left.isItemEnchanted()
			&& e.right.getItem() == Items.fermented_spider_eye) {
			if(e.right.stackSize == 64) {
				e.output = e.left.copy();
				e.cost = 37;
				addBaneEnchantment(e.output, 9);
			} else {
				e.output = null;
				if(e.isCancelable()) {
					e.setCanceled(true);
				}
			}
		} else if(hasBaneEnchantment(e.right) || hasBaneEnchantment(e.left)) {
			e.output = null;
			if(e.isCancelable()) {
				e.setCanceled(true);
			}
		}
	}

	/*@SubscribeEvent
	public void enchEvent(TickEvent.ClientTickEvent e) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(player != null && player.getCurrentEquippedItem() != null && hasBaneEnchantment(player.getCurrentEquippedItem())
			&& player.getCurrentEquippedItem().isItemStackDamageable()) {
			RayTracing.instance().fire(20.0F);
			MovingObjectPosition pos = RayTracing.instance().getTarget();
			if(pos != null && pos.typeOfHit != null && pos.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
				Entity entity = pos.entityHit;
				if(entity != null && entity instanceof EntityLivingBase
					&& ((EntityLivingBase) entity).getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD
					&& entity.hurtResistantTime <= 10) {
					try {
						AsieLibMod.packet.sendToServer(
							AsieLibMod.packet.create(Packets.ATTACK_ENTITY)
								.writeInt(entity.getEntityId())
					} catch(IOException e1) {
						//NO-OP
					}
				}
			}
		}
	}*/

	@SubscribeEvent
	public void enchEvent(TickEvent.PlayerTickEvent e) {
		EntityPlayer player = e.player;
		if(player.worldObj.isRemote) {
			return;
		}
		if(player.getCurrentEquippedItem() != null && hasBaneEnchantment(player.getCurrentEquippedItem()) && player.getCurrentEquippedItem().isItemStackDamageable()) {
			RayTracer.instance().fire(player, 10.0);
			MovingObjectPosition target = RayTracer.instance().getTarget();
			if(target != null && target.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
				Entity entity = target.entityHit;
				if(entity != null
					&& entity instanceof EntityLivingBase
					&& ((EntityLivingBase) entity).getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD
					&& entity.hurtResistantTime <= 10
					&& !player.isBlocking()) {
					player.attackTargetEntityWithCurrentItem(entity);
					if(player.getCurrentEquippedItem().isItemStackDamageable()) {
						float distance = player.getDistanceToEntity(entity);
						int damage = Math.max(Math.min((int) distance + 1, 10), 1);
						player.getCurrentEquippedItem().damageItem(damage, player);
					}
				}
			}
		}
	}

	private static boolean hasBaneEnchantment(ItemStack stack) {
		if(stack.getTagCompound() == null || stack.getTagCompound().hasNoTags()) {
			return false;
		}

		if(!stack.getTagCompound().hasKey("ench", 9)) {
			return false;
		}

		NBTTagList list = stack.getTagCompound().getTagList("ench", 10);
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			if(tag != null && tag.getShort("id") == bane.effectId) {
				return true;
			}
		}
		return false;
	}

	private static boolean addBaneEnchantment(ItemStack stack, int level) {
		if(stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if(!stack.getTagCompound().hasKey("ench", 9)) {
			stack.getTagCompound().setTag("ench", new NBTTagList());
		}

		NBTTagList list = stack.getTagCompound().getTagList("ench", 10);
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			if(tag != null && tag.getShort("id") == Enchantment.baneOfArthropods.effectId
				&& tag.getShort("lvl") == (short) 5) {
				list.removeTag(i);
				NBTTagCompound data = new NBTTagCompound();
				data.setShort("id", (short) bane.effectId);
				data.setShort("lvl", (short) ((byte) level));
				list.appendTag(data);
				return true;
			}
		}
		return false;
	}
}
