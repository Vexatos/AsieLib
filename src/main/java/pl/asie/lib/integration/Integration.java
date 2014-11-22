package pl.asie.lib.integration;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.Optional;
import crazypants.enderio.api.tool.ITool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pl.asie.lib.reference.Mods;

public class Integration {
	private boolean bcLoaded = false;
	private boolean cofhLoaded = false;
	private boolean eioLoaded = false;

	public Integration() {
		bcLoaded = ModAPIManager.INSTANCE.hasAPI(Mods.API.BuildCraftTools);
		cofhLoaded = ModAPIManager.INSTANCE.hasAPI(Mods.API.CoFHItems);
		eioLoaded = ModAPIManager.INSTANCE.hasAPI(Mods.API.EnderIOTools);
	}

	@Optional.Method(modid = Mods.API.BuildCraftTools)
	private boolean bc_isWrench(Item item) {
		return (item instanceof IToolWrench);
	}

	@Optional.Method(modid = Mods.API.BuildCraftTools)
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

	@Optional.Method(modid = Mods.API.CoFHItems)
	private boolean cofh_isHammer(ItemStack stack) {
		return (stack.getItem() instanceof IToolHammer);
	}

	@Optional.Method(modid = Mods.API.CoFHItems)
	private boolean cofh_hammer(ItemStack stack, EntityPlayer player, int x, int y, int z) {
		if(stack.getItem() instanceof IToolHammer) {
			IToolHammer hammer = (IToolHammer) stack.getItem();
			if(hammer.isUsable(stack, player, x, y, z)) {
				hammer.toolUsed(stack, player, x, y, z);
				return true;
			}
		}
		return false;
	}

	@Optional.Method(modid = Mods.API.EnderIOTools)
	private boolean eio_isTool(ItemStack stack) {
		return (stack.getItem() instanceof ITool);
	}

	@Optional.Method(modid = Mods.API.EnderIOTools)
	private boolean eio_tool(ItemStack stack, EntityPlayer player, int x, int y, int z) {
		if(stack.getItem() instanceof ITool) {
			ITool tool = ((ITool) stack.getItem());
			if(tool.canUse(stack, player, x, y, z)) {
				tool.used(stack, player, x, y, z);
				return true;
			}
		}
		return false;
	}

	public boolean isWrench(ItemStack stack) {
		return (bcLoaded && bc_isWrench(stack.getItem()))
			|| (eioLoaded && eio_isTool(stack))
			|| (cofhLoaded && cofh_isHammer(stack));
	}

	public boolean wrench(ItemStack stack, EntityPlayer player, int x, int y, int z) {
		return (bcLoaded && bc_wrench(stack.getItem(), player, x, y, z))
			|| (eioLoaded && eio_tool(stack, player, x, y, z))
			|| (cofhLoaded && cofh_hammer(stack, player, x, y, z));
	}
}
