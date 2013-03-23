package mffs.common.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidContainerRegistry;

public class ItemFortronCell extends ItemFortron
{
	public ItemFortronCell(int id)
	{
		super(id, "fortronCell");
	}

	@Override
	public int getFortronCapacity(ItemStack itemStack)
	{
		return 50 * LiquidContainerRegistry.BUCKET_VOLUME;
	}
}