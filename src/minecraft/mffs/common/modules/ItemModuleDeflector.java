package mffs.common.modules;

import java.util.Set;

import mffs.api.PointXYZ;
import mffs.common.IModularProjector;
import mffs.common.options.ItemOptionBase;
import mffs.common.options.ItemOptionCutter;
import mffs.common.options.ItemOptionCamoflage;
import mffs.common.options.ItemOptionShock;
import net.minecraft.item.Item;

public class ItemModuleDeflector extends ItemModuleBase
{
	public ItemModuleDeflector(int i)
	{
		super(i, "moduleDeflector");
		setIconIndex(50);
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
		return true;
	}

        @Override
	public void calculateField(IModularProjector projector, Set ffLocs)
	{
		int tpx = 0;
		int tpy = 0;
		int tpz = 0;

		for (int x1 = 0 - projector.countItemsInSlot(IModularProjector.Slots.FocusLeft); x1 < projector.countItemsInSlot(IModularProjector.Slots.FocusRight) + 1; x1++)
			for (int z1 = 0 - projector.countItemsInSlot(IModularProjector.Slots.FocusUp); z1 < projector.countItemsInSlot(IModularProjector.Slots.FocusDown) + 1; z1++)
			{
				if (projector.getSide() == 0)
				{
					tpy = 0 - projector.countItemsInSlot(IModularProjector.Slots.Distance) - 1;
					tpx = x1;
					tpz = z1;
				}

				if (projector.getSide() == 1)
				{
					tpy = 0 + projector.countItemsInSlot(IModularProjector.Slots.Distance) + 1;
					tpx = x1;
					tpz = z1;
				}

				if (projector.getSide() == 2)
				{
					tpz = 0 - projector.countItemsInSlot(IModularProjector.Slots.Distance) - 1;
					tpy = z1 - z1 - z1;
					tpx = x1 - x1 - x1;
				}

				if (projector.getSide() == 3)
				{
					tpz = 0 + projector.countItemsInSlot(IModularProjector.Slots.Distance) + 1;
					tpy = z1 - z1 - z1;
					tpx = x1;
				}

				if (projector.getSide() == 4)
				{
					tpx = 0 - projector.countItemsInSlot(IModularProjector.Slots.Distance) - 1;
					tpy = z1 - z1 - z1;
					tpz = x1;
				}
				if (projector.getSide() == 5)
				{
					tpx = 0 + projector.countItemsInSlot(IModularProjector.Slots.Distance) + 1;
					tpy = z1 - z1 - z1;
					tpz = x1 - x1 - x1;
				}

				ffLocs.add(new PointXYZ(tpx, tpy, tpz, 0));
			}
	}

	public static boolean supportsOption(ItemOptionBase item)
	{
		if ((item instanceof ItemOptionCutter))
			return true;
		if ((item instanceof ItemOptionCamoflage))
			return true;
		if ((item instanceof ItemOptionShock))
			return true;

		return false;
	}

        @Override
	public boolean supportsOption(Item item)
	{
		if ((item instanceof ItemOptionCutter))
			return true;
		if ((item instanceof ItemOptionCamoflage))
			return true;
		if ((item instanceof ItemOptionShock))
			return true;

		return false;
	}
}