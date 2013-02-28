package mffs.common.tileentity;

import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.List;
import mffs.common.FrequencyGrid;
import mffs.common.MFFSConfiguration;
import mffs.common.ModularForceFieldSystem;
import mffs.common.NBTTagCompoundHelper;
import mffs.common.SecurityRight;
import mffs.common.card.ItemAccessCard;
import mffs.common.card.ItemCardPersonalID;
import mffs.common.card.ItemCardPowerLink;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.container.ContainerSecurityStation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;

public class TileEntitySecurityStation extends TileEntityMFFSInventory
{

	private String MainUser = "";
	private ItemStack[] inventory;

	public TileEntitySecurityStation()
	{
		this.inventory = new ItemStack[11];
	}

	@Override
	public void dropPlugins()
	{
		for (int a = 0; a < this.inventory.length; a++)
		{
			dropplugins(a);
		}
	}

	public String getMainUser()
	{
		return this.MainUser;
	}

	public void setMainUser(String s)
	{
		if (!this.MainUser.equals(s))
		{
			this.MainUser = s;
			//NetworkHandlerServer.updateTileEntityField(this, "MainUser");
		}
	}

	public void dropplugins(int slot)
	{
		if (getStackInSlot(slot) != null)
		{
			if (((getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink)) || ((getStackInSlot(slot).getItem() instanceof ItemCardPowerLink)) || ((getStackInSlot(slot).getItem() instanceof ItemCardPersonalID)))
			{
				this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(ModularForceFieldSystem.itemCardEmpty, 1)));
			}
			else
			{
				this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, getStackInSlot(slot)));
			}

			setInventorySlotContents(slot, null);
			onInventoryChanged();
		}
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer)
	{
		return new ContainerSecurityStation(inventoryplayer.player, this);
	}

	@Override
	public void invalidate()
	{
		FrequencyGrid.getWorldMap(this.worldObj).getSecStation().remove(Integer.valueOf(getDeviceID()));
		super.invalidate();
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
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 10 == 0)
			{
				if (!getMainUser().equals(""))
				{
					if (isActive() != true)
					{
						setActive(true);
					}
				}
				else if (isActive())
				{
					setActive(false);
				}

				checkslots();
			}
		}

		super.updateEntity();
	}

	public void checkslots() {
		if (getStackInSlot(0) != null)
		{
			if (getStackInSlot(0).getItem() == ModularForceFieldSystem.itemCardID)
			{
				ItemCardPersonalID Card = (ItemCardPersonalID) getStackInSlot(0).getItem();

				String name = Card.getUsername(getStackInSlot(0));

				if (!getMainUser().equals(name))
				{
					setMainUser(name);
				}

				if (ItemCardPersonalID.hasRight(getStackInSlot(0), SecurityRight.CSR) != true)
				{
					ItemCardPersonalID.setRight(getStackInSlot(0), SecurityRight.CSR, true);
				}
			}
			else
			{
				setMainUser("");
			}
		}
		else
		{
			setMainUser("");
		}

	}

	@Override
	public int getSizeInventory()
	{
		return 11;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventory[i];
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
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
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.inventory[i] = itemstack;
		if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit()))
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return LanguageRegistry.instance().getStringLocalization("tile.securityStation.name");
	}

	public boolean RemoteInventory(String username, SecurityRight right)
	{
		for (int a = 35; a >= 1; a--)
		{
			if ((getStackInSlot(a) != null) && (getStackInSlot(a).getItem() == ModularForceFieldSystem.itemCardID))
			{
				String username_invtory = NBTTagCompoundHelper.getTAGfromItemstack(getStackInSlot(a)).getString("name");

				ItemCardPersonalID Card = (ItemCardPersonalID) getStackInSlot(a).getItem();

				boolean access = ItemCardPersonalID.hasRight(getStackInSlot(a), right);

				if (username_invtory.equals(username))
				{
					if (access)
					{
						return true;
					}
					return false;
				}

			}

		}

		return false;
	}

	public boolean RemotePlayerInventory(String username, SecurityRight right)
	{
		EntityPlayer player = this.worldObj.getPlayerEntityByName(username);
		if (player != null)
		{
			List<Slot> slots = player.inventoryContainer.inventorySlots;
			for (Slot slot : slots)
			{
				ItemStack stack = slot.getStack();
				if (stack != null)
				{
					if ((stack.getItem() instanceof ItemAccessCard))
					{
						if (ItemAccessCard.getvalidity(stack) > 0)
						{
							if (ItemAccessCard.getlinkID(stack) == getDeviceID())
							{
								if (ItemAccessCard.hasRight(stack, right))
								{
									/*if (!ItemAccessCard.getforAreaname(stack).equals(getDeviceName()))
									{
										ItemAccessCard.setforArea(stack, this);
									}*/
									//TODO: REMOVED NAME
									return true;
								}
							}
						}
						else
						{
							player.sendChatToPlayer("[Security Station] expired validity <Access license>");
							ItemStack Card = new ItemStack(ModularForceFieldSystem.itemCardEmpty, 1);
							slot.putStack(Card);
							//NetworkHandlerServer.syncClientPlayerinventorySlot(player, slot, Card);
						}
					}
				}
			}
		}

		return false;
	}

	public boolean isAccessGranted(String username, SecurityRight sr)
	{
		if (!isActive())
		{
			return true;
		}
		String[] ops = MFFSConfiguration.Admin.split(";");

		for (int i = 0; i <= ops.length - 1; i++)
		{
			if (username.equalsIgnoreCase(ops[i]))
			{
				return true;
			}
		}
		if (this.MainUser.equals(username))
		{
			return true;
		}
		if (RemoteInventory(username, sr))
		{
			return true;
		}
		if (RemotePlayerInventory(username, sr))
		{
			return true;
		}

		return false;
	}

	public ItemStack[] getContents()
	{
		return this.inventory;
	}

	@Override
	public int getStartInventorySide(ForgeDirection side)
	{
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		return 0;
	}

    /*
	@Override
	public List getFieldsForUpdate()
	{
		List NetworkedFields = new LinkedList();
		NetworkedFields.clear();

		NetworkedFields.addAll(super.getFieldsForUpdate());
		NetworkedFields.add("MainUser");

		return NetworkedFields;
	}
    */

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int slot) {
		
		if (slot == 0 && par1ItemStack.getItem() instanceof ItemAccessCard) {
			return true;
		}else if(slot != 0 && par1ItemStack.getItem() instanceof ItemCardPersonalID){
			return true;
		}
		return false;
			
	}

    /*
	@Override
	public void onNetworkHandlerEvent(int key, String value)
	{
		switch (key)
		{
			case 100:
				if (getStackInSlot(1) != null)
				{
					SecurityRight sr = (SecurityRight) SecurityRight.rights.get(value);
					if ((sr != null) && ((getStackInSlot(1).getItem() instanceof ItemCardPersonalID)))
					{
						ItemCardPersonalID.setRight(getStackInSlot(1), sr, !ItemCardPersonalID.hasRight(getStackInSlot(1), sr));
					}
				}
				break;
			case 101:
				if ((getStackInSlot(1) != null) && ((getStackInSlot(1).getItem() instanceof ItemAccessCard)))
				{
					if (ItemAccessCard.getvalidity(getStackInSlot(1)) <= 5)
					{
						setInventorySlotContents(1, new ItemStack(ModularForceFieldSystem.itemCardEmpty, 1));
					}
					else
					{
						ItemAccessCard.setvalidity(getStackInSlot(1), ItemAccessCard.getvalidity(getStackInSlot(1)) - 5);
					}
				}
				break;
			case 102:
				if (getStackInSlot(1) != null)
				{
					if ((getStackInSlot(1).getItem() instanceof ItemCardEmpty))
					{
						setInventorySlotContents(1, new ItemStack(ModularForceFieldSystem.itemCardAccess, 1));
						if ((getStackInSlot(1).getItem() instanceof ItemAccessCard))
						{
							ItemAccessCard.setforArea(getStackInSlot(1), this);
							ItemAccessCard.setvalidity(getStackInSlot(1), 5);
							ItemAccessCard.setlinkID(getStackInSlot(1), this);
						}

					}
					else if ((getStackInSlot(1).getItem() instanceof ItemAccessCard))
					{
						ItemAccessCard.setvalidity(getStackInSlot(1), ItemAccessCard.getvalidity(getStackInSlot(1)) + 5);
					}
				}
				break;
		}

		super.onNetworkHandlerEvent(key, value);
	}
    */

	public ItemStack getModCardStack()
	{
		if (getStackInSlot(1) != null)
		{
			return getStackInSlot(1);
		}
		return null;
	}

	@Override
	public TileEntitySecurityStation getLinkedSecurityStation()
	{
		return this;
	}
    
}