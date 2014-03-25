package pl.asie.lib.block;

import buildcraft.api.tools.IToolWrench;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.client.BlockBaseRender;
import pl.asie.lib.util.ItemUtils;
import pl.asie.lib.util.MiscUtils;
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
import net.minecraft.item.Item;
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
	public enum Rotation {
		NONE,
		FOUR,
		SIX
	}
	
	private Rotation rotation = Rotation.NONE;
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

	public int getFrontSide(int m) {
		switch(this.rotation) {
			case FOUR:
				return (m & 3) + 2;
			case SIX:
				return (m & 7) % 6;
			case NONE:
			default:
				return 2;
		}
	}
	
	public Rotation getRotation() { return this.rotation; }
	
	public int relToAbs(int side, int metadata) {
		int frontSide = getFrontSide(metadata);
		return MiscUtils.getAbsoluteSide(side, frontSide);
 	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getAbsoluteIcon(int side, int metadata) {
		return icon;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getAbsoluteIcon(World world, int x, int y, int z, int side, int metadata) {
		return icon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return this.getAbsoluteIcon(relToAbs(side, metadata), metadata);
	}

	public ForgeDirection getFacingDirection(World world, int x, int y, int z) {
		int m = world.getBlockMetadata(x, y, z);
		return ForgeDirection.getOrientation(getFrontSide(m));
	}
	
	@Deprecated
	public void setRotateFrontSide(boolean v) { this.rotation = Rotation.FOUR; }
	public void setRotation(Rotation v) { this.rotation = v; }
	
    private void setDefaultRotation(World world, int x, int y, int z)
    {
        if (!world.isRemote && this.rotation != Rotation.NONE)
        {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            int m = world.getBlockMetadata(x, y, z);
            byte b0 = 3;

            if (block.func_149730_j() && !block1.func_149730_j()) b0 = 3;
            else if (block1.func_149730_j() && !block.func_149730_j()) b0 = 2;

            if (block2.func_149730_j() && !block3.func_149730_j()) b0 = 5;
            else if (block3.func_149730_j() && !block2.func_149730_j()) b0 = 4;

            if(this.rotation == Rotation.SIX && y > 0 && y < 255) {
                Block block4 = world.getBlock(x, y - 1, z);
                Block block5 = world.getBlock(x, y + 1, z);
                
                if (block4.func_149730_j() && !block5.func_149730_j()) b0 = 1;
                else if (block5.func_149730_j() && !block4.func_149730_j()) b0 = 0;
            } else b0 -= 2;
            
            world.setBlockMetadataWithNotify(x, y, z, m | b0, 2);
        }
    }
    
    private static final int[] ROT_TRANSFORM4 = {2, 5, 3, 4};
    
    private int determineRotation(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
    	if(this.rotation == Rotation.NONE) return 0;
    	
    	if(this.rotation == Rotation.SIX) {
	        if (MathHelper.abs((float)entity.posX - (float)x) < 2.0F && MathHelper.abs((float)entity.posZ - (float)z) < 2.0F) {
	            double d0 = entity.posY + 1.82D - (double)entity.yOffset;

	            if (d0 - (double)y > 2.0D) return 1;
	            if ((double)y - d0 > 0.0D) return 0;
	        }
    	}
    	
        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return ROT_TRANSFORM4[l];
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
    	int rot = determineRotation(world, x, y, z, entity, stack);
        int m = stack.getItemDamage();
    	if(this.rotation == Rotation.SIX)
    		world.setBlockMetadataWithNotify(x, y, z, (m & (~7)) | rot, 2);
    	else if(this.rotation == Rotation.FOUR)
    		world.setBlockMetadataWithNotify(x, y, z, (m & (~3)) | (rot - 2), 2);
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        this.setDefaultRotation(world, x, y, z);
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
			if(held != null && held.getItem() != null && AsieLibMod.integration.isWrench(held.getItem()) && this.rotation != null) {
				boolean wrenched = AsieLibMod.integration.wrench(held.getItem(), player, x, y, z);
				int meta = world.getBlockMetadata(x, y, z);
				if(this.rotation == Rotation.FOUR) {
					world.setBlockMetadataWithNotify(x, y, z, (meta & (~3)) | (((meta & 3) + 1) & 3), 2);
				} else if(this.rotation == Rotation.SIX) {
					world.setBlockMetadataWithNotify(x, y, z, (meta & (~7)) | (((meta & 7) + 1) % 6), 2);
				}
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
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
    	return BlockBaseRender.id();
    }
}
