package mffs.common.tileentity;

import java.util.LinkedList;
import java.util.List;

import com.google.common.io.ByteArrayDataInput;

import icbm.api.RadarRegistry;
import mffs.api.IForceEnergyItems;
import mffs.api.IFortronStorage;
import mffs.api.IPowerLinkItem;
import mffs.common.Fortron;
import mffs.common.FrequencyGrid;
import mffs.common.ModularForceFieldSystem;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.container.ContainerCapacitor;
import mffs.common.upgrade.ItemUpgradeCapacity;
import mffs.common.upgrade.ItemUpgradeRange;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidTank;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;

public class TileEntityFortronCapacitor extends TileEntityFortron implements IFortronStorage
{
	private int distributionMode = 0;
	private int transmissionRange = 0;

	public TileEntityFortronCapacitor()
	{
		this.fortronTank.setCapacity(500 * LiquidContainerRegistry.BUCKET_VOLUME);
	}
	
	@Override
	public void initiate()
	{
		super.initiate();
		RadarRegistry.register(this);
	}

	@Override
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			if (!this.isDisabled())
			{
				/**
				 * Transmit fortrons in frequency network, evenly distributing them.
				 */
				if (this.ticks % 4 == 0 && this.playersUsing > 0)
				{
					
					PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
				}
			}
		}
		
		/*
		if (!this.worldObj.isRemote)
		{
			if ((getStatusMode() == 1) && (!getStatusValue()) && (isPoweredByRedstone()))
				onToggle();

			if ((getStatusMode() == 1) && (getStatusValue()) && (!isPoweredByRedstone()))
				onToggle();

			if (getStatusValue())
			{
				if (isActive() != true)
					setActive(true);

			}
			else if (isActive())
			{
				setActive(false);
			}

			if (this.ticks % 10 == 0)
			{
				if (getLinkedProjector().shortValue() != (short) FrequencyGrid.getWorldMap(this.worldObj).connectedtoCapacitor(this, getTransmitRange()))
				{
					setLinketprojektor(Short.valueOf((short) FrequencyGrid.getWorldMap(this.worldObj).connectedtoCapacitor(this, getTransmitRange())));
				}
				if (getPercentageStorageCapacity() != getStorageAvailablePower() / 1000 * 100 / (getStorageMaxPower() / 1000))
				{
					setCapacity(getStorageAvailablePower() / 1000 * 100 / (getStorageMaxPower() / 1000));
				}

				checkSlots();

				if (isActive())
				{
					powerTransfer();
				}
			}
		}
		super.updateEntity();*/
	}
	
	/**
	 * Packet Methods
	 */
	@Override
	public List getPacketUpdate()
	{
		List objects = new LinkedList();
		objects.addAll(super.getPacketUpdate());
		objects.add(this.distributionMode);
		objects.add(this.transmissionRange);
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		super.onReceivePacket(packetID, dataStream);
		this.distributionMode = dataStream.readInt();
		this.transmissionRange = dataStream.readInt();
	}

	@Override
	public void invalidate()
	{
		RadarRegistry.unregister(this);
		super.invalidate();
	}

	public void setTransmitRange(int transmitRange)
	{
		this.transmissionRange = transmitRange;
        PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
	}

	public int getTransmitRange()
	{
		return this.transmissionRange;
	}

	public int getPowerLinkMode()
	{
		return this.distributionMode;
	}

	public void setPowerLinkMode(int powerLinkMode)
	{
		this.distributionMode = powerLinkMode;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer)
	{
		return new ContainerCapacitor(inventoryplayer.player, this);
	}

/*
	@Override
	public int getStorageAvailablePower()
	{
		return this.forcePower;
	}*/

	@Override
	public int getSizeInventory()
	{
		return 5;
	}

	@Override
	public TileEntitySecurityStation getLinkedSecurityStation()
	{
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 4, this.worldObj);
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
/*
	@Override
	public int getStorageMaxPower()
	{
		if ((getStackInSlot(0) != null) && (getStackInSlot(0).getItem() == ModularForceFieldSystem.itemUpgradeCapacity))
		{
			if (this.forcePower > 10000000 + 2000000 * getStackInSlot(0).stackSize)
			{
				setForcePower(10000000 + 2000000 * getStackInSlot(0).stackSize);
			}
			return 10000000 + 2000000 * getStackInSlot(0).stackSize;
		}

		if (this.forcePower > 10000000)
		{
			setForcePower(10000000);
		}
		return 10000000;
	}*/
/*
	private void checkSlots()
	{
		if (getStackInSlot(1) != null)
		{
			if (getStackInSlot(1).getItem() == ModularForceFieldSystem.itemUpgradeRange)
			{
				setTransmitRange(8 * (getStackInSlot(1).stackSize + 1));
			}
		}
		else
		{
			setTransmitRange(8);
		}

		if (getStackInSlot(2) != null)
		{
			if ((getStackInSlot(2).getItem() instanceof IForceEnergyItems))
			{
				if ((getPowerLinkMode() != 3) && (getPowerLinkMode() != 4))
					setPowerLinkMode(3);

				IForceEnergyItems ForceEnergyItem = (IForceEnergyItems) getStackInSlot(2).getItem();

				switch (getPowerLinkMode())
				{
					case 3:
						if (ForceEnergyItem.getAvailablePower(getStackInSlot(2)) < ForceEnergyItem.getMaximumPower(null))
						{
							int maxtransfer = ForceEnergyItem.getPowerTransferrate();
							int freeeamount = ForceEnergyItem.getMaximumPower(null) - ForceEnergyItem.getAvailablePower(getStackInSlot(2));

							if (getStorageAvailablePower() > 0)
							{
								if (getStorageAvailablePower() > maxtransfer)
								{
									if (freeeamount > maxtransfer)
									{
										ForceEnergyItem.setAvailablePower(getStackInSlot(2), ForceEnergyItem.getAvailablePower(getStackInSlot(2)) + maxtransfer);
										setForcePower(getStorageAvailablePower() - maxtransfer);
									}
									else
									{
										ForceEnergyItem.setAvailablePower(getStackInSlot(2), ForceEnergyItem.getAvailablePower(getStackInSlot(2)) + freeeamount);
										setForcePower(getStorageAvailablePower() - freeeamount);
									}
								}
								else if (freeeamount > getStorageAvailablePower())
								{
									ForceEnergyItem.setAvailablePower(getStackInSlot(2), ForceEnergyItem.getAvailablePower(getStackInSlot(2)) + getStorageAvailablePower());
									setForcePower(getStorageAvailablePower() - getStorageAvailablePower());
								}
								else
								{
									ForceEnergyItem.setAvailablePower(getStackInSlot(2), ForceEnergyItem.getAvailablePower(getStackInSlot(2)) + freeeamount);
									setForcePower(getStorageAvailablePower() - freeeamount);
								}

								getStackInSlot(2).setItemDamage(ForceEnergyItem.getItemDamage(getStackInSlot(2)));
							}
						}
						break;
					case 4:
						if (ForceEnergyItem.getAvailablePower(getStackInSlot(2)) > 0)
						{
							int maxtransfer = ForceEnergyItem.getPowerTransferrate();
							int freeeamount = getStorageMaxPower() - getStorageAvailablePower();
							int amountleft = ForceEnergyItem.getAvailablePower(getStackInSlot(2));

							if (freeeamount >= amountleft)
							{
								if (amountleft >= maxtransfer)
								{
									ForceEnergyItem.setAvailablePower(getStackInSlot(2), ForceEnergyItem.getAvailablePower(getStackInSlot(2)) - maxtransfer);
									setForcePower(getStorageAvailablePower() + maxtransfer);
								}
								else
								{
									ForceEnergyItem.setAvailablePower(getStackInSlot(2), ForceEnergyItem.getAvailablePower(getStackInSlot(2)) - amountleft);
									setForcePower(getStorageAvailablePower() + amountleft);
								}
							}
							else
							{
								ForceEnergyItem.setAvailablePower(getStackInSlot(2), ForceEnergyItem.getAvailablePower(getStackInSlot(2)) - freeeamount);
								setForcePower(getStorageAvailablePower() + freeeamount);
							}

							getStackInSlot(2).setItemDamage(ForceEnergyItem.getItemDamage(getStackInSlot(2)));
						}

						break;
				}

			}

			if (getStackInSlot(2).getItem() == ModularForceFieldSystem.itemCardPowerLink)
			{
				if ((getPowerLinkMode() != 0) && (getPowerLinkMode() != 1) && (getPowerLinkMode() != 2))
					setPowerLinkMode(0);
			}
		}
	}*/

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.distributionMode = nbt.getInteger("distributionMode");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("distributionMode", this.distributionMode);
	}

	/*
	private void powerTransfer()
	{
		if (hasPowerSource())
		{
			int powerTransferRate = getMaximumPower() / 120;
			int freeStorageAmount = (int) (getMaximumPower() - getForcePower());
			int balanceLevel = (int) (getStorageAvailablePower() - getForcePower());

			switch (getPowerLinkMode())
			{
				case 0:
					if ((getPercentageStorageCapacity() >= 95) && (getPercentageCapacity() != 100))
					{
						if (freeStorageAmount > powerTransferRate)
						{
							emitPower(powerTransferRate, false);
							consumePowerFromStorage(powerTransferRate, false);
						}
						else
						{
							emitPower(freeStorageAmount, false);
							consumePowerFromStorage(freeStorageAmount, false);
						}
					}
					break;
				case 1:
					if (getPercentageCapacity() < getPercentageStorageCapacity())
					{
						if (balanceLevel > powerTransferRate)
						{
							emitPower(powerTransferRate, false);
							consumePowerFromStorage(powerTransferRate, false);
						}
						else
						{
							emitPower(balanceLevel, false);
							consumePowerFromStorage(balanceLevel, false);
						}
					}
					break;
				case 2:
					if ((getStorageAvailablePower() > 0) && (getPercentageCapacity() != 100))
					{
						if (getStorageAvailablePower() > powerTransferRate)
						{
							if (freeStorageAmount > powerTransferRate)
							{
								emitPower(powerTransferRate, false);
								consumePowerFromStorage(powerTransferRate, false);
							}
							else
							{
								emitPower(freeStorageAmount, false);
								consumePowerFromStorage(freeStorageAmount, false);
							}
						}
						else if (freeStorageAmount > getStorageAvailablePower())
						{
							emitPower(getStorageAvailablePower(), false);
							consumePowerFromStorage(getStorageAvailablePower(), false);
						}
						else
						{
							emitPower(freeStorageAmount, false);
							consumePowerFromStorage(freeStorageAmount, false);
						}
					}
					break;
			}
		}
	}*/

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventory[i];
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
	public int getStartInventorySide(ForgeDirection side)
	{
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		return 0;
	}

    /*
	@Override
	public void onNetworkHandlerEvent(int key, String value)
	{
		if (key == 1)
		{
			if (getStackInSlot(2) != null)
			{
				if ((getStackInSlot(2).getItem() instanceof IForceEnergyItems))
				{
					if (getPowerLinkMode() == 4)
					{
						setPowerLinkMode(3);
					}
					else
					{
						setPowerLinkMode(4);
					}

					return;
				}
				if (getStackInSlot(2).getItem() == ModularForceFieldSystem.itemCardPowerLink)
				{
					if (getPowerLinkMode() < 2)
					{
						setPowerLinkMode(getPowerLinkMode() + 1);
					}
					else
					{
						setPowerLinkMode(0);
					}

					return;
				}
			}

			if (getPowerLinkMode() != 4)
			{
				setPowerLinkMode(getPowerLinkMode() + 1);
			}
			else
			{
				setPowerLinkMode(0);
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

		NetworkedFields.add("linkedProjector");
		NetworkedFields.add("capacity");
		NetworkedFields.add("transmissionRange");

		return NetworkedFields;
	}
    */
/*
	@Override
	public int getFreeStorageAmount()
	{
		return getStorageMaxPower() - getStorageAvailablePower();
	}

	@Override
	public boolean insertPowerToStorage(int powerAmount, boolean simulation)
	{
		if (simulation)
		{
			if (getStorageAvailablePower() + powerAmount <= getStorageMaxPower())
			{
				return true;
			}
			return false;
		}
		setForcePower(getStorageAvailablePower() + powerAmount);
		return true;
	}

	@Override
	public boolean consumePowerFromStorage(int powerAmount, boolean simulation)
	{
		if (simulation)
		{
			if (getStorageAvailablePower() >= powerAmount)
			{
				return true;
			}
			return false;
		}
		if (getStorageAvailablePower() - powerAmount >= 0)
		{
			setForcePower(getStorageAvailablePower() - powerAmount);
		}
		else
		{
			setForcePower(0);
		}
		return true;
	}*/

	@Override
	public boolean isItemValid(int slotID, ItemStack itemStack)
	{
		switch (slotID)
		{
			case 0:
				if ((itemStack.getItem() instanceof ItemUpgradeCapacity))
				{
					return true;
				}
				break;
			case 1:
				if ((itemStack.getItem() instanceof ItemUpgradeRange))
				{
					return true;
				}
				break;
			case 2:
				if (((itemStack.getItem() instanceof IForceEnergyItems)) || ((itemStack.getItem() instanceof IPowerLinkItem)))
				{
					return true;
				}
				break;
			case 4:
				if ((itemStack.getItem() instanceof ItemCardSecurityLink))
				{
					return true;
				}
				break;
			case 3:
		}
		return false;
	}
	

}