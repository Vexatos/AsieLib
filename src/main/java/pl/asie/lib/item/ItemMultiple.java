package pl.asie.lib.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemMultiple extends Item {
	private final String mod;
	private final String[] parts;
	private final Icon[] partIcons;
	
	public ItemMultiple(int id, String mod, String[] parts) {
		super(id);
		this.mod = mod;
		this.parts = parts;
		this.partIcons = new Icon[parts.length];
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int meta) {
		return this.partIcons[meta % partIcons.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister r) {
		for(int i = 0; i < parts.length; i++) {
			partIcons[i] = r.registerIcon(mod + ":" + parts[i]);
		}
	}
	
	@Override
    public String getUnlocalizedName() {
		return "item.asielib.unknown";
    }
	
	@Override
    public String getUnlocalizedName(ItemStack stack) {
		if(stack == null) return "item.asielib.unknown";
        else return "item." + this.mod + "." + this.parts[stack.getItemDamage() % parts.length];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs tabs, List list) {
		for(int i = 0; i < parts.length; i++) {
			list.add(new ItemStack(id, 1, i));
		}
     }
}
