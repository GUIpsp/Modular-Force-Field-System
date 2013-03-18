package mffs.common.tileentity;

import mffs.common.FrequencyGridOld;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.container.ContainerSecStorage;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntitySecStorage extends TileEntityMFFSInventory implements ISidedInventory, IInventory
{

	private ItemStack[] inventory;

	public TileEntitySecStorage()
	{
		this.inventory = new ItemStack[60];
	}

	@Override
	public TAnQuan getLinkedSecurityStation()
	{
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 0, this.worldObj);
	}

	@Override
	public void invalidate()
	{
		FrequencyGridOld.getWorldMap(this.worldObj).getSecStorage().remove(Integer.valueOf(getDeviceID()));
		super.invalidate();
	}

	public int getSecStation_ID()
	{
		TAnQuan sec = getLinkedSecurityStation();
		if (sec != null)
		{
			return sec.getDeviceID();
		}
		return 0;
	}

	public short getMaxSwitchMode()
	{
		return 3;
	}

	public short getMinSwitchMode()
	{
		return 2;
	}

	public int getfreeslotcount()
	{
		int count = 0;

		for (int a = 1; a < this.inventory.length; a++)
		{
			if (getStackInSlot(a) == null)
			{
				count++;
			}
		}
		return count;
	}

	@Override
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			if ((getLinkedSecurityStation() != null) && (!isActive()) && (getStatusValue()))
			{
				setActive(true);
			}
			if (((getLinkedSecurityStation() == null) || (!getStatusValue())) && (isActive()))
			{
				setActive(false);
			}
		}
		super.updateEntity();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
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
		return "SecStation";
	}

	@Override
	public int getSizeInventory()
	{
		return 60;
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.inventory[par1] = par2ItemStack;

		if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit()))
		{
			par2ItemStack.stackSize = getInventoryStackLimit();
		}

		onInventoryChanged();
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
	public int getStartInventorySide(ForgeDirection side)
	{
		if (isActive())
		{
			return 0;
		}
		return 1;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		if (isActive())
		{
			return 0;
		}
		return 54;
	}
	@Override
	public boolean isItemValid(int slotID, ItemStack itemStack)
	{
		switch (slotID)
		{
			case 0:
				if (!(itemStack.getItem() instanceof ItemCardSecurityLink))
				{
					return false;
				}
				break;
		}
		return true;
	}
}