package mffs.common.tileentity;

import icbm.api.RadarRegistry;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mffs.api.FortronGrid;
import mffs.api.ICard;
import mffs.api.IFortronCapacitor;
import mffs.api.IFortronFrequency;
import mffs.api.IFortronStorage;
import mffs.api.IItemFortronStorage;
import mffs.api.IModule;
import mffs.common.ZhuYao;
import mffs.common.card.ItKaLian;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

public class TDianRong extends TModuleAcceptor implements IFortronStorage, IFortronCapacitor
{
	public enum TransferMode
	{
		EQUALIZE, DISTRIBUTE, DRAIN, FILL;

		public TransferMode toggle()
		{
			int newOrdinal = this.ordinal() + 1;

			if (newOrdinal >= TransferMode.values().length)
			{
				newOrdinal = 0;
			}
			return TransferMode.values()[newOrdinal];
		}

	}

	private TransferMode transferMode = TransferMode.EQUALIZE;

	public TDianRong()
	{
		this.fortronTank.setCapacity(500 * LiquidContainerRegistry.BUCKET_VOLUME);
		this.startModuleIndex = 2;
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

		if (!this.isDisabled())
		{
			/**
			 * Transmit Fortrons in frequency network, evenly distributing them.
			 */
			if (!this.worldObj.isRemote)
			{
				if (this.isPoweredByRedstone())
				{
					if (!this.isActive())
					{
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
			}

			this.fortronTank.setCapacity((this.getModuleCount(ZhuYao.itMRongLiang) * 10 + 500) * LiquidContainerRegistry.BUCKET_VOLUME);

			/**
			 * Gets the card.
			 */
			ItemStack itemStack = this.getStackInSlot(0);
			Vector3 linkPosition = null;

			if (itemStack != null)
			{
				if (itemStack.getItem() instanceof IItemFortronStorage)
				{
					int fortron = ((IItemFortronStorage) itemStack.getItem()).getFortronEnergy(itemStack);
					fortron = Math.max(fortron - this.provideFortron(fortron, true), 0);
					((IItemFortronStorage) itemStack.getItem()).setFortronEnergy(fortron, itemStack);
				}
				else if (itemStack.getItem() instanceof ItKaLian)
				{
					linkPosition = ((ItKaLian) itemStack.getItem()).getLink(itemStack);

					if (linkPosition != null && !(linkPosition.getTileEntity(this.worldObj) instanceof IFortronFrequency))
					{
						linkPosition = null;
					}
				}
			}

			if (this.isActive() && this.ticks % 10 == 0)
			{
				Set<IFortronFrequency> machines = new HashSet<IFortronFrequency>();

				if (linkPosition != null)
				{
					IFortronFrequency machine = (IFortronFrequency) linkPosition.getTileEntity(this.worldObj);
					machines.add(this);
					machines.add(machine);
				}
				else
				{
					machines = this.getLinkedDevices();
				}

				if (machines.size() > 1)
				{
					/**
					 * Check spread mode. Equal, Give All, Take All
					 */
					int totalFortron = 0;
					int totalCapacity = 0;

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
						switch (this.transferMode)
						{
							case EQUALIZE:
							{
								for (IFortronFrequency machine : machines)
								{
									if (machine != null)
									{
										double capacityPercentage = (double) machine.getFortronCapacity() / (double) totalCapacity;
										int amountToSet = (int) (totalFortron * capacityPercentage);
										this.chuanBuo(machine, amountToSet - machine.getFortronEnergy());
									}
								}

								break;
							}
							case DISTRIBUTE:
							{
								for (IFortronFrequency machine : machines)
								{
									if (machine != null)
									{
										int amountToSet = totalFortron / machines.size();
										this.chuanBuo(machine, amountToSet - machine.getFortronEnergy());
									}
								}

								break;
							}
							case DRAIN:
							{
								int remainingFortron = totalFortron;

								for (IFortronFrequency machine : machines)
								{
									if (machine != null)
									{
										double capacityPercentage = (double) machine.getFortronCapacity() / (double) totalCapacity;
										int amountToSet = (int) (totalFortron * capacityPercentage);
										this.chuanBuo(machine, amountToSet - machine.getFortronEnergy());
										remainingFortron -= amountToSet;
									}
								}
								break;
							}
							case FILL:
							{
								/**
								 * Take total fortron energy and consume it, then distribute the
								 * rest.
								 */
								// Remove this capacitor from the list.
								totalFortron -= this.getFortronEnergy();
								totalCapacity -= this.getFortronCapacity();
								int remainingFortron = totalFortron - this.provideFortron(totalFortron, true);

								for (IFortronFrequency machine : machines)
								{
									if (machine != null && machine != this)
									{
										double capacityPercentage = (double) machine.getFortronCapacity() / (double) totalCapacity;
										int amountToSet = (int) (remainingFortron * capacityPercentage);
										this.chuanBuo(machine, amountToSet - machine.getFortronEnergy());
									}
								}
								break;
							}
						}
					}
				}
			}
		}

		if (!this.worldObj.isRemote)
		{
			/**
			 * Packet Update for Client only when GUI is open.
			 */
			if (this.ticks % 4 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
			}
		}
	}

	/**
	 * Tries to transfer fortron to a specific machine from this capacitor.
	 */
	private void chuanBuo(IFortronFrequency machine, int joules)
	{
		if (machine != null)
		{
			if (joules > 0)
			{
				// Transfer energy to machine.
				joules = Math.min(joules, this.getTransmissionRate());
				int toBeInjected = machine.provideFortron(this.requestFortron(joules, false), false);
				toBeInjected = this.requestFortron(machine.provideFortron(toBeInjected, true), true);

				// Draw Beam Effect
				if (this.worldObj.isRemote && toBeInjected > 0)
				{
					ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3(this), 0.5), Vector3.add(new Vector3((TileEntity) machine), 0.5), 0.6f, 0.6f, 1, 20);
				}
			}
			else
			{
				// Take energy from machine.
				joules = Math.min(Math.abs(joules), this.getTransmissionRate());
				int toBeEjected = this.provideFortron(machine.requestFortron(joules, false), false);
				toBeEjected = machine.requestFortron(this.provideFortron(toBeEjected, true), true);

				// Draw Beam Effect
				if (this.worldObj.isRemote && toBeEjected > 0)
				{
					ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3((TileEntity) machine), 0.5), Vector3.add(new Vector3(this), 0.5), 0.6f, 0.6f, 1, 20);
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
		objects.add(this.transferMode.ordinal());
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		super.onReceivePacket(packetID, dataStream);

		if (packetID == 1)
		{
			this.transferMode = TransferMode.values()[dataStream.readInt()];
		}
		else if (packetID == 3)
		{
			this.transferMode = this.transferMode.toggle();
		}
	}

	@Override
	public void invalidate()
	{
		RadarRegistry.unregister(this);
		super.invalidate();
	}

	@Override
	public int getSizeInventory()
	{
		return 5;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.transferMode = TransferMode.values()[nbt.getInteger("transferMode")];
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("transferMode", this.transferMode.ordinal());
	}

	@Override
	public Set<IFortronFrequency> getLinkedDevices()
	{
		return FortronGrid.INSTANCE.get(this.worldObj, new Vector3(this), this.getTransmissionRange(), this.getFrequency());
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemStack)
	{
		if (slotID == 0)
		{
			return itemStack.getItem() instanceof ICard;
		}
		else
		{
			return itemStack.getItem() instanceof IModule;
		}
	}

	public TransferMode getTransferMode()
	{
		return this.transferMode;
	}

	@Override
	public int getTransmissionRange()
	{
		return 15 + this.getModuleCount(ZhuYao.itMJuLi);
	}

	@Override
	public int getTransmissionRate()
	{
		return 200 + 50 * this.getModuleCount(ZhuYao.itMSuDu);
	}
}