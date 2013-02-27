package mffs.common.tileentity;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import mffs.api.IPowerLinkItem;
import mffs.common.FrequencyGrid;
import mffs.common.Fortron;
import mffs.common.MFFSConfiguration;
import mffs.common.ModularForceFieldSystem;
import mffs.common.container.ContainerForcilliumExtractor;
import mffs.common.item.ItemForcillium;
import mffs.common.item.ItemForcilliumCell;
import mffs.common.upgrade.ItemUpgradeBooster;
import mffs.common.upgrade.ItemUpgradeCapacity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import buildcraft.api.power.IPowerProvider;

import com.google.common.io.ByteArrayDataInput;

/**
 * A TileEntity that extract forcillium into fortrons.
 * 
 * @author Calclavia
 * 
 */
public class TileEntityForcilliumExtractor extends TileEntityMFFSElectrical implements ITankContainer
{
	/**
	 * The amount of watts this machine uses.
	 */
	public static final int WATTAGE = 1000;
	public static final int TOTAL_TIME = 20 * 20;
	public int processTime = 0;

	public LiquidTank fortronTank = new LiquidTank(Fortron.LIQUID_FORTRON, 250 * LiquidContainerRegistry.BUCKET_VOLUME, this);

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (!this.isDisabled())
			{
				if (this.canUse())
				{
					if (this.wattsReceived >= this.WATTAGE)
					{
						if (this.processTime == 0)
						{
							this.processTime = TOTAL_TIME;
						}

						if (this.processTime > 0)
						{
							this.processTime--;

							if (this.processTime < 1)
							{
								this.use();
								this.processTime = 0;
							}
						}
						else
						{
							this.processTime = 0;
						}

						this.wattsReceived -= WATTAGE;
					}
				}
				else
				{
					this.processTime = 0;
				}

				if (this.ticks % 3 == 0 && this.playersUsing > 0)
				{
					PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
				}
			}
		}

		/*
		 * if (!this.worldObj.isRemote) { if ((this.getSwitchMode() == 1) && (!getSwitchValue()) &&
		 * (isPoweredByRedstone())) { onSwitch(); } if ((this.getSwitchMode() == 1) &&
		 * (getSwitchValue()) && (!isPoweredByRedstone())) { onSwitch(); }
		 * 
		 * if ((!isActive()) && (getSwitchValue())) { setActive(true); } if ((isActive()) &&
		 * (!getSwitchValue())) { setActive(false); }
		 * 
		 * if (isActive()) { if (MFFSConfiguration.MODULE_BUILDCRAFT) { convertMJtoWorkEnergy(); }
		 * if (MFFSConfiguration.MODULE_UE) { convertUEtoWorkEnergy(); } }
		 * 
		 * if (this.ticks % getWorkTicker() == 0) { this.checkSlots();
		 * 
		 * if ((this.workmode == 0) && (isActive())) { if (getWorkDone() != getWorkEnergy() * 100 /
		 * getMaxWorkEnergy()) { setWorkDone(getWorkEnergy() * 100 / getMaxWorkEnergy()); } if
		 * (getWorkDone() > 100) { setWorkDone(100); }
		 * 
		 * if (getCapacity() != getForceEnergybuffer() * 100 / getMaxForceEnergyBuffer()) {
		 * setCapacity(getForceEnergybuffer() * 100 / getMaxForceEnergyBuffer()); }
		 * 
		 * if ((hasFreeForceEnergyStorage()) && (hasStuffToConvert())) { if (hasPowerToConvert()) {
		 * setWorkCylce(getWorkCycle() - 1); setForceEnergyBuffer(getForceEnergybuffer() +
		 * MFFSConfiguration.ExtractorPassForceEnergyGenerate); }
		 * 
		 * }
		 * 
		 * transferForceEnergy(); }
		 * 
		 * if ((this.workmode == 1) && (isActive())) { if (getWorkDone() != getWorkEnergy() * 100 /
		 * getMaxWorkEnergy()) { setWorkDone(getWorkEnergy() * 100 / getMaxWorkEnergy()); } if
		 * (((ItemForcilliumCell) getStackInSlot(4).getItem()).getForceciumlevel(getStackInSlot(4))
		 * < ((ItemForcilliumCell) getStackInSlot(4).getItem()).getMaxForceciumlevel()) { if
		 * ((hasPowerToConvert()) && (isActive())) { ((ItemForcilliumCell)
		 * getStackInSlot(4).getItem()).setForceciumlevel(getStackInSlot(4), ((ItemForcilliumCell)
		 * getStackInSlot(4).getItem()).getForceciumlevel(getStackInSlot(4)) + 1); } } } }
		 * 
		 * }
		 */
	}

	@Override
	public int getSizeInventory()
	{
		return 5;
	}

	@Override
	public ElectricityPack getRequest()
	{
		if (this.canUse())
		{
			return new ElectricityPack(WATTAGE / this.getVoltage(), this.getVoltage());
		}

		return super.getRequest();
	}

	public boolean canUse()
	{
		if (!this.isDisabled())
		{
			if (this.getStackInSlot(0) != null)
			{
				if (this.getStackInSlot(0).itemID == ModularForceFieldSystem.itemForcillium.itemID)
				{
					return true;
				}
			}
		}

		return false;
	}

	private void use()
	{
		if (this.canUse())
		{
			this.fortronTank.fill(Fortron.getFortron(100), true);
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
		objects.add(Fortron.getAmount(this.fortronTank.getLiquid()));
		objects.add(this.processTime);
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		super.onReceivePacket(packetID, dataStream);
		this.fortronTank.setLiquid(Fortron.getFortron(dataStream.readInt()));
		this.processTime = dataStream.readInt();
		System.out.println(processTime);
	}

	/*
	 * public void checkSlots() { if (getStackInSlot(0) != null) { if (getStackInSlot(0).getItem()
	 * == ModularForceFieldSystem.itemForcilliumCell) { this.workmode = 1; setMaxWorkEnergy(200000);
	 * } } else { this.workmode = 0; setMaxWorkEnergy(4000); }
	 * 
	 * if (getStackInSlot(2) != null) { if (getStackInSlot(2).getItem() ==
	 * ModularForceFieldSystem.itemUpgradeCapacity) { setMaxForceEnergyBuffer(1000000 +
	 * getStackInSlot(2).stackSize * 100000); } else { setMaxForceEnergyBuffer(1000000); } } else {
	 * setMaxForceEnergyBuffer(1000000); }
	 * 
	 * if (getStackInSlot(3) != null) { if (getStackInSlot(3).getItem() ==
	 * ModularForceFieldSystem.itemUpgradeBoost) { setWorkTicker(20 - getStackInSlot(3).stackSize);
	 * } else { setWorkTicker(20); } } else { setWorkTicker(20); } }
	 * 
	 * private boolean hasPowerToConvert() { if (this.workEnergy >= this.maxWorkEnergy - 1) {
	 * setWorkEnergy(0); return true; } return false; }
	 * 
	 * private boolean hasFreeForceEnergyStorage() { if (this.maxForceEnergyBuffer >
	 * this.forceEnergyBuffer) { return true; } return false; }
	 * 
	 * private boolean hasStuffToConvert() { if (this.workCycle > 0) return true;
	 * 
	 * if (MFFSConfiguration.adventureMap) {
	 * setMaxWorkCycle(MFFSConfiguration.forceciumCellWorkCycle); setWorkCylce(getMaxWorkCycle());
	 * return true; }
	 * 
	 * if (getStackInSlot(0) != null) { if (getStackInSlot(0).getItem() ==
	 * ModularForceFieldSystem.itemForcillium) {
	 * setMaxWorkCycle(MFFSConfiguration.ForcilliumWorkCylce); setWorkCylce(getMaxWorkCycle());
	 * decrStackSize(0, 1); return true; }
	 * 
	 * if ((getStackInSlot(0).getItem() == ModularForceFieldSystem.itemForcilliumCell) &&
	 * (((ItemForcilliumCell) getStackInSlot(0).getItem()).useForcecium(1, getStackInSlot(0)))) {
	 * setMaxWorkCycle(MFFSConfiguration.forceciumCellWorkCycle); setWorkCylce(getMaxWorkCycle());
	 * return true; }
	 * 
	 * }
	 * 
	 * return false; }
	 * 
	 * public void transferForceEnergy() {/* if (getForceEnergybuffer() > 0) { if (hasPowerSource())
	 * { int powerTransferRate = getMaximumPower() / 120; int freeAmount = (int) (getMaximumPower()
	 * - getForcePower());
	 * 
	 * if (getForceEnergybuffer() > freeAmount) { if (freeAmount > powerTransferRate) {
	 * emitPower(powerTransferRate, false); setForceEnergyBuffer(getForceEnergybuffer() -
	 * powerTransferRate); } else { emitPower(freeAmount, false);
	 * setForceEnergyBuffer(getForceEnergybuffer() - freeAmount); } } else if (freeAmount >
	 * getForceEnergybuffer()) { emitPower(getForceEnergybuffer(), false);
	 * setForceEnergyBuffer(getForceEnergybuffer() - getForceEnergybuffer()); } else {
	 * emitPower(freeAmount, false); setForceEnergyBuffer(getForceEnergybuffer() - freeAmount); } }
	 * }
	 * 
	 * }
	 */

	public short getMaxSwitchMode()
	{
		return 3;
	}

	public short getMinSwitchMode()
	{
		return 1;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer)
	{
		return new ContainerForcilliumExtractor(inventoryplayer.player, this);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.processTime = nbt.getInteger("processTime");
		this.fortronTank.setLiquid(LiquidStack.loadLiquidStackFromNBT(nbt.getCompoundTag("fortron")));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("processTime", this.processTime);

		if (this.fortronTank.getLiquid() != null)
		{
			NBTTagCompound fortronCompound = new NBTTagCompound();
			this.fortronTank.getLiquid().writeToNBT(fortronCompound);
			nbt.setTag("fortron", fortronCompound);
		}

	}

	@Override
	public boolean isItemValid(ItemStack itemStack, int slot)
	{
		switch (slot)
		{
			case 0:
				if ((((itemStack.getItem() instanceof ItemForcillium)) || ((itemStack.getItem() instanceof ItemForcilliumCell))) && (getStackInSlot(4) == null))
					return true;

				break;
			case 1:
				if ((itemStack.getItem() instanceof IPowerLinkItem))
					return true;

				break;
			case 2:
				if ((itemStack.getItem() instanceof ItemUpgradeCapacity))
					return true;

				break;
			case 3:
				if ((itemStack.getItem() instanceof ItemUpgradeBooster))
					return true;

				break;
			case 4:
				if (((itemStack.getItem() instanceof ItemForcilliumCell)) && (getStackInSlot(0) == null))
					return true;

				break;
		}
		return false;
	}

	@Override
	public void invalidate()
	{

		MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

		FrequencyGrid.getWorldMap(this.worldObj).getExtractor().remove(Integer.valueOf(getDeviceID()));

		super.invalidate();
	}

	@Override
	public boolean isAddedToEnergyNet()
	{
		return this.ticks > 0;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity tileentity, Direction direction)
	{
		return true;
	}

	/*
	 * @Override public ItemStack getPowerLinkStack() { return getStackInSlot(getPowerLinkSlot()); }
	 * 
	 * @Override public int getPowerLinkSlot() { return 1; }
	 */

	@Override
	public TileEntitySecurityStation getLinkedSecurityStation()
	{/*
	 * TileEntityCapacitor cap = (TileEntityCapacitor)
	 * FrequencyGrid.getWorldMap(this.worldObj).getCapacitor
	 * ().get(Integer.valueOf(getPowerSourceID())); if (cap != null) { TileEntitySecurityStation sec
	 * = cap.getLinkedSecurityStation(); if (sec != null) return sec; }
	 */

		return null;
	}

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return this.fortronTank.drain(maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain)
	{
		return this.drain(ForgeDirection.getOrientation(tankIndex), maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction)
	{
		return new ILiquidTank[] { this.fortronTank };
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type)
	{
		return this.fortronTank;
	}
}