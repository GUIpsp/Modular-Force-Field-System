package mffs.common.upgrade;

import net.minecraft.item.ItemStack;

public class ItemUpgradeRange extends ItemUpgrade
{
	public ItemUpgradeRange(int i)
	{
		super(i, "upgradeRange");
		this.setMaxStackSize(9);
	}

	@Override
	public String getName(ItemStack itemstack)
	{
		return "range";
	}
}