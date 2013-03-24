package mffs.common.tileentity;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import mffs.api.ForceFieldType;
import mffs.api.IProjector;
import mffs.api.IProjectorMode;
import mffs.common.MFFSConfiguration;
import mffs.common.ZhuYao;
import mffs.common.card.ItKa;
import mffs.common.card.ItKaWuXian;
import mffs.common.module.IModule;
import mffs.common.module.fangyingji.IInteriorCheck;
import mffs.common.module.fangyingji.ItemModuleFusion;
import mffs.common.module.fangyingji.ItemModuleJammer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TFangYingJi extends TModuleAcceptor implements IProjector
{
	private static final int MODULE_SLOT_ID = 5;

	protected Stack fieldQueue = new Stack();

	/**
	 * A set containing all positions of all force field blocks.
	 */
	protected Set<Vector3> forceFields = new HashSet();

	protected Set<Vector3> calculatedField = new HashSet();
	protected Set<Vector3> fieldInterior = new HashSet();

	private short forcefieldblock_meta = ((short) ForceFieldType.Default.ordinal());

	private String forceFieldTextureIDs = "-76/-76/-76/-76/-76/-76";
	private String forceFieldTextureFile = "/terrain.png";

	private int[] focusmatrix = { 0, 0, 0, 0 };
	private int forceFieldCamoblockID;
	private int forceFieldCamoblockMeta;
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
			if (this.isPoweredByRedstone())
			{
				if (!this.isActive())
				{
					this.calculateForceField();
					this.setActive(true);
				}
			}
			else
			{
				if (this.isActive())
				{
					this.setActive(false);
				}
			}

			if (this.isActive() && this.getMode() != null && this.requestFortron(this.getFortronCost(), false) > 0)
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

			/**
			 * Packet Update for Client only when GUI is open.
			 */
			if (this.ticks % 4 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(super.getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
			}
		}
	}

	@Override
	public List getPacketUpdate()
	{
		List objects = new LinkedList();
		objects.addAll(super.getPacketUpdate());
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		objects.add(nbt);
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		final boolean prevActivate = this.isActive();

		super.onReceivePacket(packetID, dataStream);

		if (packetID == 1 && this.worldObj.isRemote)
		{
			if (prevActivate != this.isActive())
			{
				this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
			}

			try
			{
				this.readFromNBT(PacketManager.readNBTTagCompound(dataStream));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemStack)
	{
		switch (slotID)
		{
			case 0:
				return itemStack.getItem() instanceof ItKa || itemStack.getItem() instanceof ItKaWuXian;
			case 5:
				return itemStack.getItem() instanceof IProjectorMode;
			default:
				return itemStack.getItem() instanceof IModule;
		}
	}

	public int getForceFieldCamoblockMeta()
	{
		return this.forceFieldCamoblockMeta;
	}

	public void setForceFieldCamoblockMeta(int forcefieldCamoblockmeta)
	{
		this.forceFieldCamoblockMeta = forcefieldCamoblockmeta;
	}

	public int getForceFieldCamoblockID()
	{
		return this.forceFieldCamoblockID;
	}

	public void setForceFieldCamoblockID(int forcefieldCamoblockid)
	{
		this.forceFieldCamoblockID = forcefieldCamoblockid;
	}

	public String getForceFieldTextureFile()
	{
		return this.forceFieldTextureFile;
	}

	public void setForceFieldTextureFile(String forceFieldTexturfile)
	{
		this.forceFieldTextureFile = forceFieldTexturfile;
	}

	public String getForceFieldTextureID()
	{
		return this.forceFieldTextureIDs;
	}

	public void setForceFieldTextureID(String forceFieldTextureIDs)
	{
		this.forceFieldTextureIDs = forceFieldTextureIDs;
	}

	public int getBlockCounter()
	{
		return this.blockCount;
	}

	public int getforcefieldblock_meta()
	{
		return this.forcefieldblock_meta;
	}

	public void setforcefieldblock_meta(int ffmeta)
	{
		this.forcefieldblock_meta = ((short) ffmeta);
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

		if (!this.isActive())
		{
			this.destroyField();
		}
	}

	private void updateForceFieldTexture()
	{
		if ((isActive()) && (this.getModuleCount(ZhuYao.itemModuleCamouflage) > 0))
		{
			for (Vector3 vector : this.calculatedField)
			{
				if (this.worldObj.getChunkFromBlockCoords(vector.intX(), vector.intZ()).isChunkLoaded)
				{
					TileEntity tileEntity = this.worldObj.getBlockTileEntity(vector.intX(), vector.intY(), vector.intZ());

					if ((tileEntity != null) && ((tileEntity instanceof TLiChang)))
					{
						((TLiChang) tileEntity).updateTexture();
					}
				}
			}
		}
	}

	private boolean calculateForceField()
	{
		if (!this.worldObj.isRemote)
		{
			this.calculatedField.clear();
			this.fieldInterior.clear();

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
						if (forceFieldDefine(fieldPoint))
						{
							this.calculatedField.add(fieldPoint);
						}
					}
				}

				for (Vector3 vector : blockInterior)
				{
					if (vector.intY() + this.yCoord < this.worldObj.getHeight())
					{
						Vector3 fieldPoint = Vector3.add(new Vector3(this), vector);

						if (calculateBlock(fieldPoint))
						{
							this.fieldInterior.add(fieldPoint);
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

	public boolean forceFieldDefine(Vector3 vector)
	{
		for (IModule opt : getModules())
		{
			if (((opt instanceof ItemModuleJammer)) && (((ItemModuleJammer) opt).checkJammerinfluence(vector, this.worldObj, this)))
			{
				return false;
			}

			if (((opt instanceof ItemModuleFusion)) && (((ItemModuleFusion) opt).checkFieldFusioninfluence(vector, this.worldObj, this)))
			{
				return true;
			}

		}

		this.fieldQueue.push(Integer.valueOf(vector.hashCode()));

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
			this.blockCount = 0;
			for (Vector3 vector : this.calculatedField)
			{
				if (this.blockCount >= MFFSConfiguration.maxForceFieldPerTick)
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
						}

						this.forceFields.add(vector);
						this.blockCount++;
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

	public int fortronRequest()
	{
		if (!this.calculatedField.isEmpty())
		{
			return this.calculatedField.size() * MFFSConfiguration.forceFieldBlockCost;
		}

		return 0;
	}

	@Override
	public int getSizeInventory()
	{
		return 1 + 1 + 2 * 6 + 2;
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
		return this.fieldInterior;
	}

	public Set<Vector3> getFieldQueue()
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
				return new int[] { 10, 11 };
			case DOWN:
				return new int[] { 12, 13 };
			case NORTH:
				return new int[] { 7, 8 };
			case SOUTH:
				return new int[] { 1, 2 };
			case WEST:
				return new int[] { 3, 4 };
			case EAST:
				return new int[] { 5, 6 };
		}
	}

	@Override
	public int[] getModuleSlots()
	{
		return new int[] { 14, 15 };
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