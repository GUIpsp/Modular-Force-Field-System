package mffs.common.card;

import mffs.api.IForceEnergyStorageBlock;
import mffs.api.IPowerLinkItem;
import mffs.api.PointXYZ;
import mffs.common.FrequencyGridOld;
import mffs.common.Functions;
import mffs.common.ModularForceFieldSystem;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.tileentity.TileEntityFortronCapacitor;
import mffs.common.tileentity.TileEntityConverter;
import mffs.common.tileentity.TileEntityDefenseStation;
import mffs.common.tileentity.TileEntityForcilliumExtractor;
import mffs.common.tileentity.TileEntityMFFS;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemCardPowerLink extends ItemCard implements IPowerLinkItem
{

	public IForceEnergyStorageBlock storage;

	public ItemCardPowerLink(int i)
	{
		super(i, "cardPowerLink");
		setIconIndex(17);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
	{
		super.onUpdate(itemStack, world, entity, par4, par5);

		if (this.tick > 600)
		{
			int Cap_ID = getValuefromKey("CapacitorID", itemStack);
			if (Cap_ID != 0)
			{
				TileEntityFortronCapacitor cap = (TileEntityFortronCapacitor) FrequencyGridOld.getWorldMap(world).getCapacitor().get(Integer.valueOf(Cap_ID));
				if (cap != null)
				{/*
					if (!cap.getDeviceName().equals(getforAreaname(itemStack)))
					{
						setforArea(itemStack, cap.getDeviceName());
					}
					*/
					//TODO: REMOVED NAME
				}
			}

			this.tick = 0;
		}
		this.tick += 1;
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (!world.isRemote)
		{
			if (((tileEntity instanceof TileEntityConverter)) && (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB)))
			{
				return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0, "<Power-Link>");
			}

			if (((tileEntity instanceof TileEntityProjector)) && (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB)))
			{
				return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0, "<Power-Link>");
			}

			if (((tileEntity instanceof TileEntityForcilliumExtractor)) && (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB)))
			{
				TileEntityForcilliumExtractor entityExtractor = (TileEntityForcilliumExtractor) tileEntity;
				if (entityExtractor.getStackInSlot(1) == null)
				{
					((TileEntityForcilliumExtractor) tileEntity).setInventorySlotContents(1, itemstack);
					entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
					Functions.ChattoPlayer(entityplayer, "Success: <Power-Link> Card installed");
					return true;
				}

				if (entityExtractor.getStackInSlot(1).getItem() == ModularForceFieldSystem.itemCardEmpty)
				{
					ItemStack itemstackcopy = itemstack.copy();
					entityExtractor.setInventorySlotContents(1, itemstackcopy);
					Functions.ChattoPlayer(entityplayer, "Success: <Power-Link> Card data copied ");
					return true;
				}
				Functions.ChattoPlayer(entityplayer, "Fail: Slot is not empty");
				return false;
			}

			if (((tileEntity instanceof TileEntityDefenseStation)) && (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB)))
			{
				return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0, "<Power-Link>");
			}

			if (((tileEntity instanceof TileEntityFortronCapacitor)) && (SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB)))
			{
				return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 2, "<Power-Link>");
			}

		}

		return false;
	}

	public IForceEnergyStorageBlock getForceEnergyStorageBlock(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		if ((itemStack != null) && ((itemStack.getItem() instanceof ItemCard)))
		{
			if (((ItemCard) itemStack.getItem()).isvalid(itemStack))
			{
				PointXYZ png = getCardTargetPoint(itemStack);
				if (png != null)
				{
					if (png.dimensionId != world.provider.dimensionId)
					{
						return null;
					}

					if ((world.getBlockTileEntity(png.X, png.Y, png.Z) instanceof TileEntityFortronCapacitor))
					{
						TileEntityFortronCapacitor cap = (TileEntityFortronCapacitor) world.getBlockTileEntity(png.X, png.Y, png.Z);
						if (cap != null)
						{
							/*if ((cap.getPowerStorageID() == getValuefromKey("CapacitorID", itemStack)) && getValuefromKey("CapacitorID", itemStack) >= 0)
							{
								if (!cap.getDeviceName().equals(getforAreaname(itemStack)))
								{
									setforArea(itemStack, cap.getDeviceName());
								}//TODO: REMOVED NAME

								if (cap.getTransmitRange() >= PointXYZ.distance(tem.getMachinePoint(), cap.getMachinePoint()))
								{
									return cap;
								}
								return null;
							}*/
						}
					}
					else
					{
						int Cap_ID = getValuefromKey("CapacitorID", itemStack);
						if (Cap_ID != 0)
						{
							TileEntityFortronCapacitor cap = (TileEntityFortronCapacitor) FrequencyGridOld.getWorldMap(png.getPointWorld()).getCapacitor().get(Integer.valueOf(Cap_ID));
							if (cap != null)
							{
								setInformation(itemStack, cap.getMachinePoint(), "CapacitorID", Cap_ID);
								if (cap.getTransmitRange() >= PointXYZ.distance(tem.getMachinePoint(), cap.getMachinePoint()))
								{
								//	return cap;
								}
								return null;
							}
						}
					}

					if (!world.getChunkFromBlockCoords(png.X, png.Z).isChunkLoaded)
					{
						((ItemCard) itemStack.getItem()).setinvalid(itemStack);
					}
				}
			}
		}
		return null;
	}

	@Override
	public int getAvailablePower(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		this.storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (this.storage != null)
		{
			return this.storage.getStorageAvailablePower();
		}
		return 0;
	}

	@Override
	public int getMaximumPower(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		this.storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (this.storage != null)
		{
			return this.storage.getStorageMaxPower();
		}
		return 1;
	}

	@Override
	public int getPowersourceID(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		this.storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (this.storage != null)
		{
			return this.storage.getPowerStorageID();
		}
		return 0;
	}

	@Override
	public int getPercentageCapacity(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		this.storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (this.storage != null)
		{
			return this.storage.getPercentageStorageCapacity();
		}
		return 0;
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount, boolean simulation, TileEntityMFFS tem, World world)
	{
		this.storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (this.storage != null)
		{
			return this.storage.consumePowerFromStorage(powerAmount, simulation);
		}
		return false;
	}

	@Override
	public boolean insertPower(ItemStack itemStack, int powerAmount, boolean simulation, TileEntityMFFS tem, World world)
	{
		this.storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (this.storage != null)
		{
			return this.storage.insertPowerToStorage(powerAmount, simulation);
		}
		return false;
	}

	@Override
	public int getfreeStorageAmount(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		this.storage = getForceEnergyStorageBlock(itemStack, tem, world);
		if (this.storage != null)
		{
			return this.storage.getFreeStorageAmount();
		}
		return 0;
	}

	@Override
	public boolean isPowersourceItem()
	{
		return false;
	}
}