package mffs.machine.tile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import mffs.MFFSConfiguration;
import mffs.ModularForceFieldSystem;
import mffs.api.ISecurityCenter;
import mffs.api.SecurityPermission;
import mffs.api.card.ICardIdentification;
import mffs.item.card.ItemCardID;
import mffs.item.card.ItemCardFrequency;



import com.google.common.io.ByteArrayDataInput;

public class TileSecurityStation extends TileFrequency implements ISecurityCenter
{
	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public void setActive(boolean flag)
	{
		if (this.getOwner() != null || !flag)
		{
			super.setActive(flag);
		}
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream) throws IOException
	{
		super.onReceivePacket(packetID, dataStream);

		if (packetID == 3)
		{
			if (this.getManipulatingCard() != null)
			{
				SecurityPermission permission = SecurityPermission.values()[dataStream.readInt()];

				if (!ModularForceFieldSystem.itKaShenFen.hasPermission(this.getManipulatingCard(), permission))
				{
					ModularForceFieldSystem.itKaShenFen.addPermission(this.getManipulatingCard(), permission);
				}
				else
				{
					ModularForceFieldSystem.itKaShenFen.removePermission(this.getManipulatingCard(), permission);
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

			if (itemStack != null && itemStack.getItem() instanceof ICardIdentification)
			{
				if (username.equalsIgnoreCase(((ICardIdentification) itemStack.getItem()).getUsername(itemStack)))
				{
					if (((ICardIdentification) itemStack.getItem()).hasPermission(itemStack, permission))
					{
						return true;
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
			return itemStack.getItem() instanceof ItemCardFrequency;
		}
		else
		{
			return itemStack.getItem() instanceof ItemCardID;
		}
	}

	@Override
	public String getOwner()
	{
		ItemStack itemStack = this.getStackInSlot(2);

		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof ItemCardID)
			{
				return ((ItemCardID) itemStack.getItem()).getUsername(itemStack);
			}
		}

		return null;
	}

	@Override
	public ItemStack getManipulatingCard()
	{
		return this.getStackInSlot(1);
	}

	@Override
	public List<ISecurityCenter> getSecurityCenters()
	{
		List<ISecurityCenter> securityCenters = new ArrayList<ISecurityCenter>();
		securityCenters.add(this);
		return securityCenters;
	}
}