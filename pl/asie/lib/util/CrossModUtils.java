package pl.asie.lib.util;

import java.io.File;
import java.util.HashMap;

import pl.asie.lib.AsieLibMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class CrossModUtils {
	public static HashMap<String, String[]> classNames = new HashMap<String, String[]>();
	
	static {
		classNames.put("TConstruct", new String[]{"tconstruct.common.TContent"});
		classNames.put("TMechworks", new String[]{"tmechworks.common.MechContent"});
		classNames.put("BetterStorage", new String[]{"net.mcft.copy.betterstorage.content.Tiles", "net.mcft.copy.betterstorage.content.Items"});
		classNames.put("AntiqueAtlas", new String[]{"hunternif.mc.atlas.AntiqueAtlasMod"});
		classNames.put("ComputerCraft", new String[]{"dan200.computercraft.ComputerCraft$Blocks", "dan200.computercraft.ComputerCraft$Items"});
		classNames.put("Foundry", new String[]{"exter.foundry.item.FoundryItems"});
		classNames.put("OpenBlocks", new String[]{"openblocks.OpenBlocks$Blocks", "openblocks.OpenBlocks$Items"});
	}
	
	private static HashMap<String, Configuration> configFiles = new HashMap<String, Configuration>();
	
	public static ItemStack getItemStackFromConfig(String modid, String category, String name, int stackSize, int metadata, boolean isItem) {
		Configuration config = configFiles.get(modid);
		if(config == null) {
			File configFile = null;
			if(modid.equalsIgnoreCase("buildcraft"))
				configFile = new File(Loader.instance().getConfigDir(), "buildcraft/main.conf");
			else if(modid.equalsIgnoreCase("EnderIO"))
				configFile = new File(Loader.instance().getConfigDir(), "enderio/EnderIO.conf");
			else
				configFile = new File(Loader.instance().getConfigDir(), modid + ".cfg");
			if(configFile != null && configFile.exists()) {
				config = new Configuration(configFile);
				config.load();
				configFiles.put(modid, config);
			} else return null;
		}
		if(config.hasKey(category, name)) {
			int id = config.get(category, name, -1).getInt();
			if(isItem && !modid.equals("immibis") && !modid.equals("foundry")) id += 256;
			if(id > 0 && id < 32768 && Item.itemsList[id] != null) {
				try {
					return new ItemStack(Item.itemsList[id], stackSize, metadata);
				} catch(Exception e) {
					e.printStackTrace();
					return null;
				}
			} else return null;
		} else return null;
	}
	
	public static ItemStack getItemStack(String modid, String name, int stackSize, int metadata) {
		 if(classNames.containsKey(modid)) { // Use classNames
			for(String classname: classNames.get(modid)) {
				try {
					Class klass = null;
					if(classname.split("\\$").length == 1) {
						klass = CrossModUtils.class.getClassLoader().loadClass(classname);
					} else {
						klass = CrossModUtils.class.getClassLoader().loadClass(classname.split("\\$")[0]);
						for(Class innerklass: klass.getDeclaredClasses()) {
							if(innerklass.getName().equals(classname)) klass = innerklass;
						}
					}
					if(klass != null) {
						Object o = klass.getField(name).get(null);
						if(o instanceof Block) return new ItemStack((Block)o, stackSize, metadata);
						else if(o instanceof Item) return new ItemStack((Item)o, stackSize, metadata);
						else if(o instanceof Integer) return new ItemStack(((Integer)o).intValue(), stackSize, metadata);
						else {
							try {
								int id = klass.getField(name).getInt(null);
								return new ItemStack(id, stackSize, metadata);
							} catch(Exception e) {
								AsieLibMod.log.warning("Could not get right object for field " + name + " in class " + classNames.get(modid));
							}
						}
					}
				} catch(Exception e) { e.printStackTrace(); }
			}
			// We're still here!?
			AsieLibMod.log.severe("Could not load class for mod " + modid + ", trying worst-case alternatives - PROBABLY BROKEN");
		}
		if(GameRegistry.findBlock(modid, name) != null)
			return new ItemStack(GameRegistry.findBlock(modid, name), stackSize, metadata);
		else if(GameRegistry.findItem(modid, name) != null)
			return new ItemStack(GameRegistry.findItem(modid, name), stackSize, metadata);
		else if(GameRegistry.findItemStack(modid, name, stackSize) != null) {
			ItemStack stack = GameRegistry.findItemStack(modid, name, stackSize);
			if(stack != null) stack.setItemDamage(metadata);
			return stack;
		}
		return null; // in case we already haven't
	}
	
	public static void renameItemStack(ItemStack target, String name) {
		if(target == null) return;
		Item item = target.getItem();
		item.setUnlocalizedName(name);
		if(item instanceof ItemBlock) {
			Block.blocksList[((ItemBlock)item).getBlockID()].setUnlocalizedName(name);
		}
	}
	
	public static void renameItem(Item target, String name) {
		renameItemStack(new ItemStack(target, 1, 0), name);
	}
}
