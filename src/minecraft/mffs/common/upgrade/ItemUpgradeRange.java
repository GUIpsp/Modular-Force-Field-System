package mffs.common.upgrade;

import java.util.List;

import mffs.common.MachineTypes;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemUpgradeRange extends ItemUpgrade
{

	public ItemUpgradeRange(int i)
	{
		super(i, "upgradeRange");
		setMaxStackSize(9);

	}

	@Override
	public String getName(ItemStack itemstack)
	{
		return "range";
	}
}