package mffs.common.tileentity;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import mffs.api.IPowerLinkItem;
import mffs.common.FrequencyGridOld;
import mffs.common.MFFSConfiguration;
import mffs.common.container.ContainerConverter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

public class TileEntityConverter extends TileEntityFortron implements IEnergySource
{

	private int IC_Outputpacketsize;
	private int IC_Outputpacketamount;
	private int IC_Output = 0;
	private int UE_Outputvoltage;
	private int UE_Outputamp;
	private int UE_Output = 0;
	private boolean addedToEnergyNet;
	private boolean Industriecraftfound = true;
	private int linkPower;
	private int capacity;

	public TileEntityConverter()
	{
		this.inventory = new ItemStack[4];
		this.capacity = 0;
		this.IC_Outputpacketsize = 1;
		this.IC_Outputpacketamount = 1;
		this.UE_Outputvoltage = 120;
		this.UE_Outputamp = 10;
		this.addedToEnergyNet = false;
		this.linkPower = 0;
	}

	public int getUE_Outputvoltage()
	{
		return this.UE_Outputvoltage;
	}

	public void setUE_Outputvoltage(int uE_Outputvoltage)
	{
		this.UE_Outputvoltage = uE_Outputvoltage;
	}

	public int getUE_Outputamp()
	{
		return this.UE_Outputamp;
	}

	public void setUE_Outputamp(int uE_Outputamp)
	{
		this.UE_Outputamp = uE_Outputamp;
	}

	public int getUE_Output()
	{
		return this.UE_Output;
	}

	public void setUE_Output(int uE_Output)
	{
		this.UE_Output = uE_Output;
	}

	public int getIC_Output()
	{
		return this.IC_Output;
	}

	public void setIC_Output(int iC_Output)
	{
		this.IC_Output = iC_Output;
	}

	public int getIC_Outputpacketsize()
	{
		return this.IC_Outputpacketsize;
	}

	public void setIC_Outputpacketsize(int iC_Outputpacketsize)
	{
		this.IC_Outputpacketsize = iC_Outputpacketsize;
	}

	public int getIC_Outputpacketamount()
	{
		return this.IC_Outputpacketamount;
	}

	public void setIC_Outputpacketamount(int iC_Outputpacketamount)
	{
		this.IC_Outputpacketamount = iC_Outputpacketamount;
	}

	public int getLinkPower()
	{
		return this.linkPower;
	}

	public void setLinkPower(int linkPower)
	{
		this.linkPower = linkPower;
	}

	public int getFortronCapacity()
	{
		return this.capacity;
	}

	public void setCapacity(int Capacity)
	{
		this.capacity = Capacity;
	}

	@Override
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			if ((!this.addedToEnergyNet) && (this.Industriecraftfound))
			{
				try
				{
					MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
					this.addedToEnergyNet = true;
				}
				catch (Exception ex)
				{
					this.Industriecraftfound = false;
				}
			}

			if (hasPowerSource())
			{
				setLinkPower(getFortronEnergy());
			}
			else
			{
				setLinkPower(0);
			}

			if ((getStatusMode() == 1) && (!getStatusValue()) && (isPoweredByRedstone()))
			{
				onToggle();
			}
			if ((getStatusMode() == 1) && (getStatusValue()) && (!isPoweredByRedstone()))
			{
				onToggle();
			}

			if ((getStatusValue()) && (hasPowerSource()) && (!isActive()))
			{
				setActive(true);
			}
			if (((!getStatusValue()) || (!hasPowerSource())) && (isActive()))
			{
				setActive(false);
			}
			if ((isActive()) && (getIC_Output() == 1))
			{
				EmitICpower(getIC_Outputpacketsize(), getIC_Outputpacketamount());
			}

			EmitUEPower(getUE_Outputvoltage(), getUE_Outputamp());
		}

		super.updateEntity();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		try
		{
			this.IC_Outputpacketamount = nbttagcompound.getShort("ICOutputpacketamount");
		}
		catch (Exception e)
		{
			this.IC_Outputpacketamount = nbttagcompound.getInteger("ICOutputpacketamount");
		}
		this.IC_Output = nbttagcompound.getInteger("ICOutput");
		this.IC_Outputpacketsize = nbttagcompound.getInteger("ICOutputpacketsize");
		this.UE_Output = nbttagcompound.getInteger("UEOutput");
		this.UE_Outputvoltage = nbttagcompound.getInteger("UEOutputvoltage");
		this.UE_Outputamp = nbttagcompound.getInteger("UEOutputamp");

		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		this.inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);

			byte byte0 = nbttagcompound1.getByte("Slot");
			if ((byte0 >= 0) && (byte0 < this.inventory.length))
			{
				this.inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("ICOutput", this.IC_Output);
		nbttagcompound.setInteger("ICOutputpacketsize", this.IC_Outputpacketsize);
		nbttagcompound.setInteger("ICOutputpacketamount", this.IC_Outputpacketamount);

		nbttagcompound.setInteger("UEOutput", this.UE_Output);
		nbttagcompound.setInteger("UEOutputvoltage", this.UE_Outputvoltage);
		nbttagcompound.setInteger("UEOutputamp", this.UE_Outputamp);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.inventory.length; i++)
		{
			if (this.inventory[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventory[i];
	}

	@Override
	public String getInvName()
	{
		return "Extractor";
	}

	@Override
	public int getSizeInventory()
	{
		return 4;
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
	public ItemStack getStackInSlotOnClosing(int var1)
	{
		return null;
	}
	/*
	 * @Override public void onNetworkHandlerUpdate(String field) {
	 * this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord); }
	 * 
	 * @Override public void onNetworkHandlerEvent(int key, String value) { if (key == 100) { if
	 * (getIC_Output() == 0) { setIC_Output(1); } else { setIC_Output(0); } }
	 * 
	 * if (key == 101) { if (getUE_Output() == 0) { setUE_Output(1); } else { setUE_Output(0); } }
	 * 
	 * if (getIC_Output() == 0) { if (key == 200) { if (value.equalsIgnoreCase("+")) { switch
	 * (this.IC_Outputpacketsize) { case 1: this.IC_Outputpacketsize = 32; break; case 32:
	 * this.IC_Outputpacketsize = 128; break; case 128: this.IC_Outputpacketsize = 512; break; case
	 * 512: this.IC_Outputpacketsize = 2048; break; case 2048: this.IC_Outputpacketsize = 1; } } if
	 * (value.equalsIgnoreCase("-")) { switch (this.IC_Outputpacketsize) { case 1:
	 * this.IC_Outputpacketsize = 2048; break; case 32: this.IC_Outputpacketsize = 1; break; case
	 * 128: this.IC_Outputpacketsize = 32; break; case 512: this.IC_Outputpacketsize = 128; break;
	 * case 2048: this.IC_Outputpacketsize = 512; } } } if (key == 201) { if
	 * (value.equalsIgnoreCase("+")) { if (this.IC_Outputpacketamount == 9) {
	 * this.IC_Outputpacketamount = 1; } else { this.IC_Outputpacketamount += 1; } } if
	 * (value.equalsIgnoreCase("-")) { if (this.IC_Outputpacketamount == 1) {
	 * this.IC_Outputpacketamount = 9; } else { this.IC_Outputpacketamount -= 1; } } } } if
	 * (getUE_Output() == 0) { if (key == 202) { if (value.equalsIgnoreCase("+")) { switch
	 * (this.UE_Outputvoltage) { case 60: this.UE_Outputvoltage = 120; break; case 120:
	 * this.UE_Outputvoltage = 240; break; case 240: this.UE_Outputvoltage = 60; } } if
	 * (value.equalsIgnoreCase("-")) { switch (this.UE_Outputvoltage) { case 60:
	 * this.UE_Outputvoltage = 240; break; case 120: this.UE_Outputvoltage = 60; break; case 240:
	 * this.UE_Outputvoltage = 120; } } } if (key == 203) { if (value.equalsIgnoreCase("+")) { if
	 * (this.UE_Outputamp == 50) { this.UE_Outputamp = 1; } else { this.UE_Outputamp += 1; } } if
	 * (value.equalsIgnoreCase("-")) { if (this.UE_Outputamp == 1) { this.UE_Outputamp = 50; } else
	 * { this.UE_Outputamp -= 1; } }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * super.onNetworkHandlerEvent(key, value); }
	 */

	public void EmitUEPower(int volt, int amp)
	{
		if ((MFFSConfiguration.MODULE_UE) && (hasPowerSource()))
		{
			ForgeDirection outputDirection = this.getDirection(this.worldObj,this.xCoord,this.yCoord,this.zCoord);
			TileEntity outputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);
			IElectricityNetwork outputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile, outputDirection);

			if (outputNetwork != null)
			{
				double outputWatts = Math.min(outputNetwork.getRequest().getWatts(), volt * amp);

				if (consumeFortron((int) (MFFSConfiguration.ExtractorPassForceEnergyGenerate / 4000 * (outputWatts / 50.0D)), true) > 0)
				{
					if ((outputWatts > 0.0D) && (isActive()) && (getUE_Output() == 1))
					{
						outputNetwork.startProducing(this, outputWatts / volt, volt);
						consumeFortron((int) (MFFSConfiguration.ExtractorPassForceEnergyGenerate / 4000 * (outputWatts / 50.0D)), false);
					}
					else
					{
						outputNetwork.stopProducing(this);
					}
				}
			}
		}
	}

	public void EmitICpower(int amount, int packets)
	{
		if ((this.Industriecraftfound) && (hasPowerSource()))
		{
			while (packets > 0)
			{
				if (consumeFortron(MFFSConfiguration.ExtractorPassForceEnergyGenerate / 4000 * amount, true) > 0)
				{
					EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, amount);
					MinecraftForge.EVENT_BUS.post(event);
					consumeFortron(MFFSConfiguration.ExtractorPassForceEnergyGenerate / 4000 * (amount - event.amount), false);
				}
				packets--;
			}
		}
	}

	@Override
	public void invalidate()
	{
		if (this.addedToEnergyNet)
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			this.addedToEnergyNet = false;
		}
		FrequencyGridOld.getWorldMap(this.worldObj).getConverter().remove(Integer.valueOf(getDeviceID()));
		super.invalidate();
	}

	@Override
	public boolean isAddedToEnergyNet()
	{
		return this.addedToEnergyNet;
	}

	@Override
	public int getMaxEnergyOutput()
	{
		return 2147483647;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
	{
		return receiver instanceof IEnergyAcceptor;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer)
	{
		return new ContainerConverter(inventoryplayer.player, this);
	}

	@Override
	public boolean isItemValid(int slotID, ItemStack itemStack)
	{
		switch (slotID)
		{
			case 0:
				if (!(itemStack.getItem() instanceof IPowerLinkItem))
				{
					return false;
				}
				break;
		}
		return true;
	}
}