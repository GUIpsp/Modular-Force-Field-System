package mffs.it.ka;

import mffs.api.fortron.IItemFortronStorage;
import net.minecraft.item.ItemStack;

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