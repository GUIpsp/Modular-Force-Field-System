package mffs.common.module.fangyu;

import java.util.List;

import mffs.api.IDefenseStation;
import mffs.api.IDefenseStationModule;
import mffs.common.MFFSConfiguration;
import mffs.common.ZhuYao;
import mffs.common.module.ItemModule;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleAntiPersonnel extends ItemModule implements IDefenseStationModule
{
	public ItemModuleAntiPersonnel(int i)
	{
		super(i, "moduleAntiPersonnel");
		this.setMaxStackSize(1);
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