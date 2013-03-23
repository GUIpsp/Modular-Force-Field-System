package mffs.common.card;

import java.util.List;

import mffs.api.PointXYZ;
import mffs.common.NBTTagCompoundHelper;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

public abstract class ItKa extends ItemMFFS
{
	public ItKa(int id, String name)
	{
		super(id, name);
		this.setMaxStackSize(1);
	}
}