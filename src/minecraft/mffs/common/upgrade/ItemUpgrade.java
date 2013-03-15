package mffs.common.upgrade;

import net.minecraft.item.ItemStack;
import mffs.common.item.ItemMFFS;
import universalelectricity.prefab.modifier.IModifier;

public abstract class ItemUpgrade extends ItemMFFS implements IModifier
{
	public ItemUpgrade(int id, String name)
	{
		super(id, name);
	}

	@Override
	public int getEffectiveness(ItemStack itemstack)
	{
		return 1;
	}

}
