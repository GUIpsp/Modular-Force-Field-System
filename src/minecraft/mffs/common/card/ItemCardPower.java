package mffs.common.card;

import java.util.List;

import mffs.api.IForceEnergyItems;
import mffs.api.IPowerLinkItem;
import mffs.common.item.ItemMFFS;
import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCardPower extends ItemMFFS implements IPowerLinkItem, IForceEnergyItems
{

	public ItemCardPower(int id)
	{
		super(id, "cardInfinite");
		setMaxStackSize(1);
	}

	@Override
	public int getPercentageCapacity(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		return 100;
	}

	@Override
	public int getAvailablePower(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		return 10000000;
	}

	@Override
	public int getMaximumPower(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		return 10000000;
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount, boolean simulation, TileEntityMFFS tem, World world)
	{
		return true;
	}

	@Override
	public boolean insertPower(ItemStack itemStack, int powerAmount, boolean simulation, TileEntityMFFS tem, World world)
	{
		return false;
	}

	@Override
	public int getPowersourceID(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		return 0;
	}

	@Override
	public int getfreeStorageAmount(ItemStack itemStack, TileEntityMFFS tem, World world)
	{
		return 0;
	}

	@Override
	public boolean isPowersourceItem()
	{
		return true;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		info.add("Gives Infinite Fortron Energy");
		info.add("Creative Mode Only");
	}

	@Override
	public int getAvailablePower(ItemStack itemStack)
	{
		return getAvailablePower(itemStack, null, null);
	}

	@Override
	public int getMaximumPower(ItemStack itemStack)
	{
		return getMaximumPower(itemStack, null, null);
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount, boolean simulation)
	{
		return true;
	}

	@Override
	public void setAvailablePower(ItemStack itemStack, int amount)
	{
	}

	@Override
	public int getPowerTransferrate()
	{
		return 1000000;
	}

	@Override
	public int getItemDamage(ItemStack stackInSlot)
	{
		return 0;
	}
}