package mffs.common.tileentity;

import java.util.List;

import mffs.api.ISecurityCenter;
import mffs.common.MFFSConfiguration;
import mffs.common.NBTTagCompoundHelper;
import mffs.common.SecurityRight;
import mffs.common.ZhuYao;
import mffs.common.card.ItemAccessCard;
import mffs.common.card.ItemCardFrequency;
import mffs.common.card.ItemCardPersonalID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class TAnQuan extends TShengBuo implements ISecurityCenter
{
	private String mainUser = "";

	public String getMainUser()
	{
		return this.mainUser;
	}

	public void setMainUser(String s)
	{
		if (!this.mainUser.equals(s))
		{
			this.mainUser = s;
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
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
		return 12;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
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

	@Override
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

		if (this.mainUser.equals(username))
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

	@Override
	public boolean isItemValid(int slotID, ItemStack itemStack)
	{
		if (slotID == 0)
		{
			return itemStack.getItem() instanceof ItemCardFrequency;
		}
		else
		{
			return itemStack.getItem() instanceof ItemCardPersonalID;
		}
	}
}