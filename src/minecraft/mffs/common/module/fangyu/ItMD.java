package mffs.common.module.fangyu;

import java.util.List;

import mffs.api.IDefenseStationModule;
import mffs.common.module.ItemModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class ItMD extends ItemModule implements IDefenseStationModule
{
	public ItMD(int id, String name)
	{
		super(id, name);
		this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		info.add("\u00a74Defense Station");
		super.addInformation(itemStack, player, info, b);
	}

}
