package mffs.machine;


import icbm.api.ICamouflageMaterial;
import mffs.MFFSConfiguration;
import mffs.MFFSCreativeTab;
import mffs.ZhuYao;
import mffs.item.card.ItemCardLink;
import mffs.machine.tile.TileSecurityStation;
import mffs.machine.tile.TileMFFS;
import mffs.quanran.RHJiQi;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockRotatable;
import universalelectricity.prefab.implement.IRedstoneReceptor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockMachine extends BlockRotatable implements ICamouflageMaterial
{
	private Icon iconOn, iconMachineOn, iconMachineOff;

	public BlockMachine(int id, String name)
	{
		super(MFFSConfiguration.CONFIGURATION.getBlock(name, id).getInt(id), UniversalElectricity.machine);
		this.setUnlocalizedName(ZhuYao.PREFIX + name);
		this.setBlockUnbreakable();
		this.setResistance(100.0F);
		this.setStepSound(soundMetalFootstep);
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
	}

	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			TileMFFS tileEntity = (TileMFFS) world.getBlockTileEntity(x, y, z);
			ItemStack equippedItem = entityPlayer.getCurrentEquippedItem();

			if (equippedItem != null)
			{
				if (equippedItem.getItem() instanceof ItemCardLink)
				{
					return false;
				}
			}

			if (tileEntity instanceof TileSecurityStation)
			{
				if (((TileSecurityStation) tileEntity).getOwner() != null)
				{
					if (!((TileSecurityStation) tileEntity).getOwner().equalsIgnoreCase(entityPlayer.username))
					{
						return false;
					}
				}
			}

			if (!world.isRemote)
			{
				entityPlayer.openGui(ZhuYao.instance, 0, world, x, y, z);
			}
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itemStack)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof TileMFFS)
		{
			TileMFFS tileEntity = (TileMFFS) tile;
			int side = MathHelper.floor_double(entityliving.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
			int height = Math.round(entityliving.rotationPitch);

			if (height >= 65)
			{
				tileEntity.setDirection(world, x, y, z, ForgeDirection.getOrientation(1));
			}
			else if (height <= -65)
			{
				tileEntity.setDirection(world, x, y, z, ForgeDirection.getOrientation(0));
			}
			else if (side == 0)
			{
				tileEntity.setDirection(world, x, y, z, ForgeDirection.getOrientation(2));
			}
			else if (side == 1)
			{
				tileEntity.setDirection(world, x, y, z, ForgeDirection.getOrientation(5));
			}
			else if (side == 2)
			{
				tileEntity.setDirection(world, x, y, z, ForgeDirection.getOrientation(3));
			}
			else if (side == 3)
			{
				tileEntity.setDirection(world, x, y, z, ForgeDirection.getOrientation(4));
			}
		}
	}

	@Override
	public boolean onSneakMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		return this.onUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		this.setDirection(world, x, y, z, ForgeDirection.getOrientation(ForgeDirection.ROTATION_MATRIX[0][this.getDirection(world, x, y, z).ordinal()]));
		return true;
	}

	@Override
	public boolean onSneakUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		this.setDirection(world, x, y, z, ForgeDirection.getOrientation(ForgeDirection.ROTATION_MATRIX[2][this.getDirection(world, x, y, z).ordinal()]));
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

			if (tileEntity instanceof IRedstoneReceptor)
			{
				if (world.isBlockIndirectlyGettingPowered(x, y, z))
				{
					((IRedstoneReceptor) tileEntity).onPowerOn();
				}
				else
				{
					((IRedstoneReceptor) tileEntity).onPowerOff();
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(this.getUnlocalizedName2() + "_off");
		this.iconOn = par1IconRegister.registerIcon(this.getUnlocalizedName2() + "_on");
		this.iconMachineOn = par1IconRegister.registerIcon(ZhuYao.PREFIX + "machine_on");
		this.iconMachineOff = par1IconRegister.registerIcon(ZhuYao.PREFIX + "machine_off");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		TileEntity t = iBlockAccess.getBlockTileEntity(x, y, z);

		if (t instanceof TileMFFS)
		{
			TileMFFS tileEntity = (TileMFFS) t;

			ForgeDirection blockfacing = ForgeDirection.getOrientation(side);
			ForgeDirection facingDirection = tileEntity.getDirection(null, x, y, z);

			if (blockfacing.equals(facingDirection))
			{
				if (tileEntity.isActive())
				{
					return this.iconMachineOn;
				}
				else
				{
					return this.iconMachineOff;
				}
			}

			if (tileEntity.isActive())
			{
				return this.iconOn;
			}
		}

		return this.blockIcon;
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int i, int j, int k, double d, double d1, double d2)
	{
		if ((world.getBlockTileEntity(i, j, k) instanceof TileMFFS))
		{
			TileEntity tileentity = world.getBlockTileEntity(i, j, k);
			if (((TileMFFS) tileentity).isActive())
			{
				return 999.0F;
			}
			return 100.0F;
		}

		return 100.0F;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType()
	{
		return RHJiQi.ID;
	}
}