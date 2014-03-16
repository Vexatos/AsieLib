package pl.asie.lib.block;

import buildcraft.api.tools.IToolWrench;
import pl.asie.lib.util.ItemUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockBase extends BlockContainer {
	private final Object parent;
	private int gui = -1;
	
	public BlockBase(Material material, Object parent) {
		super(material);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setHardness(2.0F);
		this.parent = parent;
	}
	
	// Handler: Redstone
	
	public boolean emitsRedstone(IBlockAccess world, int x, int y, int z) {
		return false;
	}
	
	public boolean receivesRedstone(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean canProvidePower() { return true; }
	
	// Vanilla
    public int getVanillaRedstoneValue(World world, int x, int y, int z) {
    	return world.getBlockPowerInput(x, y, z);
    }
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if(receivesRedstone(world, x, y, z)) {
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null && te instanceof TileEntityBase)
				((TileEntityBase)te).onRedstoneSignal_internal(getVanillaRedstoneValue(world, x, y, z));
		}
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return (emitsRedstone(world, x, y, z) || receivesRedstone(world, x, y, z));
	}

    @Override
	public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side) {
    	if(!emitsRedstone(access,x,y,z)) return 0;
		TileEntity te = access.getTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityBase)
			return ((TileEntityBase)te).requestCurrentRedstoneValue(side);
		return 0;
    }
    
	public abstract TileEntity createNewTileEntity(World world, int metadata);
	
	@Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return createNewTileEntity(world, metadata);
    }

	// Direction placement
	
	private boolean rotateFrontSide = false;
	
	public ForgeDirection getFacingDirection(World world, int x, int y, int z) {
		if(!rotateFrontSide) return null;
		int m = world.getBlockMetadata(x, y, z) & 3;
		return ForgeDirection.getOrientation(m+2);
	}
	
	public void setRotateFrontSide(boolean v) { rotateFrontSide = v; }
	
    private void setDefaultFrontSideDirection(World world, int x, int y, int z)
    {
        if (!world.isRemote)
        {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            int m = world.getBlockMetadata(x, y, z);
            byte b0 = 3;

            if (block.func_149730_j() && !block1.func_149730_j())
            {
                b0 = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j())
            {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j())
            {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j())
            {
                b0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, m | b0, 2);
        }
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
    	if(rotateFrontSide) {
	        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

	        int m = stack.getItemDamage() & (~3);
	        if (l == 0) world.setBlockMetadataWithNotify(x, y, z, m | 0, 2);
	        if (l == 1) world.setBlockMetadataWithNotify(x, y, z, m | 3, 2);
	        if (l == 2) world.setBlockMetadataWithNotify(x, y, z, m | 1, 2);
	        if (l == 3) world.setBlockMetadataWithNotify(x, y, z, m | 2, 2);
	    }
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        if(rotateFrontSide) {
        	this.setDefaultFrontSideDirection(world, x, y, z);
        }
    }

	// GUI handling
	
	public Object getOwner() { return parent; }
	
	public void setGuiID(int gui) { if(gui >= 0) this.gui = gui; }
	public boolean hasGui() { return (gui >= 0); }
	public int getGuiID() { return gui; }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(player.isSneaking()) return false;
		if(!world.isRemote) {
			ItemStack held = player.inventory.getCurrentItem();
			if(held != null && held.getItem() != null && held.getItem() instanceof IToolWrench && this.rotateFrontSide) {
				int meta = world.getBlockMetadata(x, y, z);
				world.setBlockMetadataWithNotify(x, y, z, (meta & (~3)) | (((meta & 3) + 1) & 3), 2);
			} else player.openGui(this.parent, this.gui, world, x, y, z);
		}
		return true;
	}
	
	// Simple textures
	
	private String iconName = null;
	private IIcon icon = null;
	
	public void setIconName(String name) {
		iconName = name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		if(iconName != null) icon = reg.registerIcon(iconName);
	}
	
	// Block destroy unified handler and whatnot.
	public void onBlockDestroyed(World world, int x, int y, int z, int meta) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(tileEntity != null) {
			if(tileEntity instanceof TileEntityBase) {
				((TileEntityBase)tileEntity).onBlockDestroy();
			}
			if(tileEntity instanceof IInventory) {
				ItemUtils.dropItems(world, x, y, z, (IInventory)tileEntity);
			}
			tileEntity.invalidate();
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		super.onBlockDestroyedByPlayer(world, x, y, z, meta);
		this.onBlockDestroyed(world, x, y, z, meta);
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
		super.onBlockDestroyedByExplosion(world, x, y, z, explosion);
		this.onBlockDestroyed(world, x, y, z, 0);
	}

    @Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    	this.onBlockDestroyed(world, x, y, z, meta);
    	super.breakBlock(world, x, y, z, block, meta);
    }
}
