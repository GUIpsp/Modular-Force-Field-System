package mffs.jiqi;

import java.util.List;
import java.util.Random;

import mffs.MFFSConfiguration;
import mffs.api.IForceFieldBlock;
import mffs.api.IProjector;
import mffs.api.ISecurityCenter;
import mffs.api.SecurityPermission;
import mffs.api.modules.IModule;
import mffs.jiqi.t.TLiQiang;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.prefab.potion.CustomPotionEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BLiQiang extends BBase implements IForceFieldBlock
{
	public BLiQiang(int id)
	{
		super(id, "forceField", Material.glass);
		this.setBlockUnbreakable();
		this.setResistance(999.0F);
		this.setCreativeTab(null);
		this.setTickRandomly(true);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	protected boolean canSilkHarvest()
	{
		return false;
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TLiQiang)
		{
			if (((TLiQiang) tileEntity).getZhuYao() != null)
			{
				for (ItemStack moduleStack : ((TLiQiang) tileEntity).getZhuYao().getModuleStacks(((TLiQiang) tileEntity).getZhuYao().getModuleSlots()))
				{
					if (((IModule) moduleStack.getItem()).onCollideWithForceField(world, x, y, z, entityPlayer, moduleStack))
					{
						return;
					}
				}
			}
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TLiQiang)
		{
			if (((TLiQiang) tileEntity).getZhuYao() != null)
			{
				ISecurityCenter securityCenter = ((TLiQiang) tileEntity).getZhuYao().getSecurityCenter();

				List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 0.9, z + 1));

				for (EntityPlayer entityPlayer : entities)
				{
					if (entityPlayer != null)
					{
						if (entityPlayer.isSneaking())
						{
							if (entityPlayer.capabilities.isCreativeMode)
							{
								return null;
							}
							else if (securityCenter != null)
							{
								if (securityCenter.isAccessGranted(entityPlayer.username, SecurityPermission.FORCE_FIELD_WARP))
								{
									return null;
								}
							}
						}
					}
				}
			}
		}

		float f = 0.0625F;
		return AxisAlignedBB.getBoundingBox(x + f, y + f, z + f, x + 1 - f, y + 1 - f, z + 1 - f);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return AxisAlignedBB.getBoundingBox(x, y, z, x, y, z);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TLiQiang)
		{
			if (((TLiQiang) tileEntity).getZhuYao() != null)
			{
				for (ItemStack moduleStack : ((TLiQiang) tileEntity).getZhuYao().getModuleStacks(((TLiQiang) tileEntity).getZhuYao().getModuleSlots()))
				{
					if (((IModule) moduleStack.getItem()).onCollideWithForceField(world, x, y, z, entity, moduleStack))
					{
						return;
					}
				}
			}
		}

		if (entity instanceof EntityLiving)
		{
			((EntityLiving) entity).addPotionEffect(new CustomPotionEffect(Potion.confusion.id, 1, 3));
			((EntityLiving) entity).addPotionEffect(new CustomPotionEffect(Potion.moveSlowdown.id, 1, 3));
		}
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		TileEntity tileEntity = iBlockAccess.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TLiQiang)
		{
			int haoMa = ((TLiQiang) tileEntity).getHaoMa();

			Block block = Block.blocksList[haoMa];

			if (block != null)
			{
				int mD = ((TLiQiang) tileEntity).getMD();
				return block.getBlockTextureFromSideAndMetadata(side, mD);
			}
		}
		return this.getBlockTextureFromSideAndMetadata(side, iBlockAccess.getBlockMetadata(x, y, z));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		int i1 = par1IBlockAccess.getBlockId(par2, par3, par4);
		return i1 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double d, double d1, double d2)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TLiQiang();
	}

	@Override
	public void weakenForceField(World world, int x, int y, int z)
	{
		if (MFFSConfiguration.influencedByOtherMods)
		{
			world.setBlock(x, y, z, 0, 0, 3);
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderBlockPass()
	{
		return 1;
	}

	@Override
	public IProjector getProjector(World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TLiQiang)
		{
			return ((TLiQiang) tileEntity).getZhuYao();
		}

		return null;
	}
}