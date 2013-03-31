package mffs.jiqi.t;

import java.util.HashSet;
import java.util.Set;

import mffs.ZhuYao;
import mffs.api.IProjector;
import mffs.api.modules.IModule;
import mffs.api.modules.IProjectorMode;
import mffs.it.ka.ItKa;
import mffs.it.muo.fangyingji.IInteriorCheck;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneReceptor;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TFangYingJi extends TModuleAcceptor implements IProjector
{
	private static final int MODULE_SLOT_ID = 2;

	/**
	 * A set containing all positions of all force field blocks.
	 */
	protected Set<Vector3> forceFields = new HashSet();

	protected Set<Vector3> calculatedField = new HashSet();
	protected Set<Vector3> calculatedFieldInterior = new HashSet();

	private int blockCount = 0;

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

		if (!this.worldObj.isRemote)
		{
			if (this.isActive() && this.getMode() != null && this.requestFortron(this.getFortronCost(), false) >= this.getFortronCost())
			{
				this.requestFortron(this.getFortronCost(), true);

				if (this.ticks % 10 == 0)
				{
					this.projectField();
				}
			}
			else
			{
				this.destroyField();
			}

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
				cost += itemStack.stackSize * ((IModule) itemStack.getItem()).getFortronCost();
			}
		}

		return Math.round(cost);
	}

	@Override
	public void onInventoryChanged()
	{
		this.setActive(false);
		this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
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

	private boolean calculateForceField()
	{
		if (!this.worldObj.isRemote)
		{
			this.calculatedField.clear();
			this.calculatedFieldInterior.clear();

			if (this.getMode() != null)
			{
				Set<Vector3> blockDef = new HashSet();
				Set<Vector3> blockInterior = new HashSet();

				this.getMode().calculateField(this, blockDef, blockInterior);

				for (Vector3 vector : blockDef)
				{
					Vector3 fieldPoint = Vector3.add(new Vector3(this), vector);

					if (fieldPoint.intY() < this.worldObj.getHeight())
					{
						this.calculatedField.add(fieldPoint);
					}
				}

				for (Vector3 vector : blockInterior)
				{
					if (vector.intY() + this.yCoord < this.worldObj.getHeight())
					{
						Vector3 fieldPoint = Vector3.add(new Vector3(this), vector);

						if (calculateBlock(fieldPoint))
						{
							this.calculatedFieldInterior.add(fieldPoint);
						}
						else
						{
							return false;
						}
					}

				}

				return true;
			}
		}

		return false;
	}

	public boolean calculateBlock(Vector3 pnt)
	{
		for (IModule opt : this.getModules())
		{
			if (opt instanceof IInteriorCheck)
			{
				((IInteriorCheck) opt).checkInteriorBlock(pnt, this.worldObj, this);
			}
		}
		return true;
	}

	/**
	 * Projects a force field based on the calculations made.
	 */
	@Override
	public void projectField()
	{
		if (!this.worldObj.isRemote)
		{
			int constructionCount = 0;

			for (Vector3 vector : this.calculatedField)
			{
				if (constructionCount >= this.getConstructionSpeed())
				{
					break;
				}

				Block block = Block.blocksList[vector.getBlockID(this.worldObj)];

				if (block == null || block.blockMaterial.isLiquid() || block == Block.snow || block == Block.vine || block == Block.tallGrass || block == Block.deadBush || block.isBlockReplaceable(this.worldObj, vector.intX(), vector.intY(), vector.intZ()) || block == ZhuYao.blockForceField)
				{
					if (block != ZhuYao.blockForceField)
					{
						if (this.worldObj.getChunkFromBlockCoords(vector.intX(), vector.intZ()).isChunkLoaded)
						{
							this.worldObj.setBlock(vector.intX(), vector.intY(), vector.intZ(), ZhuYao.blockForceField.blockID, 0, 3);

							TileEntity tileEntity = this.worldObj.getBlockTileEntity(vector.intX(), vector.intY(), vector.intZ());

							if (tileEntity instanceof TLiQiang)
							{
								((TLiQiang) tileEntity).setZhuYao(new Vector3(this));
							}

							for (IModule module : this.getModules(this.getModuleSlots()))
							{
								module.onProject(this, vector);
							}

							constructionCount++;
						}

						this.forceFields.add(vector);
					}
				}
			}
		}
	}

	@Override
	public void destroyField()
	{
		if (!this.worldObj.isRemote)
		{
			for (Vector3 vector : this.calculatedField)
			{
				Block block = Block.blocksList[vector.getBlockID(this.worldObj)];

				if (block == ZhuYao.blockForceField)
				{
					this.worldObj.setBlock(vector.intX(), vector.intY(), vector.intZ(), 0, 0, 3);
				}
			}
		}
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