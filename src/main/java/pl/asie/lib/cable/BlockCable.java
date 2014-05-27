package pl.asie.lib.cable;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pl.asie.lib.block.BlockBase;

public class BlockCable extends BlockBase {
	private ICableManager manager;
	
	public BlockCable(Material material, ICableManager manager) {
		super(material, manager);
		this.manager = manager;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityCable(manager);
	}
}
