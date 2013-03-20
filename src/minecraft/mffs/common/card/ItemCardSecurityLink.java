package mffs.common.card;

import mffs.api.PointXYZ;
import mffs.api.SecurityHelper;
import mffs.common.Functions;
import mffs.common.SecurityRight;
import mffs.common.ZhuYao;
import mffs.common.tileentity.TAnQuan;
import mffs.common.tileentity.TDianRong;
import mffs.common.tileentity.TFangYingJi;
import mffs.common.tileentity.TFangYu;
import mffs.common.tileentity.TileEntityControlSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ISidedInventory;

public class ItemCardSecurityLink extends ItemCard
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
						int Sec_ID = card.getValuefromKey("Secstation_ID", inventiory.getStackInSlot(slot));
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