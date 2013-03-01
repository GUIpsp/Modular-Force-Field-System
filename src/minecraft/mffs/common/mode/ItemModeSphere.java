package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.api.PointXYZ;
import mffs.common.ModularForceFieldSystem;
import mffs.common.module.ItemOptionAntibiotic;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemOptionCamoflage;
import mffs.common.module.ItemOptionCutter;
import mffs.common.module.ItemOptionDefenseStation;
import mffs.common.module.ItemOptionFieldFusion;
import mffs.common.module.ItemOptionFieldManipulator;
import mffs.common.module.ItemOptionJammer;
import mffs.common.module.ItemOptionSponge;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.item.Item;

public class ItemModeSphere extends ItemMode3D
{

	public ItemModeSphere(int i)
	{
		super(i, "moduleSphere");
		setIconIndex(52);
	}

	@Override
	public boolean supportsDistance()
	{
		return true;
	}

	@Override
	public boolean supportsStrength()
	{
		return true;
	}

	@Override
	public boolean supportsMatrix()
	{
		return false;
	}

	@Override
	public void calculateField(IProjector projector, Set ffLocs, Set ffInterior)
	{
		int radius = projector.countItemsInSlot(IProjector.Slots.Distance) + 4;

		int yDown = radius;

		if (((TileEntityProjector) projector).hasModule(ModularForceFieldSystem.itemOptionFieldManipulator, true))
		{
			yDown = 0;
		}
		
		for (int x = -radius; x <= radius; x++)
		{
			for (int y = -yDown; y <= radius; y++)
			{
				for (int z = -radius; z <= radius; z++)
				{
					int dx = x;
					int dy = y;
					int dz = z;

					int dist = (int) Math.round(Math.sqrt(dx * dx + dy * dy + dz * dz));

					if ((dist <= radius) && (dist > radius - (projector.countItemsInSlot(IProjector.Slots.Strength) + 1)))
					{
						ffLocs.add(new PointXYZ(x, y, z, 0));
					}
					else if (dist <= radius)
					{
						ffInterior.add(new PointXYZ(x, y, z, 0));
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