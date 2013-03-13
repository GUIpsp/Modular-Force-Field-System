package mffs.common.block;

import java.util.List;
import java.util.Random;

import mffs.api.IForceFieldBlock;
import mffs.api.PointXYZ;
import mffs.client.renderer.RenderForceField;
import mffs.common.ForceFieldBlockStack;
import mffs.common.FrequencyGridOld;
import mffs.common.MFFSConfiguration;
import mffs.common.ModularForceFieldSystem;
import mffs.common.WorldMap;
import mffs.common.tileentity.TileEntityForceField;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockForceField extends BlockContainer implements IForceFieldBlock
{
	public enum ForceFieldType
	{
		Default(1), Camouflage(2), Zapper(3), Area(1), Containment(1);

		int cost;

		private ForceFieldType(int cost)
		{
			this.cost = cost;
		}
	}

	public BlockForceField(int id)
	{
		super(id, Material.glass);
		this.setBlockUnbreakable();
		this.setResistance(999.0F);
		this.setTickRandomly(true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType()
	{
		return RenderForceField.RENDER_ID;
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
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockid)
	{
		if (blockid != ModularForceFieldSystem.blockForceField.blockID)
		{
			for (int x1 = -1; x1 <= 1; x1++)
			{
				for (int y1 = -1; y1 <= 1; y1++)
				{
					for (int z1 = -1; z1 <= 1; z1++)
					{
						if (world.getBlockId(x + x1, y + y1, z + z1) != ModularForceFieldSystem.blockForceField.blockID)
						{
							if (world.getBlockId(x + x1, y + y1, z + z1) == 0)
							{
								breakBlock(world, x + x1, y + y1, z + z1, 0, 0);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int a, int b)
	{
		/**
		 * TODO: Checks the Projector to see if breaking this is legit.
		 */
		super.breakBlock(world, x, y, z, a, b);
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer)
	{
		/**
		 * TODO: Check if shock mode is on, if so, hurt entity
		 */
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		/**
		 * Allow creative players who are holding shift to go through the force field. TODO: Allow
		 * security bypassing.
		 */
		List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 0.9, z + 1));

		for (EntityPlayer entityPlayer : entities)
		{
			if (entityPlayer != null)
			{
				if (entityPlayer.capabilities.isCreativeMode && entityPlayer.isSneaking())
				{
					return null;
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
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
	{
		/**
		 * TODO: Check if shock mode is on, if so, hurt entity
		 * entity.attackEntityFrom(ModularForceFieldSystem.fieldShock, 10);
		 */
		if (entity instanceof EntityLiving)
		{
			((EntityLiving) entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 20, 3));
		}
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int x, int y, int z, int side)
	{
		int xCord = x;
		int yCord = y;
		int zCord = z;

		switch (side)
		{
			case 0:
				yCord++;
				break;
			case 1:
				yCord--;
				break;
			case 2:
				zCord++;
				break;
			case 3:
				zCord--;
				break;
			case 4:
				xCord++;
				break;
			case 5:
				xCord--;
		}

		if ((this.blockID == iblockaccess.getBlockId(x, y, z)) && (iblockaccess.getBlockMetadata(x, y, z) == iblockaccess.getBlockMetadata(xCord, yCord, zCord)))
		{
			return false;
		}

		return super.shouldSideBeRendered(iblockaccess, x, y, z, side);
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double d, double d1, double d2)
	{
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world).getForceFieldStackMap(Integer.valueOf(new PointXYZ(x, y, z, world).hashCode()));

		if ((ffworldmap != null) && (!ffworldmap.isEmpty()))
		{
			TileEntity tileEntity = (TileEntity) FrequencyGridOld.getWorldMap(world).getProjector().get(Integer.valueOf(ffworldmap.getProjectorID()));

			if (((tileEntity instanceof TileEntityProjector)) && (tileEntity != null))
			{
				// ((TileEntityProjector)
				// tileEntity).consumePower(MFFSConfiguration.forcefieldblockcost *
				// MFFSConfiguration.forcefieldblockcreatemodifier, false);
			}

		}

		return 999.0F;
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if ((MFFSConfiguration.advancedParticles) && (world.getBlockMetadata(i, j, k) == ForceFieldType.Zapper.ordinal()))
		{
			double d = i + Math.random() + 0.2D;
			double d1 = j + Math.random() + 0.2D;
			double d2 = k + Math.random() + 0.2D;

			world.spawnParticle("townaura", d, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public TileEntity createTileEntity(World world, int meta)
	{
		if (meta == ForceFieldType.Camouflage.ordinal())
		{
			return new TileEntityForceField();
		}

		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return createTileEntity(world, 0);
	}

	@Override
	public void weakenForceField(World world, int x, int y, int z)
	{
		if (MFFSConfiguration.influencedbyothermods)
		{
			world.setBlockAndMetadataWithNotify(x, y, z, 0, 0, 2);
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return null;
	}
}