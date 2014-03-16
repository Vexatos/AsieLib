package pl.asie.lib.util;

import java.io.File;
import java.util.HashMap;

import pl.asie.lib.AsieLibMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class CrossModUtils {
	public static void renameItemStack(ItemStack target, String name) {
		if(target == null) return;
		Item item = target.getItem();
		item.setUnlocalizedName(name);
		if(item instanceof ItemBlock) {
			Block.getBlockFromItem(((ItemBlock)item)).setBlockName(name);
		}
	}
	
	public static void renameItem(Item target, String name) {
		renameItemStack(new ItemStack(target, 1, 0), name);
	}
}
