package mffs.common.card;

import java.util.List;

import mffs.api.IItemFortronStorage;
import mffs.api.IPowerLinkItem;
import mffs.common.item.ItemMFFS;
import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItKaWuXian extends ItKa implements IItemFortronStorage
{
	public ItKaWuXian(int id)
	{
		super(id, "cardInfinite");
	}

	@Override
	public void setFortronEnergy(int joules, ItemStack itemStack)
	{

	}

	@Override
	public int getFortronEnergy(ItemStack itemStack)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public int getFortronCapacity(ItemStack itemStack)
	{
		return Integer.MAX_VALUE;
	}
}