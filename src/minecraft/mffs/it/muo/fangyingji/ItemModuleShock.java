package mffs.it.muo.fangyingji;

import mffs.ZhuYao;
import mffs.it.muo.ItM;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class ItemModuleShock extends ItM
{
	public ItemModuleShock(int i)
	{
		super(i, "moduleShock");
	}

	@Override
	public boolean onCollideWithForceField(World world, int x, int y, int z, Entity entity)
	{
		if (entity instanceof EntityLiving)
		{
			entity.attackEntityFrom(ZhuYao.fieldShock, 10);
		}

		return false;
	}
}