package mffs.common.upgrade;

import java.util.List;

import mffs.common.MFFSMachine;
import mffs.common.item.ItemMFFSBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemCapacitorUpgradeRange extends ItemMFFSBase
{
	public ItemCapacitorUpgradeRange(int i)
	{
		super(i);
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