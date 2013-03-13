package mffs.common.card;

import mffs.api.PointXYZ;
import mffs.common.FrequencyGridOld;
import mffs.common.Functions;
import mffs.common.ModularForceFieldSystem;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.tileentity.TileEntityControlSystem;
import mffs.common.tileentity.TileEntityDefenseStation;
import mffs.common.tileentity.TileEntityFortronCapacitor;
import mffs.common.tileentity.TileEntityProjector;
import mffs.common.tileentity.TileEntitySecStorage;
import mffs.common.tileentity.TileEntitySecurityStation;
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

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
	{
		super.onUpdate(itemStack, world, entity, par4, par5);

		if (this.tick > 600)
		{
			int Sec_ID = getValuefromKey("Secstation_ID", itemStack);
			if (Sec_ID != 0)
			{
				TileEntitySecurityStation sec = (TileEntitySecurityStation) FrequencyGridOld.getWorldMap(world).getSecStation().get(Integer.valueOf(Sec_ID));
				if (sec != null)
				{/*
				 * if (!sec.getDeviceName().equals(getforAreaname(itemStack))) {
				 * setforArea(itemStack, sec.getDeviceName()); }
				 */
					// TODO: REMOVED NAME
				}
			}

			this.tick = 0;
		}
		this.tick += 1;
	}

	public static TileEntitySecurityStation getLinkedSecurityStation(ISidedInventory inventiory, int slot, World world)
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

					if ((world.getBlockTileEntity(png.X, png.Y, png.Z) instanceof TileEntitySecurityStation))
					{
						TileEntitySecurityStation sec = (TileEntitySecurityStation) world.getBlockTileEntity(png.X, png.Y, png.Z);
						if (sec != null)
						{
							if ((sec.getDeviceID() == card.getValuefromKey("Secstation_ID", inventiory.getStackInSlot(slot))) && (card.getValuefromKey("Secstation_ID", inventiory.getStackInSlot(slot)) != 0))
							{
								/*
								 * if
								 * (!sec.getDeviceName().equals(getforAreaname(inventiory.getStackInSlot
								 * (slot)))) { setforArea(inventiory.getStackInSlot(slot),
								 * sec.getDeviceName()); }
								 */
								// TODO: REMOVED NAME
								return sec;
							}
						}
					}
					else
					{
						int Sec_ID = card.getValuefromKey("Secstation_ID", inventiory.getStackInSlot(slot));
						if (Sec_ID != 0)
						{
							TileEntitySecurityStation sec = (TileEntitySecurityStation) FrequencyGridOld.getWorldMap(world).getSecStation().get(Integer.valueOf(Sec_ID));
							if (sec != null)
							{
								card.setInformation(inventiory.getStackInSlot(slot), sec.getMachinePoint(), "Secstation_ID", Sec_ID);
								return sec;
							}
						}
					}

					if (world.getChunkFromBlockCoords(png.X, png.Z).isChunkLoaded)
					{
						inventiory.setInventorySlotContents(slot, new ItemStack(ModularForceFieldSystem.itemCardEmpty));
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (!world.isRemote)
		{
			if ((tileEntity instanceof TileEntityControlSystem))
			{
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB))
				{
					return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0, "<Security Station Link>");
				}

			}

			if ((tileEntity instanceof TileEntityFortronCapacitor))
			{
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB))
				{
					return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 4, "<Security Station Link>");
				}

			}

			if (((tileEntity instanceof TileEntityDefenseStation)) && (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB)))
			{
				return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 1, "<Security Station Link>");
			}

			if (((tileEntity instanceof TileEntitySecStorage)) && (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB)))
			{
				return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0, "<Security Station Link>");
			}

			if (((tileEntity instanceof TileEntityProjector)) && (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB)))
			{
				return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 12, "<Security Station Link>");
			}

		}

		return false;
	}
}