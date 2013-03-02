package mffs.common.mode;

import java.util.Set;

import universalelectricity.core.vector.Vector3;

import mffs.api.IProjector;
import mffs.api.PointXYZ;
import mffs.common.ModularForceFieldSystem;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemOptionAntibiotic;
import mffs.common.module.ItemOptionCamoflage;
import mffs.common.module.ItemOptionCutter;
import mffs.common.module.ItemOptionDefenseStation;
import mffs.common.module.ItemOptionFieldFusion;
import mffs.common.module.ItemOptionFieldManipulator;
import mffs.common.module.ItemOptionJammer;
import mffs.common.module.ItemOptionSponge;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

public class ItemModeCube extends ItemMode3D
{

	public ItemModeCube(int i)
	{
		super(i, "moduleCube");
		setIconIndex(53);
	}

	@Override
	public boolean supportsDistance()
	{
		return true;
	}

	@Override
	public boolean supportsStrength()
	{
		return false;
	}

	@Override
	public boolean supportsMatrix()
	{
		return false;
	}

	@Override
	public void calculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior)
	{
		TileEntityProjector tileEntity = (TileEntityProjector) projector;

		int tpx = 0;
		int tpy = 0;
		int tpz = 0;

		int zPout = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, 2);
		int xMout = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, 4);
		int xPout = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, 6);
		int zMout = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, 8);

		int distance = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance);
		int strength = projector.getModuleCount(ModularForceFieldSystem.itemModuleStrength);

		for (int x = 0 - xMout; x < xPout + 1; x++)
		{
			for (int y = 0; y <= strength; y++)
			{
				for (int z = 0 - zPout; z < zMout + 1; z++)
				{
					if (((TileEntityProjector) projector).getDirection().ordinal() == 0)
					{
						tpy = y - y - y + 1;
						tpx = x;
						tpz = z;
					}

					if (((TileEntityProjector) projector).getDirection().ordinal() == 1)
					{
						tpy = y - 1;
						tpx = x;
						tpz = z;
					}

					if (((TileEntityProjector) projector).getDirection().ordinal() == 2)
					{
						tpz = y - y - y + 1;
						tpy = z - z - z;
						tpx = x - x - x;
					}

					if (((TileEntityProjector) projector).getDirection().ordinal() == 3)
					{
						tpz = y - 1;
						tpy = z - z - z;
						tpx = x;
					}

					if (((TileEntityProjector) projector).getDirection().ordinal() == 4)
					{
						tpx = y - y - y + 1;
						tpy = z - z - z;
						tpz = x;
					}
					if (((TileEntityProjector) projector).getDirection().ordinal() == 5)
					{
						tpx = y - 1;
						tpy = z - z - z;
						tpz = x - x - x;
					}

					if ((y == 0) || (y == strength) || (x == 0 - xMout) || (x == xPout) || (z == 0 - zPout) || (z == zMout))
					{
						if (((TileEntityProjector) projector).hasModule(ModularForceFieldSystem.itemOptionFieldManipulator, true))
						{
							switch (((TileEntityProjector) projector).getDirection().ordinal())
							{
								case 0:
									if (((TileEntityProjector) projector).yCoord + tpy <= ((TileEntityProjector) projector).yCoord)
									{
										break;
									}
									break;
								case 1:
									if (((TileEntityProjector) projector).yCoord + tpy >= ((TileEntityProjector) projector).yCoord)
									{
										break;
									}
									break;
								case 2:
									if (((TileEntityProjector) projector).zCoord + tpz <= ((TileEntityProjector) projector).zCoord)
									{
										break;
									}
									break;
								case 3:
									if (((TileEntityProjector) projector).zCoord + tpz >= ((TileEntityProjector) projector).zCoord)
									{
										break;
									}
									break;
								case 4:
								case 5:
									if (((TileEntityProjector) projector).xCoord + tpx > ((TileEntityProjector) projector).xCoord && ((TileEntityProjector) projector).xCoord + tpx < ((TileEntityProjector) projector).xCoord)
									{
										continue;
									}
							}
						}
						
						blockDef.add(new Vector3(tpx, tpy, tpz));
					}
					else
					{
						blockInterior.add(new Vector3(tpx, tpy, tpz));
					}
				}
			}
		}

		/*
		 * int radius = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance) + 4;
		 * TileEntityProjector tileEntity = (TileEntityProjector) projector;
		 * 
		 * int yDown = radius; int yTop = radius;
		 * 
		 * if (tileEntity.yCoord + radius > tileEntity.worldObj.getHeight()) { yTop = 255 -
		 * tileEntity.yCoord; }
		 * 
		 * if (projector.getModuleCount(ModularForceFieldSystem.itemOptionFieldManipulator) > 0) {
		 * yDown = 0; }
		 * 
		 * for (int y1 = -yDown; y1 <= yTop; y1++) { for (int x1 = -radius; x1 <= radius; x1++) {
		 * for (int z1 = -radius; z1 <= radius; z1++) { if ((x1 == -radius) || (x1 == radius) || (y1
		 * == -radius) || (y1 == yTop) || (z1 == -radius) || (z1 == radius)) { blockDef.add(new
		 * Vector3(x1, y1, z1)); } else { blockInterior.add(new Vector3(x1, y1, z1)); } } } }
		 */
	}

	public static boolean supportsOption(ItemModule item)
	{
		if ((item instanceof ItemOptionCamoflage))
		{
			return true;
		}
		if ((item instanceof ItemOptionDefenseStation))
		{
			return true;
		}
		if ((item instanceof ItemOptionFieldFusion))
		{
			return true;
		}
		if ((item instanceof ItemOptionFieldManipulator))
		{
			return true;
		}
		if ((item instanceof ItemOptionJammer))
		{
			return true;
		}
		if ((item instanceof ItemOptionAntibiotic))
		{
			return true;
		}
		if ((item instanceof ItemOptionSponge))
		{
			return true;
		}
		if ((item instanceof ItemOptionCutter))
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean supportsOption(Item item)
	{
		if ((item instanceof ItemOptionCamoflage))
		{
			return true;
		}
		if ((item instanceof ItemOptionDefenseStation))
		{
			return true;
		}
		if ((item instanceof ItemOptionFieldFusion))
		{
			return true;
		}
		if ((item instanceof ItemOptionFieldManipulator))
		{
			return true;
		}
		if ((item instanceof ItemOptionJammer))
		{
			return true;
		}
		if ((item instanceof ItemOptionAntibiotic))
		{
			return true;
		}
		if ((item instanceof ItemOptionSponge))
		{
			return true;
		}
		if ((item instanceof ItemOptionCutter))
		{
			return true;
		}

		return false;
	}
}