package pl.asie.lib.integration;

import buildcraft.api.tools.IToolWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

public class Integration {
	private boolean bcLoaded = false;
	
	public Integration() {
		bcLoaded = Loader.isModLoaded("BuildCraft|Core");
	}
	
	@Optional.Method(modid = "BuildCraft|Core")
	private boolean bc_isWrench(Item item) {
		return (item instanceof IToolWrench);
	}
	
	@Optional.Method(modid = "BuildCraft|Core")
	private boolean bc_wrench(Item item, EntityPlayer player, int x, int y, int z) {
		if(item instanceof IToolWrench) {
			IToolWrench wrench = (IToolWrench)item;
			if(wrench.canWrench(player, x, y, z)) {
				wrench.wrenchUsed(player, x, y, z);
				return true;
			}
		}
		return false;
	}
	
	public boolean isWrench(Item item) {
		return (bcLoaded ? bc_isWrench(item) : false) || false;
	}

	public boolean wrench(Item item, EntityPlayer player, int x, int y, int z) {
		return (bcLoaded ? bc_wrench(item, player, x, y, z) : false) || false;
	}
}
