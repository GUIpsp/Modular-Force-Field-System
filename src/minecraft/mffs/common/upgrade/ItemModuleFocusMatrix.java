package mffs.common.upgrade;

import java.util.List;

import mffs.common.MachineTypes;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemModuleFocusMatrix extends ItemMFFS
{

	public ItemModuleFocusMatrix(int i)
	{
		super(i, "moduleFocusMatrix");
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)))
		{
			info.add("compatible with:");
			info.add("MFFS " + MachineTypes.Projector.getName());
		}
		else
		{
			info.add("compatible with: (Hold Shift)");
		}
	}
}