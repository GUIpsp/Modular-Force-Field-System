package mffs.jiqi.t;

import java.util.HashSet;
import java.util.Set;

import mffs.ZhuYao;
import mffs.api.IProjector;
import mffs.api.modules.IModule;
import mffs.api.modules.IProjectorMode;
import mffs.it.ka.ItKa;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TFangYingJi extends TModuleAcceptor implements IProjector
{
	private static final int MODULE_SLOT_ID = 2;

	/**
	 * A set containing all positions of all force field blocks.
	 */
	protected final Set<Vector3> forceFields = new HashSet();

	protected final Set<Vector3> calculatedField = new HashSet<Vector3>();
	protected final Set<Vector3> calculatedFieldInterior = new HashSet<Vector3>();

	private boolean isCalculated = false;
	private int blockCount = 0;

	public int animation = 0;

	public TFangYingJi()
	{
		this.fortronTank.setCapacity(20 * LiquidContainerRegistry.BUCKET_VOLUME);
		this.startModuleIndex = 1;
	}

	@Override
	public void initiate()
	{
		super.initiate();
		this.calculateForceField();
		this.destroyField();

	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.isActive() && this.getMode() != null && this.requestFortron(this.getFortronCost(), false) >= this.getFortronCost())
		{
			this.requestFortron(this.getFortronCost(), true);

			if (!this.worldObj.isRemote)
			{
				if (this.ticks % 10 == 0)
				{
					if (!this.isCalculated)
					{
						this.calculateForceField();
					}

					this.projectField();
				}
			}
			else
			{
				if (this.isActive())
				{
					this.animation++;
				}
			}

			if (this.ticks % (2 * 20) == 0)
			{
				this.worldObj.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "mffs.field", 1f, (1 - this.worldObj.rand.nextFloat() * 0.1f));
			}
		}
		else if (!this.worldObj.isRemote)
		{
			this.destroyField();
		}
	}

	@Override
	public int getFortronCost()
	{
		float cost = 2;

		for (ItemStack itemStack : this.getModuleStacks())
		{
			if (itemStack != null)
			{
				cost += itemStack.stackSize * ((IModule) itemStack.getItem()).getFortronCost(this.getCalculatedField().size());
			}
		}

		return Math.round(cost);
	}

	@Override
	public void onInventoryChanged()
	{
		final boolean active = this.isActive();
		this.setActive(false);
		this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);

		if (active)
		{
			this.setActive(true);
		}
	}

	@Override
	public void setActive(boolean flag)
	{
		super.setActive(flag);

		if (this.isActive())
		{
			this.calculateForceField();
		}
		else
		{
			this.destroyField();
		}
	}

	private void calculateForceField()
	{
		if (!this.worldObj.isRemote)
		{
			this.calculatedField.clear();
			this.calculatedFieldInterior.clear();

			if (this.getMode() != null)
			{
				Set<Vector3> blockDef = new HashSet();
				Set<Vector3> blockInter = new HashSet();

				this.getMode().calculateField(this, blockDef, blockInter);

				for (Vector3 vector : blockDef)
				{
					Vector3 fieldPoint = Vector3.add(new Vector3(this), vector);

					if (fieldPoint.intY() < this.worldObj.getHeight())
					{
						boolean canCalculate = true;

						for (IModule module : this.getModules(this.getModuleSlots()))
						{
							if (!module.onCalculate(this, fieldPoint))
							{
								canCalculate = false;
								break;
							}
						}

						if (canCalculate)
						{
							this.calculatedField.add(fieldPoint);
						}
					}
				}

				for (Vector3 vector : blockInter)
				{
					Vector3 fieldPoint = Vector3.add(new Vector3(this), vector);

					if (fieldPoint.intY() < this.worldObj.getHeight())
					{
						this.calculatedFieldInterior.add(fieldPoint);
					}
				}

				this.isCalculated = true;
			}
		}
	}

	/**
	 * Projects a force field based on the calculations made.
	 */
	@Override
	public void projectField()
	{
		if (!this.worldObj.isRemote && this.isCalculated)
		{
			int constructionCount = 0;
			this.forceFields.clear();

			for (IModule module : this.getModules(this.getModuleSlots()))
			{
				if (module.onProject(this))
				{
					return;
				}
			}

			try
			{
				for (Vector3 vector : this.calculatedField)
				{
					if (constructionCount >= this.getConstructionSpeed())
					{
						break;
					}

					Block block = Block.blocksList[vector.getBlockID(this.worldObj)];

					if (this.getModuleCount(ZhuYao.itemModuleDisintegration) > 0 || block == null || block.blockMaterial.isLiquid() || block == Block.snow || block == Block.vine || block == Block.tallGrass || block == Block.deadBush || block.isBlockReplaceable(this.worldObj, vector.intX(), vector.intY(), vector.intZ()) || block == ZhuYao.bLiQiang)
					{
						boolean canProject = true;

						for (IModule module : this.getModules(this.getModuleSlots()))
						{
							if (!module.canProject(this, vector.clone()))
							{
								canProject = false;
								break;
							}
						}

						if (canProject)
						{
							if (block != ZhuYao.bLiQiang)
							{
								if (this.worldObj.getChunkFromBlockCoords(vector.intX(), vector.intZ()).isChunkLoaded)
								{
									this.worldObj.setBlock(vector.intX(), vector.intY(), vector.intZ(), ZhuYao.bLiQiang.blockID, 0, 2);

									TileEntity tileEntity = this.worldObj.getBlockTileEntity(vector.intX(), vector.intY(), vector.intZ());

									if (tileEntity instanceof TLiQiang)
									{
										((TLiQiang) tileEntity).setZhuYao(new Vector3(this));
									}

									boolean cancel = false;

									for (IModule module : this.getModules(this.getModuleSlots()))
									{
										if (module.onProject(this, vector.clone()))
										{
											cancel = true;
										}
									}

									this.forceFields.add(vector);
									constructionCount++;

									if (cancel)
									{
										break;
									}
								}

							}
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroyField()
	{
		if (!this.worldObj.isRemote)
		{
			try
			{
				for (Vector3 vector : this.calculatedField)
				{
					Block block = Block.blocksList[vector.getBlockID(this.worldObj)];

					if (block == ZhuYao.bLiQiang)
					{
						this.worldObj.setBlock(vector.intX(), vector.intY(), vector.intZ(), 0, 0, 3);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.calculatedField.clear();
		this.calculatedFieldInterior.clear();
		this.isCalculated = false;
	}

	@Override
	public void invalidate()
	{
		this.destroyField();
		super.invalidate();
	}

	@Override
	public int getConstructionSpeed()
	{
		return 100 + 20 * this.getModuleCount(ZhuYao.itMSuDu, this.getModuleSlots());
	}

	@Override
	public int getSizeInventory()
	{
		return 3 + 18;
	}

	@Override
	public IProjectorMode getMode()
	{
		if (this.getModeStack() != null)
		{
			return (IProjectorMode) this.getModeStack().getItem();
		}

		return null;
	}

	@Override
	public ItemStack getModeStack()
	{
		ItemStack itemStack = this.getStackInSlot(MODULE_SLOT_ID);

		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof IProjectorMode)
			{
				return itemStack;
			}
		}

		return null;
	}

	@Override
	public Set<Vector3> getInteriorPoints()
	{
		return this.calculatedFieldInterior;
	}

	@Override
	public Set<Vector3> getCalculatedField()
	{
		return this.calculatedField;
	}

	@Override
	public int getSidedModuleCount(IModule module, ForgeDirection... direction)
	{
		int count = 0;

		if (direction != null && direction.length > 0)
		{
			for (ForgeDirection checkDir : direction)
			{
				count += this.getModuleCount(module, this.getSlotsBasedOnDirection(checkDir));
			}
		}
		else
		{
			for (int i = 0; i < 6; i++)
			{
				ForgeDirection checkDir = ForgeDirection.getOrientation(i);
				count += this.getModuleCount(module, this.getSlotsBasedOnDirection(checkDir));
			}
		}

		return count;
	}

	@Override
	public int[] getSlotsBasedOnDirection(ForgeDirection direction)
	{
		switch (direction)
		{
			default:
				return new int[] {};
			case UP:
				return new int[] { 3, 11 };
			case DOWN:
				return new int[] { 6, 14 };
			case NORTH:
				return new int[] { 8, 10 };
			case SOUTH:
				return new int[] { 7, 9 };
			case WEST:
				return new int[] { 12, 13 };
			case EAST:
				return new int[] { 4, 5 };
		}
	}

	@Override
	public int[] getModuleSlots()
	{
		return new int[] { 15, 16, 17, 18, 19, 20 };
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemStack)
	{
		if (slotID == 0 || slotID == 1)
		{
			return itemStack.getItem() instanceof ItKa;
		}
		else if (slotID == MODULE_SLOT_ID)
		{
			return itemStack.getItem() instanceof IProjectorMode;
		}
		else if (slotID >= 15)
		{
			return true;
		}

		return itemStack.getItem() instanceof IModule;
	}

	@Override
	public Set<ItemStack> getCards()
	{
		Set<ItemStack> cards = new HashSet<ItemStack>();
		cards.add(super.getCard());
		cards.add(this.getStackInSlot(1));
		return cards;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1);
	}

	public long getTicks()
	{
		return this.ticks;
	}
}