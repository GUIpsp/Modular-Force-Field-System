package mffs.common.block;

import mffs.common.MFFSCreativeTab;
import mffs.common.ModularForceFieldSystem;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.card.ItemCardEmpty;
import mffs.common.card.ItemCardPowerLink;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.modules.ItemModuleBase;
import mffs.common.multitool.ItemMultitool;
import mffs.common.tileentity.TileEntitySecurityStation;
import mffs.common.tileentity.TileEntityControlSystem;
import mffs.common.tileentity.TileEntityMFFS;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;

public abstract class BlockMFFS extends BlockMachine
{
	public BlockMFFS(int id, String name)
	{
		super(ModularForceFieldSystem.CONFIGURATION.getBlock(name, id).getInt(id), UniversalElectricity.machine);
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

		if ((entityplayer.getCurrentEquippedItem() != null) && ((entityplayer.getCurrentEquippedItem().getItem() instanceof ItemMultitool)))
		{
			return false;
		}

		if ((entityplayer.getCurrentEquippedItem() != null) && ((entityplayer.getCurrentEquippedItem().getItem() instanceof ItemCardEmpty)))
		{
			return false;
		}

		if ((entityplayer.getCurrentEquippedItem() != null) && ((entityplayer.getCurrentEquippedItem().getItem() instanceof ItemModuleBase)))
		{
			return false;
		}

		if ((entityplayer.getCurrentEquippedItem() != null) && ((entityplayer.getCurrentEquippedItem().getItem() instanceof ItemCardPowerLink)))
		{
			return false;
		}

		if ((entityplayer.getCurrentEquippedItem() != null) && ((entityplayer.getCurrentEquippedItem().getItem() instanceof ItemCardSecurityLink)))
		{
			return false;
		}

		if ((entityplayer.getCurrentEquippedItem() != null) && (entityplayer.getCurrentEquippedItem().itemID == Block.lever.blockID))
		{
			return false;
		}

		if (((tileEntity instanceof TileEntitySecurityStation)) && (tileEntity.isActive()))
		{
			if (!SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.CSR))
			{
				return true;
			}
		}

		if ((tileEntity instanceof TileEntityControlSystem))
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
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
		if ((tileEntity instanceof TileEntityMFFS))
		{
			int l = MathHelper.floor_double(entityliving.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;

			int i1 = Math.round(entityliving.rotationPitch);
			if (i1 >= 65)
				((TileEntityMFFS) tileEntity).setSide(1);
			else if (i1 <= -65)
				((TileEntityMFFS) tileEntity).setSide(0);
			else if (l == 0)
				((TileEntityMFFS) tileEntity).setSide(2);
			else if (l == 1)
				((TileEntityMFFS) tileEntity).setSide(5);
			else if (l == 2)
				((TileEntityMFFS) tileEntity).setSide(3);
			else if (l == 3)
				((TileEntityMFFS) tileEntity).setSide(4);
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

			int facing = (tileEntity instanceof TileEntityMFFS) ? ((TileEntityMFFS) tileEntity).getSide() : 1;

			ForgeDirection blockfacing = ForgeDirection.getOrientation(l);
			ForgeDirection TileEntityfacing = ForgeDirection.getOrientation(facing);

			if ((tileEntity instanceof TileEntityProjector))
			{
				typ = ((TileEntityProjector) tileEntity).getProjektor_Typ();
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