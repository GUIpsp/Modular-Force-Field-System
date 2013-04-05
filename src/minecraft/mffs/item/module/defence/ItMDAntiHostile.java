package mffs.item.module.defence;

import mffs.ModularForceFieldSystem;
import mffs.api.IDefenseStation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;

public class ItMDAntiHostile extends ItMD
{
	public ItMDAntiHostile(int i)
	{
		super(i, "moduleAntiHostile");
	}

	@Override
	public boolean onDefend(IDefenseStation defenseStation, EntityLiving entityLiving)
	{
		if (entityLiving instanceof IMob && !(entityLiving instanceof INpc))
		{

			entityLiving.attackEntityFrom(ModularForceFieldSystem.areaDefense, 20);
		}

		return false;
	}
}