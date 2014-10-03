package pl.asie.lib.shinonome;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemKey extends ItemArmor {

	public ItemKey() {
		super(ArmorMaterial.IRON, 2, 1);
		this.setUnlocalizedName("asielib.key");
		this.setTextureName("asielib:shark");
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@SuppressWarnings("unchecked")
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean par3)
	{
		info.add(EnumChatFormatting.GOLD + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.asielib.key"));
	}
	
	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
		return armorType == 1;
	}
	
	private ModelKey model;
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		if (armorSlot == 1) {
			if (model == null) model = new ModelKey();
			return model;
		}

		return null;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return "asielib:textures/models/key.png";
	}
}
