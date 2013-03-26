package mffs.it.muo.fangyu;

import java.util.List;

import mffs.api.IDefenseStation;
import mffs.api.modules.IDefenseStationModule;
import mffs.it.muo.ItM;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItMD extends ItM implements IDefenseStationModule
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

	@Override
	public boolean onDefend(IDefenseStation defenseStation, EntityLiving entityLiving)
	{
		return false;
	}

}
