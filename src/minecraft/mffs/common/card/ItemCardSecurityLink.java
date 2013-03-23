package mffs.common.card;

import mffs.api.PointXYZ;
import mffs.common.ZhuYao;
import mffs.common.tileentity.TAnQuan;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ISidedInventory;

public class ItemCardSecurityLink extends ItKa
{

	public ItemCardSecurityLink(int i)
	{
		super(i, "cardSecurityLink");
	}

	public static TAnQuan getLinkedSecurityStation(ISidedInventory inventiory, int slot, World world)
	{
		if (inventiory.getStackInSlot(slot) != null)
		{
			if ((inventiory.getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink))
			{
				ItemCardSecurityLink card = (ItemCardSecurityLink) inventiory.getStackInSlot(slot).getItem();
				PointXYZ png = card.getCardTargetPoint(inventiory.getStackInSlot(slot));
				if (png != null)
				{
					if (png.dimensionId != world.provider.dimensionId)
					{
						return null;
					}

					if ((world.getBlockTileEntity(png.X, png.Y, png.Z) instanceof TAnQuan))
					{
					}
					else
					{
						int Sec_ID = card.getValue("Secstation_ID", inventiory.getStackInSlot(slot));
						if (Sec_ID != 0)
						{
						}
					}

					if (world.getChunkFromBlockCoords(png.X, png.Z).isChunkLoaded)
					{
						inventiory.setInventorySlotContents(slot, new ItemStack(ZhuYao.itemCardEmpty));
					}
				}
			}
		}
		return null;

	}
}