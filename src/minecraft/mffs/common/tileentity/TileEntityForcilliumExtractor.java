package mffs.common.tileentity;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileUnloadEvent;

import java.util.LinkedList;
import java.util.List;

import mffs.api.IPowerLinkItem;
import mffs.common.Fortron;
import mffs.common.FrequencyGrid;
import mffs.common.ModularForceFieldSystem;
import mffs.common.container.ContainerForcilliumExtractor;
import mffs.common.item.ItemForcillium;
import mffs.common.item.ItemForcilliumCell;
import mffs.common.upgrade.ItemUpgradeBooster;
import mffs.common.upgrade.ItemUpgradeCapacity;
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
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;

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
	public static final int REQUIRED_TIME = 20 * 20;
	public static final int CAPACITY = 50 * LiquidContainerRegistry.BUCKET_VOLUME;
	public int processTime = 0;

	public LiquidTank fortronTank = new LiquidTank(Fortron.LIQUID_FORTRON, CAPACITY, this);

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
							int runTime = REQUIRED_TIME;

							/**
							 * Speed upgrade will reduce the time required. TODO: Add more upgrade
							 * ability.
							 */
							if (getStackInSlot(3) != null)
							{
								if (this.getStackInSlot(3).itemID == ModularForceFieldSystem.itemUpgradeBoost.itemID)
								{
									runTime /= 2;
								}
							}

							this.processTime = runTime;
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

				if (this.ticks % 4 == 0 && this.playersUsing > 0)
				{
					PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
				}
			}
		}
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
			if (this.isItemValid(this.getStackInSlot(0), 0))
			{
				return Fortron.getAmount(this.fortronTank) < this.fortronTank.getCapacity();
			}
		}

		return false;
	}

	private void use()
	{
		if (this.canUse())
		{
			this.fortronTank.fill(Fortron.getFortron(750 + this.worldObj.rand.nextInt(500)), true);
			this.decrStackSize(0, 1);
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
		if (itemStack != null)
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
		}
		return false;
	}

	/**
	 * Liquid Functions.
	 */
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