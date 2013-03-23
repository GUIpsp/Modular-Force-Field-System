package mffs.common.tileentity;

import icbm.api.RadarRegistry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mffs.api.FortronGrid;
import mffs.api.IFortronCapacitor;
import mffs.api.IFortronFrequency;
import mffs.api.IFortronStorage;
import mffs.common.ZhuYao;
import mffs.common.card.ItKaShengBuo;
import mffs.common.upgrade.ItemUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

public class TDianRong extends TileEntityFortron implements IFortronStorage, IFortronCapacitor
{
	public enum TransferMode
	{
		EQUALIZE, DISTRIBUTE, DRAIN, FILL;

		public TransferMode toggle()
		{
			int newOrdinal = this.ordinal() + 1;

			if (newOrdinal >= this.values().length)
			{
				newOrdinal = 0;
			}
			return this.values()[newOrdinal];
		}

	}

	private int transmissionRange = 20;
	private TransferMode transferMode = TransferMode.EQUALIZE;

	public TDianRong()
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

			if (this.isActive() && this.ticks % 10 == 0)
			{
				Set<IFortronFrequency> machines = this.getLinkedDevices();

				if (machines.size() > 1)
				{
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

										/**
										 * Draw beam effect
										 */
										if (machine.getFortronEnergy() != amountToSet)
										{
											if (this.worldObj.isRemote)
											{
												if (machine.getFortronEnergy() > amountToSet)
												{
													ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3((TileEntity) machine), 0.5), Vector3.add(new Vector3(this), 0.5), 0.6f, 0.6f, 1, 20);
												}
												else
												{
													ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3(this), 0.5), Vector3.add(new Vector3((TileEntity) machine), 0.5), 0.6f, 0.6f, 1, 20);
												}
											}

											machine.setFortronEnergy(amountToSet);
										}
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
										int amountToSet = (int) (totalFortron / machines.size());
										/**
										 * Draw beam effect
										 */
										if (this.worldObj.isRemote)
										{
											if (machine.getFortronEnergy() > amountToSet)
											{
												ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3(this), 0.5), Vector3.add(new Vector3((TileEntity) machine), 0.5), 0.6f, 0.6f, 1, 20);
											}
											else
											{
												ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3((TileEntity) machine), 0.5), Vector3.add(new Vector3(this), 0.5), 0.6f, 0.6f, 1, 20);
											}
										}

										machine.setFortronEnergy(amountToSet);
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
										/**
										 * Draw beam effect
										 */
										if (this.worldObj.isRemote)
										{
											if (machine.getFortronEnergy() > amountToSet)
											{
												ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3(this), 0.5), Vector3.add(new Vector3((TileEntity) machine), 0.5), 0.6f, 0.6f, 1, 20);
											}
											else
											{
												ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3((TileEntity) machine), 0.5), Vector3.add(new Vector3(this), 0.5), 0.6f, 0.6f, 1, 20);
											}
										}

										machine.setFortronEnergy(amountToSet);
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
										/**
										 * Draw beam effect
										 */
										if (this.worldObj.isRemote)
										{
											if (machine.getFortronEnergy() > amountToSet)
											{
												ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3(this), 0.5), Vector3.add(new Vector3((TileEntity) machine), 0.5), 0.6f, 0.6f, 1, 20);
											}
											else
											{
												ZhuYao.proxy.renderBeam(this.worldObj, Vector3.add(new Vector3((TileEntity) machine), 0.5), Vector3.add(new Vector3(this), 0.5), 0.6f, 0.6f, 1, 20);
											}
										}

										machine.setFortronEnergy(amountToSet);
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
	 * Packet Methods
	 */
	@Override
	public List getPacketUpdate()
	{
		List objects = new LinkedList();
		objects.addAll(super.getPacketUpdate());
		objects.add(this.transferMode.ordinal());
		objects.add(this.transmissionRange);
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		super.onReceivePacket(packetID, dataStream);

		if (packetID == 1)
		{
			this.transferMode = TransferMode.values()[dataStream.readInt()];
			this.transmissionRange = dataStream.readInt();
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

	public void setTransmitRange(int transmitRange)
	{
		this.transmissionRange = transmitRange;
		PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
	}

	public int getTransmitRange()
	{
		return this.transmissionRange;
	}

	@Override
	public int getSizeInventory()
	{
		return 4;
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
		return FortronGrid.INSTANCE.get(this.worldObj, new Vector3(this), this.transmissionRange, this.getFrequency());
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemStack)
	{
		if (slotID == 0)
		{
			return itemStack.getItem() instanceof ItKaShengBuo;
		}
		else
		{
			return itemStack.getItem() instanceof ItemUpgrade;
		}
	}

	public TransferMode getTransferMode()
	{
		return this.transferMode;
	}

}