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
	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 10 == 0)
			{
				if (!this.getOwner().equals("") || this.getOwner() == null)
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
			}
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

	public boolean remoteInventory(String username, SecurityRight right)
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

	public boolean remotePlayerInventory(String username, SecurityRight right)
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

		if (this.getOwner().equals(username))
		{
			return true;
		}

		if (remoteInventory(username, sr))
		{
			return true;
		}
		if (remotePlayerInventory(username, sr))
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

	@Override
	public String getOwner()
	{
		ItemStack itemStack = this.getStackInSlot(1);

		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof ItemCardPersonalID)
			{
				return ((ItemCardPersonalID) itemStack.getItem()).getUsername(itemStack);
			}
		}

		return null;
	}
}