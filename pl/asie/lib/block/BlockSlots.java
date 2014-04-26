package pl.asie.lib.block;

import pl.asie.lib.block.TileEntitySlots.EnumIO;
import pl.asie.lib.client.BlockBaseRender;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockSlots extends BlockBase {
	private Icon sideInput, sideOutput;
	
	public BlockSlots(int id, Material material, Object parent) {
		super(id, material, parent);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntitySlots();
	}

	@Override
    public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
    public boolean canRenderInPass(int pass) {
	    BlockBaseRender.renderPass = pass;
	    return true;
	}
	
	@Override
    public int getRenderBlockPass() {
		return 1;
	}
	
	public Icon getSlotIcon(World world, int x, int y, int z, int side) {
		TileEntitySlots te = (TileEntitySlots)world.getBlockTileEntity(x, y, z);
		EnumIO slot = te.getSlotForSide(side);
		return slot == EnumIO.INPUT ? sideInput : (slot == EnumIO.OUTPUT ? sideOutput : null);
	}
	
	@Override
	public void registerIcons(IconRegister ir) {
		super.registerIcons(ir);
		sideInput = ir.registerIcon("asielib:side_input");
		sideOutput = ir.registerIcon("asielib:side_output");
	}
}
