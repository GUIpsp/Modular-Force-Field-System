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
import mffs.common.module.ItemOptionFieldFusion;
import mffs.common.module.ItemOptionJammer;
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

	private boolean burnout = false;

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
					this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
				}
			}
			else
			{
				if (this.isActive())
				{
					this.setActive(false);
					this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
				}
			}

			if (this.isActive())
			{
				if (this.getFortronEnergy() > FORTRON_CONSUMPTION || (this.getStackInSlot(0) != null && this.getStackInSlot(0).itemID == ModularForceFieldSystem.itemCardInfinite.itemID))
				{
					if (this.ticks % 10 == 0)
					{
						this.projectField();
					}

					this.consumeFortron(1, true);
				}
				else
				{
					this.destroyField();
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
		// NetworkHandlerServer.updateTileEntityField(this, "ForceFieldCamoblockMeta");
	}

	public int getForceFieldCamoblockID()
	{
		return this.forceFieldCamoblockID;
	}

	public void setForceFieldCamoblockID(int forcefieldCamoblockid)
	{
		this.forceFieldCamoblockID = forcefieldCamoblockid;
		// NetworkHandlerServer.updateTileEntityField(this, "ForceFieldCamoblockID");
	}

	public String getForceFieldTextureFile()
	{
		return this.forceFieldTextureFile;
	}

	public void setForceFieldTextureFile(String forceFieldTexturfile)
	{
		this.forceFieldTextureFile = forceFieldTexturfile;
		// NetworkHandlerServer.updateTileEntityField(this, "ForceFieldTextureFile");
	}

	public String getForceFieldTextureID()
	{
		return this.forceFieldTextureIDs;
	}

	public void setForceFieldTextureID(String forceFieldTextureIDs)
	{
		this.forceFieldTextureIDs = forceFieldTextureIDs;
		// NetworkHandlerServer.updateTileEntityField(this, "forceFieldTextureIDs");
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

	public void projectorBurnout()
	{
		setBurnedOut(true);
	}

	public boolean isBurnout()
	{
		return this.burnout;
	}

	@Override
	public void setBurnedOut(boolean b)
	{
		this.burnout = b;
		// NetworkHandlerServer.updateTileEntityField(this, "burnout");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);

		this.accessType = nbttagcompound.getInteger("accessType");
		this.burnout = nbttagcompound.getBoolean("burnout");
		this.forcefieldblock_meta = nbttagcompound.getShort("forceFieldblockMeta");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("accessType", this.accessType);
		nbttagcompound.setBoolean("burnout", this.burnout);
		nbttagcompound.setShort("forceFieldblockMeta", this.forcefieldblock_meta);
	}

	@Override
	public void onInventoryChanged()
	{
		this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	/*
	 * public void checkslots() { if (hasValidTypeMod()) { if (getProjectorType() !=
	 * ProjectorTypes.typeFromItem(getModule()).ProTyp) { } if (getforcefieldblock_meta() !=
	 * getModule().getForceFieldType().ordinal()) {
	 * setforcefieldblock_meta(getModule().getForceFieldType().ordinal()); }
	 * this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord); } else { if
	 * (getProjectorType() != 0) { } this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord,
	 * this.zCoord); }
	 * 
	 * if (hasValidTypeMod()) { for (int place = 7; place < 11; place++) { if (getStackInSlot(place)
	 * != null) { if (getStackInSlot(place).getItem() == ModularForceFieldSystem.itemFocusMatix) {
	 * switch (ProjectorTypes.typeFromItem(getModule()).ProTyp) { case 6: this.focusmatrix[(place -
	 * 7)] = (getStackInSlot(place).stackSize + 1); break; case 7: this.focusmatrix[(place - 7)] =
	 * (getStackInSlot(place).stackSize + 2); break; default: this.focusmatrix[(place - 7)] =
	 * getStackInSlot(place).stackSize; break; } } } else { switch
	 * (ProjectorTypes.typeFromItem(getModule()).ProTyp) { case 6: this.focusmatrix[(place - 7)] =
	 * 1; break; case 7: this.focusmatrix[(place - 7)] = 2; break; default: this.focusmatrix[(place
	 * - 7)] = 0; } }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * if (getStackInSlot(11) != null) { if (getStackInSlot(11).itemID < 4095) {
	 * 
	 * String textureFile; String forceFieldTextureTemp;
	 * 
	 * int[] index = new int[6]; for (int side = 0; side < 6; side++) { index[side] =
	 * Block.blocksList[getStackInSlot(11).itemID].getBlockTextureFromSideAndMetadata(side,
	 * getStackInSlot(11).getItemDamage()); } forceFieldTextureTemp = index[0] + "/" + index[1] +
	 * "/" + index[2] + "/" + index[3] + "/" + index[4] + "/" + index[5]; textureFile =
	 * Block.blocksList[getStackInSlot(11).itemID].getTextureFile();
	 * 
	 * if ((!forceFieldTextureTemp.equalsIgnoreCase(this.forceFieldTextureIDs)) ||
	 * (!this.forceFieldTextureFile.equalsIgnoreCase(getForceFieldTextureFile()))) { if
	 * (getStackInSlot(11).getItem() == Item.bucketLava) {
	 * setForceFieldTextureID("237/237/239/254/255/255"); } if (getStackInSlot(11).getItem() ==
	 * Item.bucketWater) { setForceFieldTextureID("205/205/207/222/223/223"); } if
	 * ((getStackInSlot(11).getItem() != Item.bucketLava) && (getStackInSlot(11).getItem() !=
	 * Item.bucketWater)) { setForceFieldTextureID(forceFieldTextureTemp); }
	 * setForceFieldCamoblockMeta(getStackInSlot(11).getItemDamage());
	 * setForceFieldCamoblockID(getStackInSlot(11).itemID); setForceFieldTextureFile(textureFile);
	 * updateForceFieldTexture(); }
	 * 
	 * } else { // dropPlugins(11, this); }
	 * 
	 * } else if ((!this.forceFieldTextureIDs.equalsIgnoreCase("-76/-76/-76/-76/-76/-76")) ||
	 * (getForceFieldCamoblockID() != -1)) { setForceFieldCamoblockMeta(0);
	 * setForceFieldCamoblockID(-1); setForceFieldTextureID("-76/-76/-76/-76/-76/-76");
	 * setForceFieldTextureFile("/terrain.png"); updateForceFieldTexture(); }
	 * 
	 * if ((hasOption(ModularForceFieldSystem.itemOptionCamouflage, true)) &&
	 * (getforcefieldblock_meta() != ForceFieldType.Camouflage.ordinal())) {
	 * setforcefieldblock_meta((short) ForceFieldType.Camouflage.ordinal()); }
	 * 
	 * if ((hasOption(ModularForceFieldSystem.itemOptionShock, true)) && (getforcefieldblock_meta()
	 * != ForceFieldType.Zapper.ordinal())) { setforcefieldblock_meta((short)
	 * ForceFieldType.Zapper.ordinal()); }
	 * 
	 * if (hasOption(ModularForceFieldSystem.itemOptionFieldFusion, true)) { if
	 * (!FrequencyGridOld.getWorldMap
	 * (this.worldObj).getFieldFusion().containsKey(Integer.valueOf(getDeviceID()))) {
	 * FrequencyGridOld
	 * .getWorldMap(this.worldObj).getFieldFusion().put(Integer.valueOf(getDeviceID()), this); } }
	 * else if
	 * (FrequencyGridOld.getWorldMap(this.worldObj).getFieldFusion().containsKey(Integer.valueOf
	 * (getDeviceID()))) {
	 * FrequencyGridOld.getWorldMap(this.worldObj).getFieldFusion().remove(Integer
	 * .valueOf(getDeviceID())); }
	 * 
	 * if (hasOption(ModularForceFieldSystem.itemOptionJammer, false)) { if
	 * (!FrequencyGridOld.getWorldMap
	 * (this.worldObj).getJammer().containsKey(Integer.valueOf(getDeviceID()))) {
	 * FrequencyGridOld.getWorldMap(this.worldObj).getJammer().put(Integer.valueOf(getDeviceID()),
	 * this); } } else if
	 * (FrequencyGridOld.getWorldMap(this.worldObj).getJammer().containsKey(Integer
	 * .valueOf(getDeviceID()))) {
	 * FrequencyGridOld.getWorldMap(this.worldObj).getJammer().remove(Integer
	 * .valueOf(getDeviceID())); }
	 * 
	 * if (hasValidTypeMod()) { ItemModule modType = getModule(); /* if
	 * (!modType.supportsStrength()) { dropPlugins(6, this); } if (!modType.supportsDistance()) {
	 * dropPlugins(5, this); } if (!modType.supportsMatrix()) { dropPlugins(7, this); dropPlugins(8,
	 * this); dropPlugins(9, this); dropPlugins(10, this); }
	 * 
	 * for (int spot = 2; spot <= 4; spot++) { if ((getStackInSlot(spot) != null) &&
	 * (!modType.supportsOption(getStackInSlot(spot).getItem()))) { dropPlugins(spot, this); }
	 * 
	 * if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof
	 * ItemOptionJammer)) && (isPowersourceItem())) { dropPlugins(spot, this); }
	 * 
	 * if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof
	 * ItemOptionFieldFusion)) && (isPowersourceItem())) { dropPlugins(spot, this); }
	 * 
	 * if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof
	 * ItemOptionDefenseStation)) && (isPowersourceItem())) { dropPlugins(spot, this); }
	 * 
	 * }
	 * 
	 * if ((getStackInSlot(12) != null) && ((getStackInSlot(12).getItem() instanceof
	 * ItemCardSecurityLink)) && (isPowersourceItem())) { dropPlugins(12, this); }
	 * 
	 * if (!hasOption(ModularForceFieldSystem.itemOptionCamouflage, true)) { dropPlugins(11, this);
	 * }
	 * 
	 * } else { for (int spot = 2; spot <= 10; spot++) { // dropPlugins(spot, this); } } }
	 */

	private void updateForceFieldTexture()
	{
		if ((isActive()) && (hasModule(ModularForceFieldSystem.itemOptionCamouflage, true)))
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
			if (((opt instanceof ItemOptionJammer)) && (((ItemOptionJammer) opt).CheckJammerinfluence(vector, this.worldObj, this)))
			{
				return false;
			}

			if (((opt instanceof ItemOptionFieldFusion)) && (((ItemOptionFieldFusion) opt).checkFieldFusioninfluence(vector, this.worldObj, this)))
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

			/*
			 * ForceFieldBlockStack blockStack =
			 * WorldMap.getForceFieldWorld(this.worldObj).getForceFieldStackMap
			 * (Integer.valueOf(vector.hashCode()));
			 * 
			 * if (blockStack != null) { if (!blockStack.isSync()) { Vector3 point =
			 * blockStack.getPoint();
			 * 
			 * if (this.worldObj.getChunkFromBlockCoords(vector.intX(), vector.intZ()).isChunkLoaded
			 * && !blockStack.isEmpty() && blockStack.getProjectorID() == getDeviceID()) { if
			 * (hasModule(ModularForceFieldSystem.itemOptionCutter, true)) { int blockid =
			 * this.worldObj.getBlockId(vector.intX(), vector.intY(), vector.intZ()); TileEntity
			 * entity = this.worldObj.getBlockTileEntity(vector.intX(), vector.intY(),
			 * vector.intZ());
			 * 
			 * if ((blockid != ModularForceFieldSystem.blockForceField.blockID) && (blockid != 0) &&
			 * (blockid != Block.bedrock.blockID) && (entity == null)) { ArrayList stacks =
			 * Functions.getItemStackFromBlock(this.worldObj, vector.intX(), vector.intY(),
			 * vector.intZ());
			 * 
			 * this.worldObj.setBlockWithNotify(vector.intX(), vector.intY(), vector.intZ(), 0);
			 * 
			 * if ((ProjectorTypes.typeFromItem(getMode()).blockDropper && (stacks != null))) {
			 * IInventory inventory = InventoryHelper.findAttachedInventory(this.worldObj,
			 * this.xCoord, this.yCoord, this.zCoord); if ((inventory != null) &&
			 * (inventory.getSizeInventory() > 0)) { InventoryHelper.addStacksToInventory(inventory,
			 * stacks); } } } }
			 * 
			 * if ((this.worldObj.getBlockMaterial(vector.intX(), vector.intY(),
			 * vector.intZ()).isLiquid()) || (this.worldObj.isAirBlock(vector.intX(), vector.intY(),
			 * vector.intZ())) || (this.worldObj.getBlockId(vector.intX(), vector.intY(),
			 * vector.intZ()) == ModularForceFieldSystem.blockForceField.blockID)) { if
			 * (this.worldObj.getBlockId(vector.intX(), vector.intY(), vector.intZ()) !=
			 * ModularForceFieldSystem.blockForceField.blockID) {
			 * this.worldObj.setBlockAndMetadataWithNotify(vector.intX(), vector.intY(),
			 * vector.intZ(), ModularForceFieldSystem.blockForceField.blockID, blockStack.getTyp());
			 * 
			 * this.blockCount += 1; }
			 * 
			 * blockStack.setSync(true); } } } }
			 */
		}
	}

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

		/*
		 * while (!this.fieldQueue.isEmpty()) { ForceFieldBlockStack ffworldmap =
		 * WorldMap.getForceFieldWorld(this.worldObj).getForceFieldStackMap((Integer)
		 * this.fieldQueue.pop());
		 * 
		 * if (!ffworldmap.isEmpty()) { if (ffworldmap.getProjectorID() == getDeviceID()) {
		 * ffworldmap.removebyProjector(getDeviceID());
		 * 
		 * if (ffworldmap.isSync()) { Vector3 vector = ffworldmap.getPoint();
		 * this.worldObj.removeBlockTileEntity(vector.intX(), vector.intY(), vector.intZ());
		 * this.worldObj.setBlockWithNotify(vector.intX(), vector.intY(), vector.intZ(), 0); }
		 * 
		 * ffworldmap.setSync(false); } else { ffworldmap.removebyProjector(getDeviceID()); } } }
		 * 
		 * Map<Integer, TileEntityProjector> FieldFusion =
		 * FrequencyGridOld.getWorldMap(this.worldObj).getFieldFusion(); for (TileEntityProjector
		 * tileentity : FieldFusion.values()) { // if (tileentity.getPowerSourceID() ==
		 * getPowerSourceID()) { if (tileentity.isActive()) { tileentity.calculateField(false); } }
		 * }
		 */
	}

	@Override
	public void invalidate()
	{
		FrequencyGridOld.getWorldMap(this.worldObj).getProjector().remove(Integer.valueOf(getDeviceID()));
		destroyField();
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
	public ItemStack decrStackSize(int i, int j)
	{
		if (this.inventory[i] != null)
		{
			if (this.inventory[i].stackSize <= j)
			{
				ItemStack itemstack = this.inventory[i];
				this.inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = this.inventory[i].splitStack(j);
			if (this.inventory[i].stackSize == 0)
			{
				this.inventory[i] = null;
			}
			return itemstack1;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.inventory[i] = itemstack;
		if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit()))
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventory[i];
	}

	@Override
	public int getSizeInventory()
	{
		return 1 + 3 * 3;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer)
	{
		return new ContainerProjector(inventoryplayer.player, this);
	}

	/*
	 * @Override public void onNetworkHandlerEvent(int key, String value) { if (!isActive()) {
	 * switch (key) { case 1: if (!isPowersourceItem()) { if (getAccessType() != 3) { if
	 * (getAccessType() == 2) { setAccessType(0); } else { setAccessType(getAccessType() + 1); } } }
	 * break; }
	 * 
	 * }
	 * 
	 * super.onNetworkHandlerEvent(key, value); }
	 * 
	 * @Override public List getFieldsForUpdate() { List NetworkedFields = new LinkedList();
	 * NetworkedFields.clear();
	 * 
	 * NetworkedFields.addAll(super.getFieldsForUpdate());
	 * 
	 * NetworkedFields.add("ProjektorTyp"); NetworkedFields.add("burnout");
	 * NetworkedFields.add("camoflage"); NetworkedFields.add("ForceFieldTexturfile");
	 * NetworkedFields.add("ForceFieldTexturids"); NetworkedFields.add("ForcefieldCamoblockid");
	 * NetworkedFields.add("ForcefieldCamoblockmeta");
	 * 
	 * return NetworkedFields; }
	 */
	/*
	 * @Override public boolean isItemValid(int slotID, ItemStack itemStack) { if ((slotID == 1) &&
	 * ((itemStack.getItem() instanceof ItemModuleBase))) return true;
	 * 
	 * if ((slotID == 0) && ((itemStack.getItem() instanceof IPowerLinkItem))) return true;
	 * 
	 * if ((slotID == 11) && (itemStack.itemID < 4096) &&
	 * (hasOption(ModularForceFieldSystem.itemOptionCamouflage, true))) return true;
	 * 
	 * if (hasValidTypeMod()) { ItemModuleBase modType = getType();
	 * 
	 * switch (slotID) { case 12: // if (((itemStack.getItem() instanceof ItemOptionDefenseStation))
	 * && // (isPowersourceItem())) // return false;
	 * 
	 * // if (((itemStack.getItem() instanceof ItemCardSecurityLink)) && // (isPowersourceItem()))
	 * // return false;
	 * 
	 * if ((itemStack.getItem() instanceof ItemCardSecurityLink)) return true;
	 * 
	 * break; case 5: if ((itemStack.getItem() instanceof ItemModuleDistance)) return
	 * modType.supportsDistance();
	 * 
	 * break; case 6: if ((itemStack.getItem() instanceof ItemModuleStrength)) { return
	 * modType.supportsStrength(); }
	 * 
	 * break; case 7: case 8: case 9: case 10: if ((itemStack.getItem() instanceof
	 * ItemProjectorFocusMatrix)) return modType.supportsMatrix();
	 * 
	 * break; case 2: case 3: case 4: if (isActive()) return false;
	 * 
	 * if ((itemStack.getItem() instanceof ItemOptionShock)) { for (int spot = 2; spot <= 4; spot++)
	 * { if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof
	 * ItemOptionCamoflage))) return false; } }
	 * 
	 * if ((itemStack.getItem() instanceof ItemOptionCamoflage)) { for (int spot = 2; spot <= 4;
	 * spot++) { if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof
	 * ItemOptionShock))) return false; }
	 * 
	 * }
	 * 
	 * if (!hasPowerSource()) return false;
	 * 
	 * // if (((itemStack.getItem() instanceof ItemOptionDefenseStation)) && //
	 * (isPowersourceItem())) // return false;
	 * 
	 * // if (((itemStack.getItem() instanceof ItemOptionFieldFusion)) && // (isPowersourceItem()))
	 * // return false;
	 * 
	 * // if (((itemStack.getItem() instanceof ItemOptionJammer)) && // (isPowersourceItem())) //
	 * return false;
	 * 
	 * if ((itemStack.getItem() instanceof ItemOptionBase)) return
	 * modType.supportsOption(itemStack.getItem()); } }
	 * 
	 * return false; }
	 */

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

	public int getSecStation_ID()
	{
		TileEntitySecurityStation sec = getLinkedSecurityStation();
		if (sec != null)
		{
			return sec.getDeviceID();
		}
		return 0;
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

	public boolean hasModule(IModule item, boolean includeCheckAll)
	{
		for (IModule opt : getModules())
		{
			if (opt == item)
				return true;
		}

		return false;
	}

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

	/*
	 * @Override public ItemStack getPowerLinkStack() { return getStackInSlot(getPowerLinkSlot()); }
	 * 
	 * @Override public int getPowerLinkSlot() { return 0; }
	 */

	@Override
	public World getWorldObj()
	{
		return this.worldObj;
	}

	public static int[] getSlotsBasedOnDirection(ForgeDirection direction)
	{
		switch (direction)
		{
			default:
				return null;
			case UP:
				return new int[] { 1, 3 };
			case DOWN:
				return new int[] { 7, 9 };
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