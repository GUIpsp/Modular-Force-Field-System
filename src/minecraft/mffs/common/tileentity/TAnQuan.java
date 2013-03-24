package mffs.common.tileentity;

import mffs.api.IIdentificationCard;
import mffs.api.ISecurityCenter;
import mffs.api.SecurityPermission;
import mffs.common.MFFSConfiguration;
import mffs.common.ZhuYao;
import mffs.common.card.ItKaShenFen;
import mffs.common.card.ItKaShengBuo;
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
				SecurityPermission permission = SecurityPermission.values()[dataStream.readInt()];

				if (!ZhuYao.itKaShenFen.hasPermission(this.getManipulatingCard(), permission))
				{
					ZhuYao.itKaShenFen.addPermission(this.getManipulatingCard(), permission);
				}
				else
				{
					ZhuYao.itKaShenFen.removePermission(this.getManipulatingCard(), permission);
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
			ItemStack itemStack = this.getStackInSlot(i);

			if (itemStack != null && itemStack.getItem() instanceof IIdentificationCard)
			{
				if (username.equalsIgnoreCase(((IIdentificationCard) itemStack.getItem()).getUsername(itemStack)))
				{
					if (((IIdentificationCard) itemStack.getItem()).hasPermission(itemStack, permission))
					{
						return true;
					}
				}
			}
		}

		// Check Temporary Card
		EntityPlayer entityPlayer = this.worldObj.getPlayerEntityByName(username);

		if (entityPlayer != null)
		{
			for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
			{
				ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);

				if (itemStack != null && itemStack.getItem() instanceof IIdentificationCard)
				{
					if (username.equalsIgnoreCase(((IIdentificationCard) itemStack.getItem()).getUsername(itemStack)))
					{
						if (((IIdentificationCard) itemStack.getItem()).hasPermission(itemStack, permission))
						{
							return true;
						}
					}
				}
			}
		}

		return username.equalsIgnoreCase(this.getOwner());
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
			return itemStack.getItem() instanceof ItKaShenFen;
		}
	}

	@Override
	public String getOwner()
	{
		ItemStack itemStack = this.getStackInSlot(2);

		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof ItKaShenFen)
			{
				return ((ItKaShenFen) itemStack.getItem()).getUsername(itemStack);
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