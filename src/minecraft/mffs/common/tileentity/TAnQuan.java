package mffs.common.tileentity;

import mffs.api.ISecurityCenter;
import mffs.api.SecurityPermission;
import mffs.common.MFFSConfiguration;
import mffs.common.NBTTagCompoundHelper;
import mffs.common.ZhuYao;
import mffs.common.card.ItCardIdentification;
import mffs.common.card.ItemCardFrequency;
import mffs.common.card.ItemCardTemporaryID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.io.ByteArrayDataInput;

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
				if (this.getOwner() != null)
				{
					this.setActive(true);
				}
				else
				{
					this.setActive(true);
				}
			}
		}
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		super.onReceivePacket(packetID, dataStream);

		if (packetID == 3)
		{
			if (this.getManipulatingCard() != null)
			{
				ZhuYao.itemCardID.addPermission(this.getManipulatingCard(), SecurityPermission.values()[dataStream.readInt()]);
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

	@Override
	public boolean isAccessGranted(String username, SecurityPermission permission)
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

		// Check if ID card is in this inventory.
		for (int i = 0; i < this.getSizeInventory(); i++)
		{
			if ((getStackInSlot(i) != null) && (getStackInSlot(i).getItem() == ZhuYao.itemCardID))
			{
				String username_invtory = NBTTagCompoundHelper.getTAGfromItemstack(getStackInSlot(i)).getString("name");

				ItCardIdentification Card = (ItCardIdentification) getStackInSlot(i).getItem();

				if (username_invtory.equals(username))
				{
					if (ZhuYao.itemCardID.hasPermission(this.getStackInSlot(i), permission))
					{
						return true;
					}

					return false;
				}

			}
		}

		// Check Temporary Card
		EntityPlayer entityPlayer = this.worldObj.getPlayerEntityByName(username);

		if (entityPlayer != null)
		{
			for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
			{
				ItemStack stack = entityPlayer.inventory.getStackInSlot(i);

				if (stack != null)
				{
					if (stack.getItem() instanceof ItemCardTemporaryID)
					{/*
					 * if (ItemCardTemporaryID.getvalidity(stack) > 0) { } else {
					 * player.sendChatToPlayer
					 * ("[Security Station] expired validity <Access license>"); ItemStack Card =
					 * new ItemStack(ZhuYao.itemCardEmpty, 1); slot.putStack(Card); //
					 * NetworkHandlerServer.syncClientPlayerinventorySlot(player, slot, // Card); }
					 */
					}
				}
			}
		}

		return this.getOwner().equals(username);
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemStack)
	{
		if (slotID == 0)
		{
			return itemStack.getItem() instanceof ItemCardFrequency;
		}
		else
		{
			return itemStack.getItem() instanceof ItCardIdentification;
		}
	}

	@Override
	public String getOwner()
	{
		ItemStack itemStack = this.getStackInSlot(2);

		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof ItCardIdentification)
			{
				return ((ItCardIdentification) itemStack.getItem()).getUsername(itemStack);
			}
		}

		return null;
	}

	@Override
	public ItemStack getManipulatingCard()
	{
		return this.getStackInSlot(1);
	}
}