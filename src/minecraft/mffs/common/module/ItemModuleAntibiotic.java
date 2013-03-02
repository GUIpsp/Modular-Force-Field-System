package mffs.common.module;

import java.util.List;

import mffs.api.IProjector;
import mffs.api.PointXYZ;
import mffs.common.ModularForceFieldSystem;
import mffs.common.mode.ItemModeSphere;
import mffs.common.tileentity.TileEntityProjector;
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
		setIconIndex(40);
	}

	public static void ProjectorNPCDefence(TileEntityProjector projector, World world)
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
					if ((!(projector.getMode() instanceof ItemModeSphere)) || (PointXYZ.distance(new PointXYZ((int) entityLiving.posX, (int) entityLiving.posY, (int) entityLiving.posZ, world), projector.getMachinePoint()) <= projector.getModuleCount(ModularForceFieldSystem.itemModuleTranslation) + 4))
					{
						if (projector.getLinkPower() < 10000)
						{
							break;
						}
						// if (projector.consumePower(10000, true))
						{
							entityLiving.attackEntityFrom(ModularForceFieldSystem.fieldDefense, 10);
							// projector.consumePower(10000, false);
						}
					}
				}
			}
		}
	}
}