package pl.asie.lib.block;

import pl.asie.lib.block.TileEntitySlots.EnumIO;
import pl.asie.lib.client.BlockBaseRender;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSlots extends BlockBase {
	private IIcon sideInput, sideOutput;
	
	public BlockSlots(Material material, Object parent) {
		super(material, parent);
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
	
	public IIcon getSlotIcon(World world, int x, int y, int z, int side) {
		TileEntitySlots te = (TileEntitySlots)world.getTileEntity(x, y, z);
		EnumIO slot = te.getSlotForSide(side);
		return slot == EnumIO.INPUT ? sideInput : (slot == EnumIO.OUTPUT ? sideOutput : null);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister ir) {
		super.registerBlockIcons(ir);
		sideInput = ir.registerIcon("asielib:side_input");
		sideOutput = ir.registerIcon("asielib:side_output");
	}
}
