package mffs.common.upgrade;

import java.util.List;

import mffs.common.MFFSMachines;
import mffs.common.item.ItemMFFSBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemCapacitorUpgradeCapacity extends ItemMFFSBase
{
	public ItemCapacitorUpgradeCapacity(int i)
	{
		super(i);
		setIconIndex(32);
		setMaxStackSize(9);
		this.setNoRepair();
	}

	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)))
		{
			info.add("compatible with:");
			info.add("MFFS " + MFFSMachines.Extractor.displayName);
			info.add("MFFS " + MFFSMachines.Capacitor.displayName);
		}
		else
		{
			info.add("compatible with: (Hold Shift)");
		}
	}
}