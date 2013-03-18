package mffs.common.module;

import java.util.List;

import mffs.api.PointXYZ;
import mffs.common.SecurityRight;
import mffs.common.ZhuYao;
import mffs.common.mode.ItemModeSphere;
import mffs.common.tileentity.TileEntityProjector;
import mffs.common.tileentity.TAnQuan;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleDefenseStation extends ItemModule
{
	public ItemModuleDefenseStation(int i)
	{
		super(i, "moduleDefenseStation");
	}

	public static void ProjectorPlayerDefence(TileEntityProjector projector, World world)
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

				if ((entityLiving instanceof EntityPlayer))
				{
					if ((!(projector.getMode() instanceof ItemModeSphere)) || (PointXYZ.distance(new PointXYZ((int) entityLiving.posX, (int) entityLiving.posY, (int) entityLiving.posZ, world), projector.getMachinePoint()) <= projector.getModuleCount(ZhuYao.itemModuleScale) + 4))
					{
						if (projector.getLinkPower() < 10000)
						{
							break;
						}
						if (projector.getLinkPower() > 10000)
						{
							boolean killswitch = false;

							if (projector.getAccessType() == 2)
							{
								/*
								 * TileEntityCapacitor cap = (TileEntityCapacitor)
								 * FrequencyGrid.getWorldMap
								 * (world).getCapacitor().get(Integer.valueOf
								 * (projector.getPowerSourceID())); if (cap != null) {
								 * TileEntitySecurityStation SecurityStation =
								 * cap.getLinkedSecurityStation();
								 * 
								 * if (SecurityStation != null) { killswitch =
								 * !SecurityStation.isAccessGranted(((EntityPlayer)
								 * entityLiving).username, SecurityRight.SR); } }
								 */
							}
							if (projector.getAccessType() == 3)
							{
								TAnQuan SecurityStation = projector.getLinkedSecurityStation();
								if (SecurityStation != null)
								{
									killswitch = !SecurityStation.isAccessGranted(((EntityPlayer) entityLiving).username, SecurityRight.SR);
								}
							}

							if (killswitch)
							{
								// if (projector.consumePower(10000, true))
								{
									((EntityPlayer) entityLiving).addChatMessage("!!! [Area Defence] leave or die !!!");
									((EntityPlayer) entityLiving).attackEntityFrom(ZhuYao.fieldDefense, 10);
									// projector.consumePower(10000, false);
								}
							}
						}
					}
				}
			}
		}
	}
}