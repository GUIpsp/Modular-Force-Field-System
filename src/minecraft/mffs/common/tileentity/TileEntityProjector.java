package mffs.common.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import mffs.api.IPowerLinkItem;
import mffs.api.PointXYZ;
import mffs.common.ForceFieldBlockStack;
import mffs.common.ForceFieldTyps;
import mffs.common.FrequencyGrid;
import mffs.common.Functions;
import mffs.common.IModularProjector;
import mffs.common.InventoryHelper;
import mffs.common.MFFSConfiguration;
import mffs.common.ModularForceFieldSystem;
import mffs.common.ProjectorTypes;
import mffs.common.WorldMap;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.container.ContainerProjector;
import mffs.common.modules.ItemModule3DBase;
import mffs.common.modules.ItemModuleBase;
import mffs.common.options.IChecksOnAll;
import mffs.common.options.IInteriorCheck;
import mffs.common.options.ItemOptionAntibiotic;
import mffs.common.options.ItemOptionBase;
import mffs.common.options.ItemOptionCamoflage;
import mffs.common.options.ItemOptionDefenseStation;
import mffs.common.options.ItemOptionFieldFusion;
import mffs.common.options.ItemOptionJammer;
import mffs.common.options.ItemOptionShock;
import mffs.common.upgrade.ItemModuleDistance;
import mffs.common.upgrade.ItemModuleStrength;
import mffs.common.upgrade.ItemProjectorFocusMatrix;
import mffs.network.server.NetworkHandlerServer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityProjector extends TileEntityForcePowerMachine implements IModularProjector
{

	private ItemStack[] ProjectorItemStacks = new ItemStack[13];

	protected Stack field_queue = new Stack();
	protected Set field_interior = new HashSet();
	protected Set<PointXYZ> field_def = new HashSet();

	private short forcefieldblock_meta = ((short) ForceFieldTyps.Default.ordinal());

	private String forceFieldTextureIDs = "-76/-76/-76/-76/-76/-76";
	private String forceFieldTextureFile = "/terrain.png";

	private boolean burnout = false;

	private int[] focusmatrix = { 0, 0, 0, 0 };
	private int forceFieldCamoblockID;
	private int forceFieldCamoblockMeta;
	private int blockCounter;
	private int accessType = 0;
	private int capacity = 0;
	private int projectorType = 0;
	private int linkPower = 0;
	private int switchDelay = 0;

	public int getCapacity()
	{
		return this.capacity;
	}

	public void setCapacity(int Capacity)
	{
		this.capacity = Capacity;
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
		NetworkHandlerServer.updateTileEntityField(this, "ForceFieldCamoblockMeta");
	}

	public int getForceFieldCamoblockID()
	{
		return this.forceFieldCamoblockID;
	}

	public void setForceFieldCamoblockID(int forcefieldCamoblockid)
	{
		this.forceFieldCamoblockID = forcefieldCamoblockid;
		NetworkHandlerServer.updateTileEntityField(this, "ForceFieldCamoblockID");
	}

	public String getForceFieldTextureFile()
	{
		return this.forceFieldTextureFile;
	}

	public void setForceFieldTextureFile(String forceFieldTexturfile)
	{
		this.forceFieldTextureFile = forceFieldTexturfile;
		NetworkHandlerServer.updateTileEntityField(this, "ForceFieldTextureFile");
	}

	public String getForceFieldTextureID()
	{
		return this.forceFieldTextureIDs;
	}

	public void setForceFieldTextureID(String forceFieldTextureIDs)
	{
		this.forceFieldTextureIDs = forceFieldTextureIDs;
		NetworkHandlerServer.updateTileEntityField(this, "forceFieldTextureIDs");
	}

	public int getProjectorType()
	{
		return this.projectorType;
	}

	public void setProjectorType(int projectorType)
	{
		this.projectorType = projectorType;
		NetworkHandlerServer.updateTileEntityField(this, "projectorType");
	}

	public int getBlockCounter()
	{
		return this.blockCounter;
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
		dropPlugins();
	}

	public boolean isBurnout()
	{
		return this.burnout;
	}

	@Override
	public void setBurnedOut(boolean b)
	{
		this.burnout = b;
		NetworkHandlerServer.updateTileEntityField(this, "burnout");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);

		this.accessType = nbttagcompound.getInteger("accessType");
		this.burnout = nbttagcompound.getBoolean("burnout");
		this.projectorType = nbttagcompound.getInteger("ProjectorType");
		this.forcefieldblock_meta = nbttagcompound.getShort("forceFieldblockMeta");

		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		this.ProjectorItemStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);

			byte byte0 = nbttagcompound1.getByte("Slot");
			if ((byte0 >= 0) && (byte0 < this.ProjectorItemStacks.length))
			{
				this.ProjectorItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("accessType", this.accessType);
		nbttagcompound.setBoolean("burnout", this.burnout);
		nbttagcompound.setInteger("ProjectorType", this.projectorType);
		nbttagcompound.setShort("forceFieldblockMeta", this.forcefieldblock_meta);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.ProjectorItemStacks.length; i++)
		{
			if (this.ProjectorItemStacks[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.ProjectorItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public void dropPlugins()
	{
		for (int a = 0; a < this.ProjectorItemStacks.length; a++)
		{
			dropPlugins(a, this);
		}
	}

	@Override
	public void onInventoryChanged()
	{
		getLinkedSecurityStation();
		checkslots();
	}

	public void checkslots()
	{
		if (hasValidTypeMod())
		{
			if (getProjectorType() != ProjectorTypes.typeFromItem(getType()).ProTyp)
			{
				setProjectorType(ProjectorTypes.typeFromItem(getType()).ProTyp);
			}
			if (getforcefieldblock_meta() != getType().getForceFieldTyps().ordinal())
			{
				setforcefieldblock_meta(getType().getForceFieldTyps().ordinal());
			}
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		else
		{
			if (getProjectorType() != 0)
			{
				setProjectorType(0);
			}
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}

		if (hasValidTypeMod())
		{
			for (int place = 7; place < 11; place++)
			{
				if (getStackInSlot(place) != null)
				{
					if (getStackInSlot(place).getItem() == ModularForceFieldSystem.itemFocusMatix)
					{
						switch (ProjectorTypes.typeFromItem(getType()).ProTyp)
						{
							case 6:
								this.focusmatrix[(place - 7)] = (getStackInSlot(place).stackSize + 1);
								break;
							case 7:
								this.focusmatrix[(place - 7)] = (getStackInSlot(place).stackSize + 2);
								break;
							default:
								this.focusmatrix[(place - 7)] = getStackInSlot(place).stackSize;
								break;
						}
					}
					else
					{
						dropPlugins(place, this);
					}
				}
				else
				{
					switch (ProjectorTypes.typeFromItem(getType()).ProTyp)
					{
						case 6:
							this.focusmatrix[(place - 7)] = 1;
							break;
						case 7:
							this.focusmatrix[(place - 7)] = 2;
							break;
						default:
							this.focusmatrix[(place - 7)] = 0;
					}
				}

			}

		}

		if (getStackInSlot(11) != null)
		{
			if (getStackInSlot(11).itemID < 4095)
			{

				String textureFile;
				String forceFieldTextureTemp;

				int[] index = new int[6];
				for (int side = 0; side < 6; side++)
				{
					index[side] = Block.blocksList[getStackInSlot(11).itemID].getBlockTextureFromSideAndMetadata(side, getStackInSlot(11).getItemDamage());
				}
				forceFieldTextureTemp = index[0] + "/" + index[1] + "/" + index[2] + "/" + index[3] + "/" + index[4] + "/" + index[5];
				textureFile = Block.blocksList[getStackInSlot(11).itemID].getTextureFile();

				if ((!forceFieldTextureTemp.equalsIgnoreCase(this.forceFieldTextureIDs)) || (!this.forceFieldTextureFile.equalsIgnoreCase(getForceFieldTextureFile())))
				{
					if (getStackInSlot(11).getItem() == Item.bucketLava)
					{
						setForceFieldTextureID("237/237/239/254/255/255");
					}
					if (getStackInSlot(11).getItem() == Item.bucketWater)
					{
						setForceFieldTextureID("205/205/207/222/223/223");
					}
					if ((getStackInSlot(11).getItem() != Item.bucketLava) && (getStackInSlot(11).getItem() != Item.bucketWater))
					{
						setForceFieldTextureID(forceFieldTextureTemp);
					}
					setForceFieldCamoblockMeta(getStackInSlot(11).getItemDamage());
					setForceFieldCamoblockID(getStackInSlot(11).itemID);
					setForceFieldTextureFile(textureFile);
					updateForceFieldTexture();
				}

			}
			else
			{
				dropPlugins(11, this);
			}

		}
		else if ((!this.forceFieldTextureIDs.equalsIgnoreCase("-76/-76/-76/-76/-76/-76")) || (getForceFieldCamoblockID() != -1))
		{
			setForceFieldCamoblockMeta(0);
			setForceFieldCamoblockID(-1);
			setForceFieldTextureID("-76/-76/-76/-76/-76/-76");
			setForceFieldTextureFile("/terrain.png");
			updateForceFieldTexture();
		}

		if ((hasOption(ModularForceFieldSystem.itemOptionCamouflage, true)) && (getforcefieldblock_meta() != ForceFieldTyps.Camouflage.ordinal()))
		{
			setforcefieldblock_meta((short) ForceFieldTyps.Camouflage.ordinal());
		}

		if ((hasOption(ModularForceFieldSystem.itemOptionShock, true)) && (getforcefieldblock_meta() != ForceFieldTyps.Zapper.ordinal()))
		{
			setforcefieldblock_meta((short) ForceFieldTyps.Zapper.ordinal());
		}

		if (hasOption(ModularForceFieldSystem.itemOptionFieldFusion, true))
		{
			if (!FrequencyGrid.getWorldMap(this.worldObj).getFieldFusion().containsKey(Integer.valueOf(getDeviceID())))
			{
				FrequencyGrid.getWorldMap(this.worldObj).getFieldFusion().put(Integer.valueOf(getDeviceID()), this);
			}
		}
		else if (FrequencyGrid.getWorldMap(this.worldObj).getFieldFusion().containsKey(Integer.valueOf(getDeviceID())))
		{
			FrequencyGrid.getWorldMap(this.worldObj).getFieldFusion().remove(Integer.valueOf(getDeviceID()));
		}

		if (hasOption(ModularForceFieldSystem.itemOptionJammer, false))
		{
			if (!FrequencyGrid.getWorldMap(this.worldObj).getJammer().containsKey(Integer.valueOf(getDeviceID())))
			{
				FrequencyGrid.getWorldMap(this.worldObj).getJammer().put(Integer.valueOf(getDeviceID()), this);
			}
		}
		else if (FrequencyGrid.getWorldMap(this.worldObj).getJammer().containsKey(Integer.valueOf(getDeviceID())))
		{
			FrequencyGrid.getWorldMap(this.worldObj).getJammer().remove(Integer.valueOf(getDeviceID()));
		}

		if (hasValidTypeMod())
		{
			ItemModuleBase modType = getType();

			if (!modType.supportsStrength())
			{
				dropPlugins(6, this);
			}
			if (!modType.supportsDistance())
			{
				dropPlugins(5, this);
			}
			if (!modType.supportsMatrix())
			{
				dropPlugins(7, this);
				dropPlugins(8, this);
				dropPlugins(9, this);
				dropPlugins(10, this);
			}

			for (int spot = 2; spot <= 4; spot++)
			{
				if ((getStackInSlot(spot) != null) && (!modType.supportsOption(getStackInSlot(spot).getItem())))
				{
					dropPlugins(spot, this);
				}

				if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof ItemOptionJammer)) && (isPowersourceItem()))
				{
					dropPlugins(spot, this);
				}

				if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof ItemOptionFieldFusion)) && (isPowersourceItem()))
				{
					dropPlugins(spot, this);
				}

				if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof ItemOptionDefenseStation)) && (isPowersourceItem()))
				{
					dropPlugins(spot, this);
				}

			}

			if ((getStackInSlot(12) != null) && ((getStackInSlot(12).getItem() instanceof ItemCardSecurityLink)) && (isPowersourceItem()))
			{
				dropPlugins(12, this);
			}

			if (!hasOption(ModularForceFieldSystem.itemOptionCamouflage, true))
			{
				dropPlugins(11, this);
			}
		}
		else
		{
			for (int spot = 2; spot <= 10; spot++)
			{
				dropPlugins(spot, this);
			}
		}
	}

	private void updateForceFieldTexture()
	{
		if ((isActive()) && (hasOption(ModularForceFieldSystem.itemOptionCamouflage, true)))
		{
			for (PointXYZ png : this.field_def)
			{
				if (this.worldObj.getChunkFromBlockCoords(png.X, png.Z).isChunkLoaded)
				{
					TileEntity tileEntity = this.worldObj.getBlockTileEntity(png.X, png.Y, png.Z);

					if ((tileEntity != null) && ((tileEntity instanceof TileEntityForceField)))
					{
						((TileEntityForceField) tileEntity).updateTexture();
					}
				}
			}
		}
	}

	@Override
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			if (this.init)
			{
				checkslots();
				if (isActive())
				{
					calculateField(true);
				}
			}

			if (hasPowerSource())
			{
				setLinkPower((int) getForcePower());

				if ((isPowersourceItem()) && (getAccessType() != 0))
				{
					setAccessType(0);
				}
			}
			else
			{
				setLinkPower(0);
			}

			if ((getSwitchModi() == 1) && (!getSwitchValue()) && (isRedstoneSignal()))
			{
				toggelSwitchValue();
			}
			if ((getSwitchModi() == 1) && (getSwitchValue()) && (!isRedstoneSignal()))
			{
				toggelSwitchValue();
			}

			if ((getSwitchValue()) && (this.switchDelay >= 40) && (hasValidTypeMod()) && (hasPowerSource()) && (getLinkPower() > forcePowerNeed(5)))
			{
				if (isActive() != true)
				{
					setActive(true);
					this.switchDelay = 0;
					if (calculateField(true))
					{
						fieldGenerate(true);
					}
					this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
				}
			}
			if (((!getSwitchValue()) && (this.switchDelay >= 40)) || (!hasValidTypeMod()) || (!hasPowerSource()) || (this.burnout) || (getLinkPower() <= forcePowerNeed(1)))
			{
				if (isActive())
				{
					setActive(false);
					this.switchDelay = 0;
					destroyField();
					this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
				}
			}

			if (getTicker() == 20)
			{
				if (isActive())
				{
					fieldGenerate(false);

					if (hasOption(ModularForceFieldSystem.itemOptionAntibiotic, true))
					{
						ItemOptionAntibiotic.ProjectorNPCDefence(this, this.worldObj);
					}

					if (hasOption(ModularForceFieldSystem.itemOptionDefenseeStation, true))
					{
						ItemOptionDefenseStation.ProjectorPlayerDefence(this, this.worldObj);
					}

				}

				setTicker((short) 0);
			}

			setTicker((short) (getTicker() + 1));
		}
		this.switchDelay += 1;
		super.updateEntity();
	}

	private boolean calculateField(boolean addtoMap)
	{
		this.field_def.clear();
		this.field_interior.clear();
		if (hasValidTypeMod())
		{
			Set<PointXYZ> tField = new HashSet();
			Set<PointXYZ> tFieldInt = new HashSet();

			if ((getType() instanceof ItemModule3DBase))
			{
				((ItemModule3DBase) getType()).calculateField(this, tField, tFieldInt);
			}
			else
			{
				getType().calculateField(this, tField);
			}

			for (PointXYZ pnt : tField)
			{
				if (pnt.Y + this.yCoord < 255)
				{
					PointXYZ tp = new PointXYZ(pnt.X + this.xCoord, pnt.Y + this.yCoord, pnt.Z + this.zCoord, this.worldObj);

					if (forceFieldDefine(tp, addtoMap))
					{
						this.field_def.add(tp);
					}
					else
					{
						return false;
					}
				}
			}
			for (PointXYZ pnt : tFieldInt)
			{
				if (pnt.Y + this.yCoord < 255)
				{
					PointXYZ tp = new PointXYZ(pnt.X + this.xCoord, pnt.Y + this.yCoord, pnt.Z + this.zCoord, this.worldObj);

					if (calculateBlock(tp))
					{
						this.field_interior.add(tp);
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

	public boolean calculateBlock(PointXYZ pnt)
	{
		for (ItemOptionBase opt : getOptions(true))
		{
			if ((opt instanceof IInteriorCheck))
			{
				((IInteriorCheck) opt).checkInteriorBlock(pnt, this.worldObj, this);
			}
		}
		return true;
	}

	public boolean forceFieldDefine(PointXYZ png, boolean addtoMap)
	{
		for (ItemOptionBase opt : getOptions(true))
		{
			if (((opt instanceof ItemOptionJammer)) && (((ItemOptionJammer) opt).CheckJammerinfluence(png, this.worldObj, this)))
			{
				return false;
			}

			if (((opt instanceof ItemOptionFieldFusion)) && (((ItemOptionFieldFusion) opt).checkFieldFusioninfluence(png, this.worldObj, this)))
			{
				return true;
			}

		}

		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(this.worldObj).getorcreateFFStackMap(png.X, png.Y, png.Z, this.worldObj);

		if (!ffworldmap.isEmpty())
		{
			if (ffworldmap.getProjectorID() != getDeviceID())
			{
				ffworldmap.removebyProjector(getDeviceID());
				ffworldmap.add(getPowerSourceID(), getDeviceID(), getforcefieldblock_meta());
			}
		}
		else
		{
			ffworldmap.add(getPowerSourceID(), getDeviceID(), getforcefieldblock_meta());
			ffworldmap.setSync(false);
		}

		this.field_queue.push(Integer.valueOf(png.hashCode()));

		return true;
	}

	public void fieldGenerate(boolean init)
	{
		int cost = 0;

		if (init)
		{
			cost = MFFSConfiguration.forcefieldblockcost * MFFSConfiguration.forcefieldblockcreatemodifier;
		}
		else
		{
			cost = MFFSConfiguration.forcefieldblockcost;
		}

		if (getforcefieldblock_meta() == 1)
		{
			cost *= MFFSConfiguration.forcefieldblockzappermodifier;
		}

		consumePower(cost * this.field_def.size(), false);

		this.blockCounter = 0;

		for (PointXYZ pnt : this.field_def)
		{
			if (this.blockCounter == MFFSConfiguration.forcefieldmaxblockpeerTick)
			{
				break;
			}
			ForceFieldBlockStack ffb = WorldMap.getForceFieldWorld(this.worldObj).getForceFieldStackMap(Integer.valueOf(pnt.hashCode()));

			if (ffb != null)
			{
				if (!ffb.isSync())
				{
					PointXYZ png = ffb.getPoint();

					if ((this.worldObj.getChunkFromBlockCoords(png.X, png.Z).isChunkLoaded) && (!ffb.isEmpty()) && (ffb.getProjectorID() == getDeviceID()))
					{
						if (hasOption(ModularForceFieldSystem.itemOptionCutter, true))
						{
							int blockid = this.worldObj.getBlockId(png.X, png.Y, png.Z);
							TileEntity entity = this.worldObj.getBlockTileEntity(png.X, png.Y, png.Z);

							if ((blockid != ModularForceFieldSystem.blockForceField.blockID) && (blockid != 0) && (blockid != Block.bedrock.blockID) && (entity == null))
							{
								ArrayList stacks = Functions.getItemStackFromBlock(this.worldObj, png.X, png.Y, png.Z);

								this.worldObj.setBlockWithNotify(png.X, png.Y, png.Z, 0);

								if ((ProjectorTypes.typeFromItem(getType()).Blockdropper) && (stacks != null))
								{
									IInventory inventory = InventoryHelper.findAttachedInventory(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
									if ((inventory != null) && (inventory.getSizeInventory() > 0))
									{
										InventoryHelper.addStacksToInventory(inventory, stacks);
									}
								}
							}

						}

						if ((this.worldObj.getBlockMaterial(png.X, png.Y, png.Z).isLiquid()) || (this.worldObj.isAirBlock(png.X, png.Y, png.Z)) || (this.worldObj.getBlockId(png.X, png.Y, png.Z) == ModularForceFieldSystem.blockForceField.blockID))
						{
							if (this.worldObj.getBlockId(png.X, png.Y, png.Z) != ModularForceFieldSystem.blockForceField.blockID)
							{
								this.worldObj.setBlockAndMetadataWithNotify(png.X, png.Y, png.Z, ModularForceFieldSystem.blockForceField.blockID, ffb.getTyp());

								this.blockCounter += 1;
							}
							ffb.setSync(true);
						}
					}
				}
			}
		}
	}

	public void destroyField()
	{
		while (!this.field_queue.isEmpty())
		{
			ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(this.worldObj).getForceFieldStackMap((Integer) this.field_queue.pop());

			if (!ffworldmap.isEmpty())
			{
				if (ffworldmap.getProjectorID() == getDeviceID())
				{
					ffworldmap.removebyProjector(getDeviceID());

					if (ffworldmap.isSync())
					{
						PointXYZ png = ffworldmap.getPoint();
						this.worldObj.removeBlockTileEntity(png.X, png.Y, png.Z);
						this.worldObj.setBlockWithNotify(png.X, png.Y, png.Z, 0);
					}

					ffworldmap.setSync(false);
				}
				else
				{
					ffworldmap.removebyProjector(getDeviceID());
				}
			}
		}

		Map<Integer, TileEntityProjector> FieldFusion = FrequencyGrid.getWorldMap(this.worldObj).getFieldFusion();
		for (TileEntityProjector tileentity : FieldFusion.values())
		{
			if (tileentity.getPowerSourceID() == getPowerSourceID())
			{
				if (tileentity.isActive())
				{
					tileentity.calculateField(false);
				}
			}
		}
	}

	@Override
	public void invalidate()
	{
		FrequencyGrid.getWorldMap(this.worldObj).getProjector().remove(Integer.valueOf(getDeviceID()));
		destroyField();
		super.invalidate();
	}

	public int forcePowerNeed(int factor)
	{
		if (!this.field_def.isEmpty())
		{
			return this.field_def.size() * MFFSConfiguration.forcefieldblockcost;
		}

		int forcepower = 0;
		int blocks = 0;

		int tmplength = 1;

		if (countItemsInSlot(IModularProjector.Slots.Strength) != 0)
		{
			tmplength = countItemsInSlot(IModularProjector.Slots.Strength);
		}

		switch (getProjectorType())
		{
			case 1:
				blocks = (countItemsInSlot(IModularProjector.Slots.FocusDown) + countItemsInSlot(IModularProjector.Slots.FocusLeft) + countItemsInSlot(IModularProjector.Slots.FocusRight) + countItemsInSlot(IModularProjector.Slots.FocusUp) + 1) * tmplength;

				break;
			case 2:
				blocks = (countItemsInSlot(IModularProjector.Slots.FocusDown) + countItemsInSlot(IModularProjector.Slots.FocusUp) + 1) * (countItemsInSlot(IModularProjector.Slots.FocusLeft) + countItemsInSlot(IModularProjector.Slots.FocusRight) + 1);

				break;
			case 3:
				blocks = ((countItemsInSlot(IModularProjector.Slots.Distance) + 2 + countItemsInSlot(IModularProjector.Slots.Distance) + 2) * 4 + 4) * (countItemsInSlot(IModularProjector.Slots.Strength) + 1);

				break;
			case 4:
			case 5:
				blocks = countItemsInSlot(IModularProjector.Slots.Distance) * countItemsInSlot(IModularProjector.Slots.Distance) * 6;
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
		if (this.ProjectorItemStacks[i] != null)
		{
			if (this.ProjectorItemStacks[i].stackSize <= j)
			{
				ItemStack itemstack = this.ProjectorItemStacks[i];
				this.ProjectorItemStacks[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = this.ProjectorItemStacks[i].splitStack(j);
			if (this.ProjectorItemStacks[i].stackSize == 0)
			{
				this.ProjectorItemStacks[i] = null;
			}
			return itemstack1;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.ProjectorItemStacks[i] = itemstack;
		if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit()))
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.ProjectorItemStacks[i];
	}

	@Override
	public String getInvName()
	{
		return "Projektor";
	}

	@Override
	public int getSizeInventory()
	{
		return this.ProjectorItemStacks.length;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer)
	{
		return new ContainerProjector(inventoryplayer.player, this);
	}

	@Override
	public int getStartInventorySide(ForgeDirection side)
	{
		return 11;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		return 1;
	}

	@Override
	public void onNetworkHandlerEvent(int key, String value)
	{
		if (!isActive())
		{
			switch (key)
			{
				case 1:
					if (!isPowersourceItem())
					{
						if (getAccessType() != 3)
						{
							if (getAccessType() == 2)
							{
								setAccessType(0);
							}
							else
							{
								setAccessType(getAccessType() + 1);
							}
						}
					}
					break;
			}

		}

		super.onNetworkHandlerEvent(key, value);
	}

	@Override
	public List getFieldsForUpdate()
	{
		List NetworkedFields = new LinkedList();
		NetworkedFields.clear();

		NetworkedFields.addAll(super.getFieldsForUpdate());

		NetworkedFields.add("ProjektorTyp");
		NetworkedFields.add("burnout");
		NetworkedFields.add("camoflage");
		NetworkedFields.add("ForceFieldTexturfile");
		NetworkedFields.add("ForceFieldTexturids");
		NetworkedFields.add("ForcefieldCamoblockid");
		NetworkedFields.add("ForcefieldCamoblockmeta");

		return NetworkedFields;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack, int slot)
	{
		if ((slot == 1) && ((itemStack.getItem() instanceof ItemModuleBase)))
			return true;

		if ((slot == 0) && ((itemStack.getItem() instanceof IPowerLinkItem)))
			return true;

		if ((slot == 11) && (itemStack.itemID < 4096) && (hasOption(ModularForceFieldSystem.itemOptionCamouflage, true)))
			return true;

		if (hasValidTypeMod())
		{
			ItemModuleBase modType = getType();

			switch (slot)
			{
				case 12:
					if (((itemStack.getItem() instanceof ItemOptionDefenseStation)) && (isPowersourceItem()))
						return false;

					if (((itemStack.getItem() instanceof ItemCardSecurityLink)) && (isPowersourceItem()))
						return false;

					if ((itemStack.getItem() instanceof ItemCardSecurityLink))
						return true;

					break;
				case 5:
					if ((itemStack.getItem() instanceof ItemModuleDistance))
						return modType.supportsDistance();

					break;
				case 6:
					if ((itemStack.getItem() instanceof ItemModuleStrength))
					{
						return modType.supportsStrength();
					}

					break;
				case 7:
				case 8:
				case 9:
				case 10:
					if ((itemStack.getItem() instanceof ItemProjectorFocusMatrix))
						return modType.supportsMatrix();

					break;
				case 2:
				case 3:
				case 4:
					if (isActive())
						return false;

					if ((itemStack.getItem() instanceof ItemOptionShock))
					{
						for (int spot = 2; spot <= 4; spot++)
						{
							if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof ItemOptionCamoflage)))
								return false;
						}
					}

					if ((itemStack.getItem() instanceof ItemOptionCamoflage))
					{
						for (int spot = 2; spot <= 4; spot++)
						{
							if ((getStackInSlot(spot) != null) && ((getStackInSlot(spot).getItem() instanceof ItemOptionShock)))
								return false;
						}

					}

					if (!hasPowerSource())
						return false;

					if (((itemStack.getItem() instanceof ItemOptionDefenseStation)) && (isPowersourceItem()))
						return false;

					if (((itemStack.getItem() instanceof ItemOptionFieldFusion)) && (isPowersourceItem()))
						return false;

					if (((itemStack.getItem() instanceof ItemOptionJammer)) && (isPowersourceItem()))
						return false;

					if ((itemStack.getItem() instanceof ItemOptionBase))
						return modType.supportsOption(itemStack.getItem());
			}
		}

		return false;
	}

	@Override
	public int getSlotStackLimit(int slot)
	{

		if ((slot >= 5) && (slot <= 10))
			return 64;

		return 1;
	}

	public boolean hasValidTypeMod()
	{
		if ((getStackInSlot(1) != null) && ((getStackInSlot(1).getItem() instanceof ItemModuleBase)))
		{
			return true;
		}
		return false;
	}

	public ItemModuleBase getType()
	{
		if (hasValidTypeMod())
		{
			return (ItemModuleBase) getStackInSlot(1).getItem();
		}
		return null;
	}

	@Override
	public Set<PointXYZ> getInteriorPoints()
	{
		return this.field_interior;
	}

	public Set<PointXYZ> getFieldQueue()
	{
		return this.field_def;
	}

	@Override
	public TileEntitySecurityStation getLinkedSecurityStation()
	{
		TileEntitySecurityStation sec = ItemCardSecurityLink.getLinkedSecurityStation(this, 12, this.worldObj);
		if (sec != null)
		{
			if ((getAccessType() != 3) && (!isPowersourceItem()))
				setAccessType(3);

			return sec;
		}

		if (getAccessType() == 3)
			setAccessType(0);

		return null;
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

	public boolean hasOption(Item item, boolean includeCheckAll)
	{
		for (ItemOptionBase opt : getOptions(includeCheckAll))
		{
			if (opt == item)
				return true;

		}
		return false;
	}

	public List<ItemOptionBase> getOptions(boolean includeCheckAll)
	{
		List ret = new ArrayList();
		for (int place = 2; place < 5; place++)
		{
			if ((getStackInSlot(place) != null) && ((getStackInSlot(place).getItem() instanceof ItemOptionBase)))
			{
				ret.add((ItemOptionBase) getStackInSlot(place).getItem());
			}

			if (includeCheckAll)
			{
				for (ItemOptionBase opt : ItemOptionBase.get_instances())
				{
					if (((opt instanceof IChecksOnAll)) && (!ret.contains(opt)))
					{
						ret.add(opt);
					}
				}
			}
		}

		return ret;
	}

	@Override
	public short getMaxSwitchModi()
	{
		return 3;
	}

	@Override
	public short getMinSwitchModi()
	{
		return 1;
	}

	@Override
	public ItemStack getPowerLinkStack()
	{
		return getStackInSlot(getPowerLinkSlot());
	}

	@Override
	public int getPowerLinkSlot()
	{
		return 0;
	}

	@Override
	public World getWorldObj()
	{
		return this.worldObj;
	}
}