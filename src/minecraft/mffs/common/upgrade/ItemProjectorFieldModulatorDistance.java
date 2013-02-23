package mffs.common.upgrade;

import java.util.List;

import mffs.common.MFFSMachines;
import mffs.common.item.ItemMFFSBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemProjectorFieldModulatorDistance extends ItemMFFSBase
{
	public ItemProjectorFieldModulatorDistance(int i)
	{
		super(i);
		setIconIndex(64);
		setMaxStackSize(64);
		this.setNoRepair();

	}

	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)))
		{
			info.add("compatible with:");
			info.add("MFFS " + MFFSMachines.Projector.displayName);
			info.add("MFFS " + MFFSMachines.DefenceStation.displayName);
		}
		else
		{
			info.add("compatible with: (Hold Shift)");
		}
	}
}