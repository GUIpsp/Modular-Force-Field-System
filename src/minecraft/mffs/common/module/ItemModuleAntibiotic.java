package mffs.common.module;

import java.util.List;

import mffs.common.ZhuYao;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleAntibiotic extends ItemModule
{
	public ItemModuleAntibiotic(int i)
	{
		super(i, "moduleAntibiotic");
		this.setMaxStackSize(1);
	}

	public static void ProjectorNPCDefence(TFangYingJi projector, World world)
	{
		if (projector.isActive())
		{
			int fieldxmin = projector.xCoord;
			int fieldxmax = projector.xCoord;
			int fieldymin = projector.yCoord;
			int fieldymax = projector.yCoord;
			int fieldzmin = projector.zCoord;
			int fieldzmax = projector.zCoord;

			for (Vector3 position : projector.getFieldQueue())
			{
				fieldxmax = Math.max(fieldxmax, position.intX());
				fieldxmin = Math.min(fieldxmin, position.intX());
				fieldymax = Math.max(fieldymax, position.intY());
				fieldymin = Math.min(fieldymin, position.intY());
				fieldzmax = Math.max(fieldzmax, position.intZ());
				fieldzmin = Math.min(fieldzmin, position.intZ());
			}

			List LivingEntitylist = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(fieldxmin, fieldymin, fieldzmin, fieldxmax, fieldymax, fieldzmax));

			for (int i = 0; i < LivingEntitylist.size(); i++)
			{
				EntityLiving entityLiving = (EntityLiving) LivingEntitylist.get(i);

				if (((entityLiving instanceof EntityMob)) || ((entityLiving instanceof EntitySlime)) || ((entityLiving instanceof EntityGhast)))
				{
					entityLiving.attackEntityFrom(ZhuYao.fieldDefense, 10);
				}
			}
		}
	}
}