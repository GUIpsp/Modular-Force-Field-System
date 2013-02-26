package mffs.common.multitool;

import java.util.List;

import mffs.api.PointXYZ;
import mffs.common.Functions;
import mffs.common.ModularForceFieldSystem;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.card.ItemCardDataLink;
import mffs.common.card.ItemCardPersonalID;
import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemMultitoolWriter extends ItemMultitool
{

	public ItemMultitoolWriter(int i)
	{
		super(i, 2, "multitoolWriter");
	}

	@Override
	public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer entityplayer, Entity entity)
	{
		if ((entity instanceof EntityPlayer))
		{
			List<Slot> slots = entityplayer.inventoryContainer.inventorySlots;
			for (Slot slot : slots)
			{
				ItemStack stack = slot.getStack();
				if ((stack != null) && (stack.getItem() == ModularForceFieldSystem.itemCardEmpty))
				{
					if (consumePower(itemstack, 1000, true))
					{
						consumePower(itemstack, 1000, false);
						ItemStack IDCard = new ItemStack(ModularForceFieldSystem.itemCardID, 1);
						ItemCardPersonalID.setOwner(IDCard, ((EntityPlayer) entity).username);

						if (--stack.stackSize <= 0)
						{
							slot.putStack(IDCard);
						}
						else if (!entityplayer.inventory.addItemStackToInventory(IDCard))
						{
							entityplayer.dropPlayerItem(IDCard);
						}
						Functions.ChattoPlayer(entityplayer, "[Multi-Tool] Identification Card created.");
						return true;
					}
					Functions.ChattoPlayer(entityplayer, "[Multi-Tool] Not enough Fortron!");
					return true;
				}

			}

			Functions.ChattoPlayer(entityplayer, "[Multi-Tool] Blank Card needed in Inventory!");
			return true;
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (entityplayer.isSneaking())
		{
			return super.onItemRightClick(itemstack, world, entityplayer);
		}

		List<Slot> slots = entityplayer.inventoryContainer.inventorySlots;
		for (Slot slot : slots)
		{
			ItemStack stack = slot.getStack();
			if ((stack != null) && (stack.getItem() == ModularForceFieldSystem.itemCardEmpty))
			{
				if (consumePower(itemstack, 1000, true))
				{
					consumePower(itemstack, 1000, false);
					ItemStack IDCard = new ItemStack(ModularForceFieldSystem.itemCardID, 1);
					ItemCardPersonalID.setOwner(IDCard, entityplayer.username);

					if (--stack.stackSize <= 0)
					{
						slot.putStack(IDCard);
					}
					else if (!entityplayer.inventory.addItemStackToInventory(IDCard))
					{
						entityplayer.dropPlayerItem(IDCard);
					}
					if (world.isRemote)
					{
						Functions.ChattoPlayer(entityplayer, "[Multi-Tool] Identification Card created.");
					}
					return itemstack;
				}
				if (world.isRemote)
				{
					Functions.ChattoPlayer(entityplayer, "[Multi-Tool] Not enough Fortron!");
				}
				return itemstack;
			}

		}

		if (world.isRemote)
		{
			Functions.ChattoPlayer(entityplayer, "[Multi-Tool] Blank Card needed in Inventory!");
		}
		return itemstack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (((tileEntity instanceof TileEntityMFFS)) && (SecurityHelper.isAccessGranted(tileEntity, player, world, SecurityRight.UCS)))
		{
			List<Slot> slots = player.inventoryContainer.inventorySlots;
			for (Slot slot : slots)
			{
				ItemStack playerstack = slot.getStack();
				if ((playerstack != null) && (playerstack.getItem() == ModularForceFieldSystem.itemCardEmpty))
				{
					if (consumePower(stack, 1000, true))
					{
						consumePower(stack, 1000, false);
						ItemStack IDCard = new ItemStack(ModularForceFieldSystem.itemCardDataLink);

						((ItemCardDataLink) IDCard.getItem()).setInformation(IDCard, new PointXYZ(x, y, z, world), "DeviceID", ((TileEntityMFFS) tileEntity).getDeviceID(), tileEntity);

						if (--playerstack.stackSize <= 0)
						{
							slot.putStack(IDCard);
						}
						else if (!player.inventory.addItemStackToInventory(IDCard))
						{
							player.dropPlayerItem(IDCard);
						}
						player.inventoryContainer.detectAndSendChanges();
						Functions.ChattoPlayer(player, "[Multi-Tool] Data-Link Card created.");

						return true;
					}

					Functions.ChattoPlayer(player, "[Multi-Tool] Not enough Fortron!");
					return false;
				}

			}

		}

		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}
}