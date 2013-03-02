package mffs.common.tileentity;

import java.util.LinkedList;
import java.util.List;

import mffs.api.IPowerLinkItem;
import mffs.common.Fortron;
import mffs.common.ModularForceFieldSystem;
import mffs.common.container.ContainerForcilliumExtractor;
import mffs.common.item.ItemForcillium;
import mffs.common.item.ItemForcilliumCell;
import mffs.common.upgrade.ItemUpgradeBooster;
import mffs.common.upgrade.ItemUpgradeCapacity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidContainerRegistry;
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
public class TileEntityForcilliumExtractor extends TileEntityMFFSElectrical
{
	/**
	 * The amount of watts this machine uses.
	 */
	public static final int WATTAGE = 1000;
	public static final int REQUIRED_TIME = 20 * 15;
	public int processTime = 0;

	public TileEntityForcilliumExtractor()
	{
		this.fortronTank.setCapacity(10 * LiquidContainerRegistry.BUCKET_VOLUME);
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
				 * Spread Fortron to nearby Fortron Capacitors
				 */
				for (int i = 0; i < 6; i++)
				{
					TileEntity tileEntity = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), ForgeDirection.getOrientation(i));

					if (tileEntity instanceof TileEntityFortronCapacitor)
					{
						// TODO:Finish this.
						// ((TileEntityFortronCapacitor)tileEntity)

					}
				}

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
		if (this.canUse() && !this.isPoweredByRedstone())
		{
			return new ElectricityPack(WATTAGE / this.getVoltage(), this.getVoltage());
		}

		return super.getRequest();
	}

	@Override
	public boolean isActive()
	{
		return !this.isPoweredByRedstone();
	}

	public boolean canUse()
	{
		if (!this.isDisabled())
		{
			if (this.isItemValid(0, this.getStackInSlot(0)))
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
		objects.add(this.processTime);
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		super.onReceivePacket(packetID, dataStream);
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
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("processTime", this.processTime);
	}

	@Override
	public boolean isItemValid(int slotID, ItemStack itemStack)
	{
		if (itemStack != null)
		{
			switch (slotID)
			{
				case 0:
					if (itemStack.getItem() instanceof ItemForcillium || itemStack.isItemEqual(new ItemStack(Item.dyePowder, 1, 4)))
						return true;
				case 1:
					if (itemStack.getItem() instanceof IPowerLinkItem)
						return true;
				case 2:
					if (itemStack.getItem() instanceof ItemUpgradeCapacity)
						return true;
				case 3:
					if (itemStack.getItem() instanceof ItemUpgradeBooster)
						return true;
			}
		}

		return false;
	}
}