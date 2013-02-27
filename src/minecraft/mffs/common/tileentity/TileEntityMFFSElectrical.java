package mffs.common.tileentity;

import ic2.api.energy.tile.IEnergySink;

import java.util.EnumSet;

import mffs.common.MFFSConfiguration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.implement.IVoltage;
import universalelectricity.prefab.ItemElectric;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;

public abstract class TileEntityMFFSElectrical extends TileEntityMFFSInventory implements IVoltage, IPowerReceptor, IEnergySink
{
	protected IPowerProvider powerProvider;

	public TileEntityMFFSElectrical()
	{
		if (MFFSConfiguration.MODULE_BUILDCRAFT)
		{
			this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
			// this.powerProvider.configure(10, 2, (int) (getMaxWorkEnergy() / 2.5D), (int)
			// (getMaxWorkEnergy() / 2.5D), (int) (getMaxWorkEnergy() / 2.5D));
		}
	}

	@Override
	public void setPowerProvider(IPowerProvider provider)
	{
		this.powerProvider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider()
	{
		return this.powerProvider;
	}

	@Override
	public void doWork()
	{
	}

	@Override
	public int powerRequest()
	{
		// double workEnergyinMJ = getWorkEnergy() / 2.5D;
		// double MaxWorkEnergyinMj = getMaxWorkEnergy() / 2.5D;

		// return (int) Math.round(MaxWorkEnergyinMj - workEnergyinMJ);
		return 0;
	}

	/**
	 * The amount of watts received this tick. This variable should be deducted when used.
	 */
	public double prevWatts, wattsReceived = 0;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		this.prevWatts = this.wattsReceived;

		/**
		 * ElectricityManager works on server side.
		 */
		if (!this.worldObj.isRemote)
		{
			/**
			 * If the machine is disabled, stop requesting electricity.
			 */
			if (!this.isDisabled())
			{
				ElectricityPack electricityPack = ElectricityNetwork.consumeFromMultipleSides(this, this.getConsumingSides(), this.getRequest());
				this.onReceive(electricityPack);
			}
			else
			{
				ElectricityNetwork.consumeFromMultipleSides(this, new ElectricityPack());
			}
		}
	}

	/**
	 * Returns the amount of energy being requested this tick. Return an empty ElectricityPack if no
	 * electricity is desired.
	 */
	public ElectricityPack getRequest()
	{
		return new ElectricityPack();
	}

	/**
	 * The sides in which this machine can consume electricity from.
	 */
	protected EnumSet<ForgeDirection> getConsumingSides()
	{
		return ElectricityConnections.getDirections(this);
	}

	/**
	 * Called right after electricity is transmitted to the TileEntity. Override this if you wish to
	 * have another effect for a voltage overcharge.
	 * 
	 * @param electricityPack
	 */
	public void onReceive(ElectricityPack electricityPack)
	{
		/**
		 * Creates an explosion if the voltage is too high.
		 */
		if (UniversalElectricity.isVoltageSensitive)
		{
			if (electricityPack.voltage > this.getVoltage())
			{
				this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 1.5f, true);
				return;
			}
		}

		this.wattsReceived = Math.min(this.wattsReceived + electricityPack.getWatts(), this.getWattBuffer());
	}

	/**
	 * @return The amount of internal buffer that may be stored within this machine. This will make
	 * the machine run smoother as electricity might not always be consistent.
	 */
	public double getWattBuffer()
	{
		return this.getRequest().getWatts() * 2;
	}

	@Override
	public double getVoltage(Object... data)
	{
		if (UniversalElectricity.isVoltageSensitive)
		{
			return 240;
		}

		return 120;
	}
	
	public void decharge(ItemStack itemStack)
	{
		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof IItemElectric)
			{
				IItemElectric electricItem = (IItemElectric) itemStack.getItem();

				if (electricItem.canProduceElectricity())
				{
					double receivedElectricity = electricItem.onUse(Math.min(electricItem.getMaxJoules() * 0.005, this.getRequest().getWatts()), itemStack);
					this.wattsReceived += receivedElectricity;
				}
			}
		}
	}

}
