package mffs.common.upgrade;

import net.minecraft.item.ItemStack;

public class ItemUpgradeSpeed extends ItemUpgrade
{
	public ItemUpgradeSpeed(int i)
	{
		super(i, "upgradeSpeed");
		this.setMaxStackSize(16);
	}

	@Override
	public String getName(ItemStack itemstack)
	{
		return "speed";
	}
}