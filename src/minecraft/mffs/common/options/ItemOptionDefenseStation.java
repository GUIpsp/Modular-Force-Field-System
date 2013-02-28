package mffs.common.options;

import java.util.List;

import mffs.api.IModularProjector;
import mffs.api.PointXYZ;
import mffs.common.FrequencyGrid;
import mffs.common.ModularForceFieldSystem;
import mffs.common.SecurityRight;
import mffs.common.modules.ItemModuleSphere;
import mffs.common.tileentity.TileEntityFortronCapacitor;
import mffs.common.tileentity.TileEntityProjector;
import mffs.common.tileentity.TileEntitySecurityStation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemOptionDefenseStation extends ItemOptionBase
{

	public ItemOptionDefenseStation(int i)
	{
		super(i, "optionDefenseStation");
		setIconIndex(39);
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

			for (PointXYZ png : projector.getFieldQueue())
			{
				fieldxmax = Math.max(fieldxmax, png.X);
				fieldxmin = Math.min(fieldxmin, png.X);
				fieldymax = Math.max(fieldymax, png.Y);
				fieldymin = Math.min(fieldymin, png.Y);
				fieldzmax = Math.max(fieldzmax, png.Z);
				fieldzmin = Math.min(fieldzmin, png.Z);
			}

			List LivingEntitylist = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(fieldxmin, fieldymin, fieldzmin, fieldxmax, fieldymax, fieldzmax));

			for (int i = 0; i < LivingEntitylist.size(); i++)
			{
				EntityLiving entityLiving = (EntityLiving) LivingEntitylist.get(i);

				if ((entityLiving instanceof EntityPlayer))
				{
					if ((!(projector.getType() instanceof ItemModuleSphere)) || (PointXYZ.distance(new PointXYZ((int) entityLiving.posX, (int) entityLiving.posY, (int) entityLiving.posZ, world), projector.getMachinePoint()) <= projector.countItemsInSlot(IModularProjector.Slots.Distance) + 4))
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
								/*TileEntityCapacitor cap = (TileEntityCapacitor) FrequencyGrid.getWorldMap(world).getCapacitor().get(Integer.valueOf(projector.getPowerSourceID()));
								if (cap != null)
								{
									TileEntitySecurityStation SecurityStation = cap.getLinkedSecurityStation();

									if (SecurityStation != null)
									{
										killswitch = !SecurityStation.isAccessGranted(((EntityPlayer) entityLiving).username, SecurityRight.SR);
									}
								}*/
							}
							if (projector.getAccessType() == 3)
							{
								TileEntitySecurityStation SecurityStation = projector.getLinkedSecurityStation();
								if (SecurityStation != null)
								{
									killswitch = !SecurityStation.isAccessGranted(((EntityPlayer) entityLiving).username, SecurityRight.SR);
								}
							}

							if (killswitch)
							{
								//if (projector.consumePower(10000, true))
								{
									((EntityPlayer) entityLiving).addChatMessage("!!! [Area Defence] leave or die !!!");
									((EntityPlayer) entityLiving).attackEntityFrom(ModularForceFieldSystem.fieldDefense, 10);
								//	projector.consumePower(10000, false);
								}
							}
						}
					}
				}
			}
		}
	}
}