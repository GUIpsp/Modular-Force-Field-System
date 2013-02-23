package mffs.common.upgrade;

import java.util.List;

import mffs.common.MFFSMachines;
import mffs.common.item.ItemMFFSBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemExtractorUpgradeBooster extends ItemMFFSBase
{
	public ItemExtractorUpgradeBooster(int i)
	{
		super(i);
		setIconIndex(37);
		setMaxStackSize(19);
		this.setNoRepair();

	}

	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)))
		{
			info.add("Compatible with:");
			info.add("MFFS " + MFFSMachines.Extractor.displayName);
		}
		else
		{
			info.add("Compatible with: (Hold Shift)");
		}
	}
}