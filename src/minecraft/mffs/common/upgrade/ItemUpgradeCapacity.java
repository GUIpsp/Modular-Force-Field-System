package mffs.common.upgrade;

import java.util.List;

import mffs.common.MachineTypes;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemUpgradeCapacity extends ItemMFFS
{
	public ItemUpgradeCapacity(int i)
	{
		super(i, "upgradeCapacity");
		setIconIndex(32);
		setMaxStackSize(9);
		this.setNoRepair();
	}

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