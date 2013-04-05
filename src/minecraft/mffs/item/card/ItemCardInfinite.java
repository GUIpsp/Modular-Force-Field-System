package mffs.item.card;

import mffs.api.fortron.IItemFortronStorage;
import net.minecraft.item.ItemStack;

public class ItemCardInfinite extends ItemCard implements IItemFortronStorage
{
	public ItemCardInfinite(int id)
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