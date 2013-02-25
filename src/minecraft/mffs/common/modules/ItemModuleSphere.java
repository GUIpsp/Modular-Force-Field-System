package mffs.common.modules;

import java.util.Set;

import mffs.api.PointXYZ;
import mffs.common.IModularProjector;
import mffs.common.ModularForceFieldSystem;
import mffs.common.options.ItemOptionBase;
import mffs.common.options.ItemOptionCutter;
import mffs.common.options.ItemOptionCamoflage;
import mffs.common.options.ItemOptionDefenseStation;
import mffs.common.options.ItemOptionFieldFusion;
import mffs.common.options.ItemOptionFieldManipulator;
import mffs.common.options.ItemOptionJammer;
import mffs.common.options.ItemOptionAntibiotic;
import mffs.common.options.ItemOptionSponge;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.item.Item;

public class ItemModuleSphere extends ItemModule3DBase
{
	public ItemModuleSphere(int i)
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
	public void calculateField(IModularProjector projector, Set ffLocs, Set ffInterior)
	{
		int radius = projector.countItemsInSlot(IModularProjector.Slots.Distance) + 4;

		int yDown = radius;

		if (((TileEntityProjector) projector).hasOption(ModularForceFieldSystem.itemOptionFieldManipulator, true))
		{
			yDown = 0;
		}

		for (int y1 = -yDown; y1 <= radius; y1++)
			for (int x1 = -radius; x1 <= radius; x1++)
				for (int z1 = -radius; z1 <= radius; z1++)
				{
					int dx = x1;
					int dy = y1;
					int dz = z1;

					int dist = (int) Math.round(Math.sqrt(dx * dx + dy * dy + dz * dz));

					if ((dist <= radius) && (dist > radius - (projector.countItemsInSlot(IModularProjector.Slots.Strength) + 1)))
						ffLocs.add(new PointXYZ(x1, y1, z1, 0));
					else if (dist <= radius)
						ffInterior.add(new PointXYZ(x1, y1, z1, 0));
				}
	}

	public static boolean supportsOption(ItemOptionBase item)
	{
		if ((item instanceof ItemOptionCamoflage))
			return true;
		if ((item instanceof ItemOptionDefenseStation))
			return true;
		if ((item instanceof ItemOptionFieldFusion))
			return true;
		if ((item instanceof ItemOptionFieldManipulator))
			return true;
		if ((item instanceof ItemOptionJammer))
			return true;
		if ((item instanceof ItemOptionAntibiotic))
			return true;
		if ((item instanceof ItemOptionSponge))
			return true;
		if ((item instanceof ItemOptionCutter))
			return true;

		return false;
	}

        @Override
	public boolean supportsOption(Item item)
	{
		if ((item instanceof ItemOptionCamoflage))
			return true;
		if ((item instanceof ItemOptionDefenseStation))
			return true;
		if ((item instanceof ItemOptionFieldFusion))
			return true;
		if ((item instanceof ItemOptionFieldManipulator))
			return true;
		if ((item instanceof ItemOptionJammer))
			return true;
		if ((item instanceof ItemOptionAntibiotic))
			return true;
		if ((item instanceof ItemOptionSponge))
			return true;
		if ((item instanceof ItemOptionCutter))
			return true;

		return false;
	}
}