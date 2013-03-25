package mffs.it.muo.fangyu;

import mffs.MFFSConfiguration;
import mffs.ZhuYao;
import mffs.api.IDefenseStation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class ItMDAntiPersonnel extends ItMD
{
	public ItMDAntiPersonnel(int i)
	{
		super(i, "moduleAntiPersonnel");
	}

	@Override
	public boolean onDefend(IDefenseStation defenseStation, EntityLiving entityLiving)
	{
		boolean hasPermission = false;

		if (!hasPermission && entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLiving;
			player.addChatMessage("[" + defenseStation.getInvName() + "] Fairwell.");

			for (int i = 0; i < player.inventory.getSizeInventory(); i++)
			{
				defenseStation.mergeIntoInventory(player.inventory.getStackInSlot(i));
				player.inventory.setInventorySlotContents(i, null);
			}

			player.attackEntityFrom(ZhuYao.areaDefense, Integer.MAX_VALUE);
			defenseStation.requestFortron(MFFSConfiguration.defenceStationKillForceEnergy, false);
		}
		return false;
	}
}