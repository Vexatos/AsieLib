package pl.asie.lib.integration;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.Optional;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Integration {
	private boolean bcLoaded = false;
	private boolean cofhLoaded = false;

	public Integration() {
		bcLoaded = ModAPIManager.INSTANCE.hasAPI("BuildCraftAPI|tools");
		cofhLoaded = ModAPIManager.INSTANCE.hasAPI("CoFHAPI|item");
	}

	@Optional.Method(modid = "BuildCraft|Core")
	private boolean bc_isWrench(Item item) {
		return (item instanceof IToolWrench);
	}

	@Optional.Method(modid = "BuildCraft|Core")
	private boolean bc_wrench(Item item, EntityPlayer player, int x, int y, int z) {
		if(item instanceof IToolWrench) {
			IToolWrench wrench = (IToolWrench) item;
			if(wrench.canWrench(player, x, y, z)) {
				wrench.wrenchUsed(player, x, y, z);
				return true;
			}
		}
		return false;
	}

	private boolean cofh_isHammer(ItemStack item) {
		return (item.getItem() instanceof IToolHammer);
	}

	private boolean cofh_hammer(ItemStack item, EntityPlayer player, int x, int y, int z) {
		if(item.getItem() instanceof IToolHammer) {
			IToolHammer hammer = (IToolHammer) item.getItem();
			if(hammer.isUsable(item, player, x, y, z)) {
				hammer.toolUsed(item, player, x, y, z);
				return true;
			}
		}
		return false;
	}

	public boolean isWrench(ItemStack item) {
		return (bcLoaded && bc_isWrench(item.getItem()))
			|| (cofhLoaded && cofh_isHammer(item));
	}

	public boolean wrench(ItemStack item, EntityPlayer player, int x, int y, int z) {
		return (bcLoaded && bc_wrench(item.getItem(), player, x, y, z))
			|| (cofhLoaded && cofh_hammer(item, player, x, y, z));
	}
}
