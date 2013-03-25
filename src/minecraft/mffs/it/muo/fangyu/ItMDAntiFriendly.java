package mffs.it.muo.fangyu;

import mffs.ZhuYao;
import mffs.api.IDefenseStation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;

public class ItMDAntiFriendly extends ItMD
{
	public ItMDAntiFriendly(int i)
	{
		super(i, "moduleAntiFriendly");
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