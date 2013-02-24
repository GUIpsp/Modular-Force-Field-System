package mffs.common.upgrade;

import java.util.List;

import mffs.common.MFFSMachine;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemUpgradeRange extends ItemMFFS
{
	public ItemUpgradeRange(int i)
	{
		super(i, "upgradeRange");
		setIconIndex(33);
		setMaxStackSize(9);
		this.setNoRepair();

	}

	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)))
		{
			info.add("compatible with:");
			info.add("MFFS " + MFFSMachine.Capacitor.getName());
		}
		else
		{
			info.add("compatible with: (Hold Shift)");
		}
	}
}