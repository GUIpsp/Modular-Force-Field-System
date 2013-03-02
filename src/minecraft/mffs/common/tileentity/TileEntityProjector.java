package mffs.common.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.google.common.io.ByteArrayDataInput;

import mffs.api.IProjector;
import mffs.api.IProjectorMode;
import mffs.common.ForceFieldBlockStack;
import mffs.common.FrequencyGridOld;
import mffs.common.MFFSConfiguration;
import mffs.common.ModularForceFieldSystem;
import mffs.common.ProjectorTypes;
import mffs.common.WorldMap;
import mffs.common.block.BlockForceField.ForceFieldType;
import mffs.common.card.ItemCard;
import mffs.common.card.ItemCardPower;
import mffs.common.container.ContainerProjector;
import mffs.common.module.IInteriorCheck;
import mffs.common.module.IModule;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemModuleFusion;
import mffs.common.module.ItemModuleJammer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;

public class TileEntityProjector extends TileEntityFortron implements IProjector
{
	/**
	 * The amount of fortron energy to consume per second.
	 */
	public static final int FORTRON_CONSUMPTION = 1;

	private static final int MODULE_SLOT_ID = 5;

	protected Stack fieldQueue = new Stack();

	/**
	 * A set containinig all positions of all force field blocks.
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
	private int blockCount;
	private int accessType = 0;
	private int linkPower = 0;
	private int switchDelay = 0;

	public TileEntityProjector()
	{
		this.fortronTank.setCapacity(20 * LiquidContainerRegistry.BUCKET_VOLUME);
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

			if (this.isActive() && this.getFortronEnergy() > FORTRON_CONSUMPTION || (this.getStackInSlot(0) != null && this.getStackInSlot(0).itemID == ModularForceFieldSystem.itemCardInfinite.itemID))
			{
				if (this.ticks % 10 == 0)
				{
					this.projectField();
				}

				this.consumeFortron(1, true);
			}
			else
			{
				// this.destroyField();
			}

			/**
			 * Packet Update for Client only when GUI is open.
			 */
			if (this.ticks % 4 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
			}
		}
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		final boolean prevActivate = this.isActive();
		super.onReceivePacket(packetID, dataStream);

		if (prevActivate != this.isActive())
		{
			this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	@Override
	public boolean isItemValid(int slotID, ItemStack itemStack)
	{
		switch (slotID)
		{
			case 0:
				return itemStack.getItem() instanceof ItemCard || itemStack.getItem() instanceof ItemCardPower;
			case 5:
				return itemStack.getItem() instanceof IProjectorMode;
			default:
				return itemStack.getItem() instanceof IModule;
		}
	}

	public int getAccessType()
	{
		return this.accessType;
	}

	public void setAccessType(int accesstyp)
	{
		this.accessType = accesstyp;
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

	public int getLinkPower()
	{
		return this.linkPower;
	}

	public void setLinkPower(int linkPower)
	{
		this.linkPower = linkPower;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);

		this.accessType = nbttagcompound.getInteger("accessType");
		this.forcefieldblock_meta = nbttagcompound.getShort("forceFieldblockMeta");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("accessType", this.accessType);
		nbttagcompound.setShort("forceFieldblockMeta", this.forcefieldblock_meta);
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

		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	private void updateForceFieldTexture()
	{
		if ((isActive()) && (this.getModuleCount(ModularForceFieldSystem.itemModuleCamouflage) > 0))
		{
			for (Vector3 vector : this.calculatedField)
			{
				if (this.worldObj.getChunkFromBlockCoords(vector.intX(), vector.intZ()).isChunkLoaded)
				{
					TileEntity tileEntity = this.worldObj.getBlockTileEntity(vector.intX(), vector.intY(), vector.intZ());

					if ((tileEntity != null) && ((tileEntity instanceof TileEntityForceField)))
					{
						((TileEntityForceField) tileEntity).updateTexture();
					}
				}
			}
		}
	}

	private boolean calculateForceField()
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

		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(this.worldObj).getorcreateFFStackMap(vector.intX(), vector.intY(), vector.intZ(), this.worldObj);

		if (!ffworldmap.isEmpty())
		{
			if (ffworldmap.getProjectorID() != getDeviceID())
			{
				ffworldmap.removebyProjector(getDeviceID());
				// ffworldmap.add(getPowerSourceID(), getDeviceID(), getforcefieldblock_meta());
			}
		}
		else
		{
			// ffworldmap.add(getPowerSourceID(), getDeviceID(), getforcefieldblock_meta());
			ffworldmap.setSync(false);
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
		this.blockCount = 0;
		for (Vector3 vector : this.calculatedField)
		{
			if (this.blockCount >= MFFSConfiguration.maxForceFieldPerTick)
			{
				break;
			}

			Block block = Block.blocksList[vector.getBlockID(this.worldObj)];

			if (block == null || block.blockMaterial.isLiquid() || block == Block.snow || block == Block.vine || block == Block.tallGrass || block == Block.deadBush || block.isBlockReplaceable(this.worldObj, vector.intX(), vector.intY(), vector.intZ()) || block == ModularForceFieldSystem.blockForceField)
			{
				if (block != ModularForceFieldSystem.blockForceField)
				{
					if (this.worldObj.getChunkFromBlockCoords(vector.intX(), vector.intZ()).isChunkLoaded)
					{
						this.worldObj.setBlockAndMetadataWithNotify(vector.intX(), vector.intY(), vector.intZ(), ModularForceFieldSystem.blockForceField.blockID, 0);
					}

					this.forceFields.add(vector);
					this.blockCount++;
				}
			}
		}
	}

	@Override
	public void destroyField()
	{
		for (Vector3 vector : this.calculatedField)
		{
			Block block = Block.blocksList[vector.getBlockID(this.worldObj)];

			if (block == ModularForceFieldSystem.blockForceField)
			{
				vector.setBlockWithNotify(this.worldObj, 0);
			}
		}
	}

	@Override
	public void invalidate()
	{
		this.destroyField();
		super.invalidate();
	}

	public int forcePowerNeed(int factor)
	{
		if (!this.calculatedField.isEmpty())
		{
			return this.calculatedField.size() * MFFSConfiguration.forcefieldblockcost;
		}

		int forcepower = 0;
		int blocks = 0;

		int tmplength = 1;

		if (countItemsInSlot(IProjector.Slots.Strength) != 0)
		{
			tmplength = countItemsInSlot(IProjector.Slots.Strength);
		}

		switch (ProjectorTypes.typeFromItem(this.getMode()).ordinal())
		{
			case 1:
				blocks = (countItemsInSlot(IProjector.Slots.FocusDown) + countItemsInSlot(IProjector.Slots.FocusLeft) + countItemsInSlot(IProjector.Slots.FocusRight) + countItemsInSlot(IProjector.Slots.FocusUp) + 1) * tmplength;

				break;
			case 2:
				blocks = (countItemsInSlot(IProjector.Slots.FocusDown) + countItemsInSlot(IProjector.Slots.FocusUp) + 1) * (countItemsInSlot(IProjector.Slots.FocusLeft) + countItemsInSlot(IProjector.Slots.FocusRight) + 1);

				break;
			case 3:
				blocks = ((countItemsInSlot(IProjector.Slots.Distance) + 2 + countItemsInSlot(IProjector.Slots.Distance) + 2) * 4 + 4) * (countItemsInSlot(IProjector.Slots.Strength) + 1);

				break;
			case 4:
			case 5:
				blocks = countItemsInSlot(IProjector.Slots.Distance) * countItemsInSlot(IProjector.Slots.Distance) * 6;
		}

		forcepower = blocks * MFFSConfiguration.forcefieldblockcost;
		if (factor != 1)
		{
			forcepower = forcepower * MFFSConfiguration.forcefieldblockcreatemodifier + forcepower * 5;
		}

		return forcepower;
	}

	@Override
	public int getSizeInventory()
	{
		return 1 + 3 * 3 + 2;
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
	public ItemStack getModule(IModule module)
	{
		ItemStack returnStack = new ItemStack((Item) module, 0);

		for (IModule comparedModule : getModules())
		{
			if (comparedModule == module)
			{
				// TODO:This is wrong.
				returnStack.stackSize++;
			}
		}

		return returnStack;
	}

	@Override
	public int getModuleCount(IModule module, int... slots)
	{
		int count = 0;

		if (slots != null && slots.length > 0)
		{
			for (int slotID : slots)
			{
				if (this.getStackInSlot(slotID) != null)
				{
					if (this.getStackInSlot(slotID).getItem() == module)
					{
						count += this.getStackInSlot(slotID).stackSize;
					}
				}
			}
		}
		else
		{
			for (ItemStack itemStack : getModuleStacks())
			{
				if (itemStack.getItem() == module)
				{
					count += itemStack.stackSize;
				}
			}
		}

		return count;
	}

	@Override
	public List<ItemStack> getModuleStacks()
	{
		List<ItemStack> modules = new ArrayList();

		for (int slotID = 1; slotID <= 9; slotID++)
		{
			ItemStack itemStack = this.getStackInSlot(slotID);

			if (itemStack != null)
			{
				if (itemStack.getItem() instanceof ItemModule)
				{
					modules.add(itemStack);
				}
			}
		}

		return modules;
	}

	@Override
	public List<IModule> getModules()
	{
		List<IModule> modules = new ArrayList();

		for (int slotID = 1; slotID < 9; slotID++)
		{
			ItemStack itemStack = this.getStackInSlot(slotID);

			if (itemStack != null)
			{
				if (itemStack.getItem() instanceof ItemModule)
				{
					modules.add((ItemModule) itemStack.getItem());
				}
			}
		}

		return modules;
	}

	@Override
	public int[] getSlotsBasedOnDirection(ForgeDirection direction)
	{
		switch (direction)
		{
			default:
				return null;
			case UP:
				return new int[] { 10 };
			case DOWN:
				return new int[] { 11 };
			case NORTH:
				return new int[] { 8 };
			case SOUTH:
				return new int[] { 2 };
			case WEST:
				return new int[] { 4 };
			case EAST:
				return new int[] { 6 };
		}
	}
}