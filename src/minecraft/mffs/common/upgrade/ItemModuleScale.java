package mffs.common.upgrade;

import java.util.List;

import mffs.common.MachineTypes;
import mffs.common.module.ItemModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemModuleScale extends ItemModule
{
	public ItemModuleScale(int i)
	{
		super(i, "moduleScale");
		this.setIconIndex(64);
	}
}