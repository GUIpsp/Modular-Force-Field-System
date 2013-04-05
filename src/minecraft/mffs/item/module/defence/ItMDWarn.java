package mffs.item.module.defence;

import mffs.api.IDefenseStation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class ItMDWarn extends ItMD
{
	public ItMDWarn(int i)
	{
		super(i, "moduleWarn");
	}

	@Override
	public boolean onDefend(IDefenseStation defenseStation, EntityLiving entityLiving)
	{
		// TODO: Add custom message.
		boolean hasPermission = false;
		if (!hasPermission && entityLiving instanceof EntityPlayer)
		{
			((EntityPlayer) entityLiving).addChatMessage("[" + defenseStation.getInvName() + "] Leave this zone immediately. You have no right to enter.");
		}
		return false;
	}
}