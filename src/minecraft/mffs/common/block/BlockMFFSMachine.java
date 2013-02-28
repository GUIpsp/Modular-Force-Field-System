package mffs.common.block;

import mffs.common.MFFSConfiguration;
import mffs.common.MFFSCreativeTab;
import mffs.common.ModularForceFieldSystem;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.card.ItemCardEmpty;
import mffs.common.card.ItemCardPowerLink;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.modules.ItemModuleBase;
import mffs.common.multitool.ItemMultitool;
import mffs.common.tileentity.TileEntityControlSystem;
import mffs.common.tileentity.TileEntityMFFS;
import mffs.common.tileentity.TileEntityProjector;
import mffs.common.tileentity.TileEntitySecurityStation;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;

public abstract class BlockMFFSMachine extends BlockMachine
{
	public BlockMFFSMachine(int id, String name)
	{
		super(MFFSConfiguration.CONFIGURATION.getBlock(name, id).getInt(id), UniversalElectricity.machine);
		this.setBlockName(name);
		this.setBlockUnbreakable();
		this.setRequiresSelfNotify();
		this.setResistance(100.0F);
		this.setStepSound(soundMetalFootstep);
		this.setRequiresSelfNotify();
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
		this.setTextureFile(ModularForceFieldSystem.BLOCK_TEXTURE_FILE);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
	{
		if (entityplayer.isSneaking())
		{
			return false;
		}

		if (world.isRemote)
		{
			return true;
		}

		TileEntityMFFS tileEntity = (TileEntityMFFS) world.getBlockTileEntity(i, j, k);
		ItemStack equippedItem = entityplayer.getCurrentEquippedItem();

		if ((equippedItem != null) && ((equippedItem.getItem() instanceof ItemMultitool)))
		{
			return false;
		}

		if ((equippedItem != null) && ((equippedItem.getItem() instanceof ItemCardEmpty)))
		{
			return false;
		}

		if ((equippedItem != null) && ((equippedItem.getItem() instanceof ItemModuleBase)))
		{
			return false;
		}

		if ((equippedItem != null) && ((equippedItem.getItem() instanceof ItemCardPowerLink)))
		{
			return false;
		}

		if ((equippedItem != null) && ((equippedItem.getItem() instanceof ItemCardSecurityLink)))
		{
			return false;
		}

		if ((equippedItem != null) && (equippedItem.itemID == Block.lever.blockID))
		{
			return false;
		}

		if ((tileEntity instanceof TileEntitySecurityStation) && (tileEntity.isActive()))
		{
			if (!SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.CSR))
			{
				return true;
			}
		}

		if (tileEntity instanceof TileEntityControlSystem)
		{
			if (!SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.UCS))
			{
				return true;
			}
		}

		if (!SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB))
		{
			return true;
		}

		if (!world.isRemote)
		{
			entityplayer.openGui(ModularForceFieldSystem.instance, 0, world, i, j, k);
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
	{
		TileEntity tile = world.getBlockTileEntity(i, j, k);
		
		if (tile instanceof TileEntityMFFS)
		{
			TileEntityMFFS tileEntity = (TileEntityMFFS) tile;
			int side = MathHelper.floor_double(entityliving.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
			int height = Math.round(entityliving.rotationPitch);

			if (height >= 65)
			{
				tileEntity.setDirection(ForgeDirection.getOrientation(1));
			}
			else if (height <= -65)
			{
				tileEntity.setDirection(ForgeDirection.getOrientation(0));
			}
			else if (side == 0)
			{
				tileEntity.setDirection(ForgeDirection.getOrientation(2));
			}
			else if (side == 1)
			{
				tileEntity.setDirection(ForgeDirection.getOrientation(5));
			}
			else if (side == 2)
			{
				tileEntity.setDirection(ForgeDirection.getOrientation(3));
			}
			else if (side == 3)
			{
				tileEntity.setDirection(ForgeDirection.getOrientation(4));
			}
		}
	}

	@Override
	public int getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int l)
	{
		int typ = 0;

		TileEntity t = iBlockAccess.getBlockTileEntity(x, y, z);

		if (t instanceof TileEntityMFFS)
		{
			TileEntityMFFS tileEntity = (TileEntityMFFS) t;

			int facing = (tileEntity instanceof TileEntityMFFS) ? ((TileEntityMFFS) tileEntity).getDirection().ordinal() : 1;

			ForgeDirection blockfacing = ForgeDirection.getOrientation(l);
			ForgeDirection TileEntityfacing = ForgeDirection.getOrientation(facing);

			if ((tileEntity instanceof TileEntityProjector))
			{
				typ = ((TileEntityProjector) tileEntity).getProjectorType();
			}

			if (tileEntity.isActive())
			{
				if (blockfacing.equals(TileEntityfacing))
				{
					return this.blockIndexInTexture + typ * 16 + 3 + 1;
				}
				if (blockfacing.equals(TileEntityfacing.getOpposite()))
				{
					return this.blockIndexInTexture + typ * 16 + 3 + 2;
				}
				return this.blockIndexInTexture + typ * 16 + 3;
			}

			if (blockfacing.equals(TileEntityfacing))
			{
				return this.blockIndexInTexture + typ * 16 + 1;
			}
			if (blockfacing.equals(TileEntityfacing.getOpposite()))
			{
				return this.blockIndexInTexture + typ * 16 + 2;
			}
		}

		return this.blockIndexInTexture + typ * 16;
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int i, int j, int k, double d, double d1, double d2)
	{
		if ((world.getBlockTileEntity(i, j, k) instanceof TileEntityMFFS))
		{
			TileEntity tileentity = world.getBlockTileEntity(i, j, k);
			if (((TileEntityMFFS) tileentity).isActive())
			{
				return 999.0F;
			}
			return 100.0F;
		}

		return 100.0F;
	}
}