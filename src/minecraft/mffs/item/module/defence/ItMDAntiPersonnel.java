package mffs.item.module.defence;

import mffs.MFFSConfiguration;
import mffs.ModularForceFieldSystem;
import mffs.api.IDefenseStation;
import mffs.it.muo.fangyu.ItMD;
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

			player.attackEntityFrom(ModularForceFieldSystem.areaDefense, Integer.MAX_VALUE);
			defenseStation.requestFortron(MFFSConfiguration.defenceStationKillForceEnergy, false);
		}
		return false;
	}
}