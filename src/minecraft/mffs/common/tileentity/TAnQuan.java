package mffs.common.tileentity;

import java.util.List;

import mffs.api.ISecurityStation;
import mffs.common.MFFSConfiguration;
import mffs.common.NBTTagCompoundHelper;
import mffs.common.SecurityRight;
import mffs.common.ZhuYao;
import mffs.common.card.ItemAccessCard;
import mffs.common.card.ItemCardPersonalID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class TAnQuan extends TShengBuo implements ISecurityStation
{
	private String MainUser = "";

	public String getMainUser()
	{
		return this.MainUser;
	}

	public void setMainUser(String s)
	{
		if (!this.MainUser.equals(s))
		{
			this.MainUser = s;
		}
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

	public void checkslots()
	{
		if (getStackInSlot(0) != null)
		{
			if (getStackInSlot(0).getItem() == ZhuYao.itemCardID)
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

	public boolean RemoteInventory(String username, SecurityRight right)
	{
		for (int a = 35; a >= 1; a--)
		{
			if ((getStackInSlot(a) != null) && (getStackInSlot(a).getItem() == ZhuYao.itemCardID))
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
									/*
									 * if
									 * (!ItemAccessCard.getforAreaname(stack).equals(getDeviceName
									 * ())) { ItemAccessCard.setforArea(stack, this); }
									 */
									// TODO: REMOVED NAME
									return true;
								}
							}
						}
						else
						{
							player.sendChatToPlayer("[Security Station] expired validity <Access license>");
							ItemStack Card = new ItemStack(ZhuYao.itemCardEmpty, 1);
							slot.putStack(Card);
							// NetworkHandlerServer.syncClientPlayerinventorySlot(player, slot,
							// Card);
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
		String[] ops = MFFSConfiguration.administrators.split(";");

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

	/*
	 * @Override public List getFieldsForUpdate() { List NetworkedFields = new LinkedList();
	 * NetworkedFields.clear();
	 * 
	 * NetworkedFields.addAll(super.getFieldsForUpdate()); NetworkedFields.add("MainUser");
	 * 
	 * return NetworkedFields; }
	 */

	@Override
	public boolean isItemValid(int slotID, ItemStack itemStack)
	{
		if (slotID == 0 && itemStack.getItem() instanceof ItemAccessCard)
		{
			return true;
		}
		else if (slotID != 0 && itemStack.getItem() instanceof ItemCardPersonalID)
		{
			return true;
		}
		return false;

	}

	public ItemStack getModCardStack()
	{
		if (getStackInSlot(1) != null)
		{
			return getStackInSlot(1);
		}
		return null;
	}
}