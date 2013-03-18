package mffs.common.card;

import mffs.api.PointXYZ;
import mffs.common.Functions;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.ZhuYao;
import mffs.common.tileentity.TileEntityFortronCapacitor;
import mffs.common.tileentity.TAnQuan;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemCardEmpty extends ItemCard
{

	public ItemCardEmpty(int i)
	{
		super(i, "cardEmpty");
		setMaxStackSize(16);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10)
	{
		if (world.isRemote)
		{
			return false;
		}
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if ((tileEntity instanceof TAnQuan))
		{
			TAnQuan securityStation = (TAnQuan) tileEntity;
			if (securityStation.isActive())
			{
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.CSR))
				{
					ItemStack newcard = new ItemStack(ZhuYao.itemCardSecurityLink);
					((ItemCardSecurityLink) newcard.getItem()).setInformation(newcard, new PointXYZ(i, j, k, world), "Secstation_ID", securityStation.getDeviceID());
					// ItemCardSecurityLink.setforArea(newcard, securityStation.getDeviceName());
					// TODO: REMOVED NAME
					if (--itemstack.stackSize <= 0)
					{
						entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, newcard);
					}
					else if (!entityplayer.inventory.addItemStackToInventory(newcard))
					{
						entityplayer.dropPlayerItem(newcard);
					}
					entityplayer.inventoryContainer.detectAndSendChanges();
					Functions.ChattoPlayer(entityplayer, "[Security Station] Success: <Security Station Link>  Card create");

					return true;
				}
			}
			else
			{
				Functions.ChattoPlayer(entityplayer, "[Security Station] Fail: Security Station must be Active  for create");
			}

		}

		if ((tileEntity instanceof TileEntityFortronCapacitor))
		{
			if (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB))
			{
				ItemStack newcard = new ItemStack(ZhuYao.itemCardPowerLink);
				// ((ItemCardPowerLink) newcard.getItem()).setInformation(newcard, new PointXYZ(i,
				// j, k, world), "CapacitorID", ((TileEntityCapacitor)
				// tileEntity).getPowerStorageID());
				// ItemCardPowerLink.setforArea(newcard, ((TileEntityCapacitor)
				// tileEntity).getDeviceName());
				// TODO: REMOVED NAME.
				if (--itemstack.stackSize <= 0)
				{
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, newcard);
				}
				else if (!entityplayer.inventory.addItemStackToInventory(newcard))
				{
					entityplayer.dropPlayerItem(newcard);
				}
				entityplayer.inventoryContainer.detectAndSendChanges();

				entityplayer.addChatMessage("[Capacitor] Success: <Power-Link> Card create");

				return true;
			}

		}

		return false;
	}
}