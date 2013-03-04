package mffs.common.tileentity;

import icbm.api.RadarRegistry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mffs.api.FortronGrid;
import mffs.api.IForceEnergyItems;
import mffs.api.IFortronFrequency;
import mffs.api.IFortronStorage;
import mffs.api.IPowerLinkItem;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.container.ContainerCapacitor;
import mffs.common.upgrade.ItemUpgradeCapacity;
import mffs.common.upgrade.ItemUpgradeRange;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityFortronCapacitor extends TileEntityFortron implements IFortronStorage
{
	private int distributionMode = 0;
	private int transmissionRange = 20;

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
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (!this.isDisabled())
			{
				/**
				 * Transmit Fortrons in frequency network, evenly distributing them.
				 */
				if (this.isPoweredByRedstone() && this.ticks % 20 == 0)
				{
					Set<IFortronFrequency> machines = FortronGrid.INSTANCE.get(this.worldObj, new Vector3(this), this.transmissionRange, this.getFrequency());

					/**
					 * Check spread mode. Equal, Give All, Take All
					 */
					int totalFortron = 0;
					int totalCapacity = 0;

					HashMap<IFortronFrequency, Double> distributionMap = new HashMap<IFortronFrequency, Double>();

					for (IFortronFrequency machine : machines)
					{
						if (machine != null)
						{
							totalFortron += machine.getFortronEnergy();
							totalCapacity += machine.getFortronCapacity();
						}
					}

					if (totalFortron > 0 && totalCapacity > 0)
					{
						for (IFortronFrequency machine : machines)
						{
							if (machine != null)
							{
								double capacityPercentage = (double) machine.getFortronCapacity() / (double) totalCapacity;
								int amountToSet = (int) (totalFortron * capacityPercentage);
								machine.setFortronEnergy(amountToSet);
							}
						}
					}

					this.setActive(true);
				}
				else
				{
					this.setActive(false);
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

		if (packetID == 1)
		{
			this.distributionMode = dataStream.readInt();
			this.transmissionRange = dataStream.readInt();
		}
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

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer)
	{
		return new ContainerCapacitor(inventoryplayer.player, this);
	}

	/*
	 * @Override public int getStorageAvailablePower() { return this.forcePower; }
	 */

	@Override
	public int getSizeInventory()
	{
		return 4;
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
	 * @Override public int getStorageMaxPower() { if ((getStackInSlot(0) != null) &&
	 * (getStackInSlot(0).getItem() == ModularForceFieldSystem.itemUpgradeCapacity)) { if
	 * (this.forcePower > 10000000 + 2000000 * getStackInSlot(0).stackSize) { setForcePower(10000000
	 * + 2000000 * getStackInSlot(0).stackSize); } return 10000000 + 2000000 *
	 * getStackInSlot(0).stackSize; }
	 * 
	 * if (this.forcePower > 10000000) { setForcePower(10000000); } return 10000000; }
	 */
	/*
	 * private void checkSlots() { if (getStackInSlot(1) != null) { if (getStackInSlot(1).getItem()
	 * == ModularForceFieldSystem.itemUpgradeRange) { setTransmitRange(8 *
	 * (getStackInSlot(1).stackSize + 1)); } } else { setTransmitRange(8); }
	 * 
	 * if (getStackInSlot(2) != null) { if ((getStackInSlot(2).getItem() instanceof
	 * IForceEnergyItems)) { if ((getPowerLinkMode() != 3) && (getPowerLinkMode() != 4))
	 * setPowerLinkMode(3);
	 * 
	 * IForceEnergyItems ForceEnergyItem = (IForceEnergyItems) getStackInSlot(2).getItem();
	 * 
	 * switch (getPowerLinkMode()) { case 3: if
	 * (ForceEnergyItem.getAvailablePower(getStackInSlot(2)) <
	 * ForceEnergyItem.getMaximumPower(null)) { int maxtransfer =
	 * ForceEnergyItem.getPowerTransferrate(); int freeeamount =
	 * ForceEnergyItem.getMaximumPower(null) - ForceEnergyItem.getAvailablePower(getStackInSlot(2));
	 * 
	 * if (getStorageAvailablePower() > 0) { if (getStorageAvailablePower() > maxtransfer) { if
	 * (freeeamount > maxtransfer) { ForceEnergyItem.setAvailablePower(getStackInSlot(2),
	 * ForceEnergyItem.getAvailablePower(getStackInSlot(2)) + maxtransfer);
	 * setForcePower(getStorageAvailablePower() - maxtransfer); } else {
	 * ForceEnergyItem.setAvailablePower(getStackInSlot(2),
	 * ForceEnergyItem.getAvailablePower(getStackInSlot(2)) + freeeamount);
	 * setForcePower(getStorageAvailablePower() - freeeamount); } } else if (freeeamount >
	 * getStorageAvailablePower()) { ForceEnergyItem.setAvailablePower(getStackInSlot(2),
	 * ForceEnergyItem.getAvailablePower(getStackInSlot(2)) + getStorageAvailablePower());
	 * setForcePower(getStorageAvailablePower() - getStorageAvailablePower()); } else {
	 * ForceEnergyItem.setAvailablePower(getStackInSlot(2),
	 * ForceEnergyItem.getAvailablePower(getStackInSlot(2)) + freeeamount);
	 * setForcePower(getStorageAvailablePower() - freeeamount); }
	 * 
	 * getStackInSlot(2).setItemDamage(ForceEnergyItem.getItemDamage(getStackInSlot(2))); } } break;
	 * case 4: if (ForceEnergyItem.getAvailablePower(getStackInSlot(2)) > 0) { int maxtransfer =
	 * ForceEnergyItem.getPowerTransferrate(); int freeeamount = getStorageMaxPower() -
	 * getStorageAvailablePower(); int amountleft =
	 * ForceEnergyItem.getAvailablePower(getStackInSlot(2));
	 * 
	 * if (freeeamount >= amountleft) { if (amountleft >= maxtransfer) {
	 * ForceEnergyItem.setAvailablePower(getStackInSlot(2),
	 * ForceEnergyItem.getAvailablePower(getStackInSlot(2)) - maxtransfer);
	 * setForcePower(getStorageAvailablePower() + maxtransfer); } else {
	 * ForceEnergyItem.setAvailablePower(getStackInSlot(2),
	 * ForceEnergyItem.getAvailablePower(getStackInSlot(2)) - amountleft);
	 * setForcePower(getStorageAvailablePower() + amountleft); } } else {
	 * ForceEnergyItem.setAvailablePower(getStackInSlot(2),
	 * ForceEnergyItem.getAvailablePower(getStackInSlot(2)) - freeeamount);
	 * setForcePower(getStorageAvailablePower() + freeeamount); }
	 * 
	 * getStackInSlot(2).setItemDamage(ForceEnergyItem.getItemDamage(getStackInSlot(2))); }
	 * 
	 * break; }
	 * 
	 * }
	 * 
	 * if (getStackInSlot(2).getItem() == ModularForceFieldSystem.itemCardPowerLink) { if
	 * ((getPowerLinkMode() != 0) && (getPowerLinkMode() != 1) && (getPowerLinkMode() != 2))
	 * setPowerLinkMode(0); } } }
	 */

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
	 * private void powerTransfer() { if (hasPowerSource()) { int powerTransferRate =
	 * getMaximumPower() / 120; int freeStorageAmount = (int) (getMaximumPower() - getForcePower());
	 * int balanceLevel = (int) (getStorageAvailablePower() - getForcePower());
	 * 
	 * switch (getPowerLinkMode()) { case 0: if ((getPercentageStorageCapacity() >= 95) &&
	 * (getPercentageCapacity() != 100)) { if (freeStorageAmount > powerTransferRate) {
	 * emitPower(powerTransferRate, false); consumePowerFromStorage(powerTransferRate, false); }
	 * else { emitPower(freeStorageAmount, false); consumePowerFromStorage(freeStorageAmount,
	 * false); } } break; case 1: if (getPercentageCapacity() < getPercentageStorageCapacity()) { if
	 * (balanceLevel > powerTransferRate) { emitPower(powerTransferRate, false);
	 * consumePowerFromStorage(powerTransferRate, false); } else { emitPower(balanceLevel, false);
	 * consumePowerFromStorage(balanceLevel, false); } } break; case 2: if
	 * ((getStorageAvailablePower() > 0) && (getPercentageCapacity() != 100)) { if
	 * (getStorageAvailablePower() > powerTransferRate) { if (freeStorageAmount > powerTransferRate)
	 * { emitPower(powerTransferRate, false); consumePowerFromStorage(powerTransferRate, false); }
	 * else { emitPower(freeStorageAmount, false); consumePowerFromStorage(freeStorageAmount,
	 * false); } } else if (freeStorageAmount > getStorageAvailablePower()) {
	 * emitPower(getStorageAvailablePower(), false);
	 * consumePowerFromStorage(getStorageAvailablePower(), false); } else {
	 * emitPower(freeStorageAmount, false); consumePowerFromStorage(freeStorageAmount, false); } }
	 * break; } } }
	 */

	/*
	 * @Override public void onNetworkHandlerEvent(int key, String value) { if (key == 1) { if
	 * (getStackInSlot(2) != null) { if ((getStackInSlot(2).getItem() instanceof IForceEnergyItems))
	 * { if (getPowerLinkMode() == 4) { setPowerLinkMode(3); } else { setPowerLinkMode(4); }
	 * 
	 * return; } if (getStackInSlot(2).getItem() == ModularForceFieldSystem.itemCardPowerLink) { if
	 * (getPowerLinkMode() < 2) { setPowerLinkMode(getPowerLinkMode() + 1); } else {
	 * setPowerLinkMode(0); }
	 * 
	 * return; } }
	 * 
	 * if (getPowerLinkMode() != 4) { setPowerLinkMode(getPowerLinkMode() + 1); } else {
	 * setPowerLinkMode(0); } }
	 * 
	 * super.onNetworkHandlerEvent(key, value); }
	 * 
	 * @Override public List getFieldsForUpdate() { List NetworkedFields = new LinkedList();
	 * NetworkedFields.clear();
	 * 
	 * NetworkedFields.addAll(super.getFieldsForUpdate());
	 * 
	 * NetworkedFields.add("linkedProjector"); NetworkedFields.add("capacity");
	 * NetworkedFields.add("transmissionRange");
	 * 
	 * return NetworkedFields; }
	 */
	/*
	 * @Override public int getFreeStorageAmount() { return getStorageMaxPower() -
	 * getStorageAvailablePower(); }
	 * 
	 * @Override public boolean insertPowerToStorage(int powerAmount, boolean simulation) { if
	 * (simulation) { if (getStorageAvailablePower() + powerAmount <= getStorageMaxPower()) { return
	 * true; } return false; } setForcePower(getStorageAvailablePower() + powerAmount); return true;
	 * }
	 * 
	 * @Override public boolean consumePowerFromStorage(int powerAmount, boolean simulation) { if
	 * (simulation) { if (getStorageAvailablePower() >= powerAmount) { return true; } return false;
	 * } if (getStorageAvailablePower() - powerAmount >= 0) {
	 * setForcePower(getStorageAvailablePower() - powerAmount); } else { setForcePower(0); } return
	 * true; }
	 */

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