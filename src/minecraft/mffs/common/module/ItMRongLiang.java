package mffs.common.module;

import java.util.List;

import mffs.common.MachineTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItMRongLiang extends ItM
{
	public ItMRongLiang(int i)
	{
		super(i, "upgradeCapacity");
		this.setMaxStackSize(9);
	}
}