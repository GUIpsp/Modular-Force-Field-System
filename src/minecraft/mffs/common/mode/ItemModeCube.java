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
		int radius = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance) + 4;
		TileEntityProjector tileEntity = (TileEntityProjector) projector;

		int yDown = radius;
		int yTop = radius;

		if (tileEntity.yCoord + radius > tileEntity.worldObj.getHeight())
		{
			yTop = 255 - tileEntity.yCoord;
		}

		if (projector.getModuleCount(ModularForceFieldSystem.itemOptionFieldManipulator) > 0)
		{
			yDown = 0;
		}

		for (int y1 = -yDown; y1 <= yTop; y1++)
		{
			for (int x1 = -radius; x1 <= radius; x1++)
			{
				for (int z1 = -radius; z1 <= radius; z1++)
				{
					if ((x1 == -radius) || (x1 == radius) || (y1 == -radius) || (y1 == yTop) || (z1 == -radius) || (z1 == radius))
					{
						blockDef.add(new Vector3(x1, y1, z1));
					}
					else
					{
						blockInterior.add(new Vector3(x1, y1, z1));
					}
				}
			}
		}
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