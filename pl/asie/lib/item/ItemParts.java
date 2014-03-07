package pl.asie.lib.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemParts extends Item {
	private final String mod;
	private final String[] parts;
	private final Icon[] partIcons;
	
	public ItemParts(int id, String mod, String[] parts) {
		super(id);
		this.mod = mod;
		this.parts = parts;
		this.partIcons = new Icon[parts.length];
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
			partIcons[i] = r.registerIcon(mod + ":part_" + parts[i]);
		}
	}
	
	@Override
    public String getUnlocalizedName() {
		return "item.asielib.part_unknown";
    }
	
	@Override
    public String getUnlocalizedName(ItemStack stack) {
		if(stack == null) return "item.asielib.part_unknown";
        else return "item." + this.mod + ".part_" + this.parts[stack.getItemDamage() % parts.length];
    }
}
