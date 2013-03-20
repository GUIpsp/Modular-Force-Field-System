package mffs.common.upgrade;

import java.util.List;

import mffs.common.MachineTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemUpgradeCapacity extends ItemUpgrade
{
	public ItemUpgradeCapacity(int i)
	{
		super(i, "upgradeCapacity");
		setMaxStackSize(9);
	}

	@Override
	public String getName(ItemStack itemstack)
	{
		return "capacity";
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)))
		{
			info.add("compatible with:");
			info.add("MFFS " + MachineTypes.Extractor.getName());
			info.add("MFFS " + MachineTypes.Capacitor.getName());
		}
		else
		{
			info.add("compatible with: (Hold Shift)");
		}
	}
}