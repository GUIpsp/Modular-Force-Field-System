package mffs.common.tileentity;

import mffs.api.IModularProjector;
import mffs.common.ModularForceFieldSystem;
import mffs.common.card.ItemCardDataLink;
import mffs.common.card.ItemCardPersonalID;
import mffs.common.card.ItemCardPowerLink;
import mffs.common.card.ItemCardSecurityLink;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.prefab.TranslationHelper;

/**
 * All TileEntities that have an inventory should extend this.
 * 
 * @author Calclavia
 * 
 */
public abstract class TileEntityMFFSInventory extends TileEntityMFFS implements ISidedInventory
{
	/**
	 * The inventory of the TileEntity.
	 */
	protected ItemStack[] inventory = new ItemStack[this.getSizeInventory()];

	/**
	 * Inventory Methods
	 */
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventory[i];
	}

	@Override
	public String getInvName()
	{
		return TranslationHelper.getLocal(this.getBlockType().getBlockName() + ".name");
	}

	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
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
	public int getStartInventorySide(ForgeDirection side)
	{
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		return 1;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	public abstract boolean isItemValid(ItemStack itemStack, int slotID);

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		if (this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this)
		{
			return false;
		}

		return entityplayer.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{
		return null;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	public int countItemsInSlot(IModularProjector.Slots slt)
	{
		if (getStackInSlot(slt.slot) != null)
		{
			return getStackInSlot(slt.slot).stackSize;
		}
		return 0;
	}

	public abstract void dropPlugins();

	public void dropPlugins(int slot, IInventory inventory)
	{
		if (this.worldObj.isRemote)
		{
			setInventorySlotContents(slot, null);
			return;
		}

		if (inventory.getStackInSlot(slot) != null)
		{
			if (((inventory.getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink)) || ((inventory.getStackInSlot(slot).getItem() instanceof ItemCardPowerLink)) || ((inventory.getStackInSlot(slot).getItem() instanceof ItemCardPersonalID)) || ((inventory.getStackInSlot(slot).getItem() instanceof ItemCardDataLink)))
			{
				this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(ModularForceFieldSystem.itemCardEmpty, 1)));
			}
			else
			{
				this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, inventory.getStackInSlot(slot)));
			}

			inventory.setInventorySlotContents(slot, null);
			onInventoryChanged();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		NBTTagList nbtTagList = nbttagcompound.getTagList("Items");
		this.inventory = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < nbtTagList.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtTagList.tagAt(i);

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

		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < this.inventory.length; i++)
		{
			if (this.inventory[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inventory[i].writeToNBT(nbttagcompound1);
				nbtTagList.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbtTagList);
	}
}
