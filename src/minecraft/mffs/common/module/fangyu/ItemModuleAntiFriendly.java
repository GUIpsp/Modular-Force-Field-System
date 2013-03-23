package mffs.common.module.fangyu;

import java.util.List;

import mffs.api.IDefenseStation;
import mffs.api.IDefenseStationModule;
import mffs.common.ZhuYao;
import mffs.common.module.ItemModule;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleAntiFriendly extends ItemModule implements IDefenseStationModule
{
	public ItemModuleAntiFriendly(int i)
	{
		super(i, "moduleAntiFriendly");
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onDefend(IDefenseStation defenseStation, EntityLiving entityLiving)
	{
		if (!(entityLiving instanceof IMob && !(entityLiving instanceof INpc)))
		{
			entityLiving.attackEntityFrom(ZhuYao.areaDefense, Integer.MAX_VALUE);
		}
		return false;
	}
}