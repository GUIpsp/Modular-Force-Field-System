package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.api.PointXYZ;
import mffs.common.module.ItemOptionBase;
import mffs.common.module.ItemOptionCamoflage;
import mffs.common.module.ItemOptionCutter;
import mffs.common.module.ItemOptionShock;
import net.minecraft.item.Item;

public class ItemModeWall extends ItemProjectorMode
{

	public ItemModeWall(int i)
	{
		super(i, "moduleWall");
		setIconIndex(49);
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
		return true;
	}

	@Override
	public void calculateField(IProjector projector, Set ffLocs)
	{
		int tpx = 0;
		int tpy = 0;
		int tpz = 0;

		for (int x1 = 0 - projector.countItemsInSlot(IProjector.Slots.FocusLeft); x1 < projector.countItemsInSlot(IProjector.Slots.FocusRight) + 1; x1++)
		{
			for (int z1 = 0 - projector.countItemsInSlot(IProjector.Slots.FocusDown); z1 < projector.countItemsInSlot(IProjector.Slots.FocusUp) + 1; z1++)
			{
				for (int y1 = 1; y1 < projector.countItemsInSlot(IProjector.Slots.Strength) + 1 + 1; y1++)
				{
					if (projector.getDirection().ordinal() == 0)
					{
						tpy = y1 - y1 - y1 - projector.countItemsInSlot(IProjector.Slots.Distance);
						tpx = x1;
						tpz = z1 - z1 - z1;
					}

					if (projector.getDirection().ordinal() == 1)
					{
						tpy = y1 + projector.countItemsInSlot(IProjector.Slots.Distance);
						tpx = x1;
						tpz = z1 - z1 - z1;
					}

					if (projector.getDirection().ordinal() == 2)
					{
						tpz = y1 - y1 - y1 - projector.countItemsInSlot(IProjector.Slots.Distance);
						tpx = x1 - x1 - x1;
						tpy = z1;
					}

					if (projector.getDirection().ordinal() == 3)
					{
						tpz = y1 + projector.countItemsInSlot(IProjector.Slots.Distance);
						tpx = x1;
						tpy = z1;
					}

					if (projector.getDirection().ordinal() == 4)
					{
						tpx = y1 - y1 - y1 - projector.countItemsInSlot(IProjector.Slots.Distance);
						tpz = x1;
						tpy = z1;
					}
					if (projector.getDirection().ordinal() == 5)
					{
						tpx = y1 + projector.countItemsInSlot(IProjector.Slots.Distance);
						tpz = x1 - x1 - x1;
						tpy = z1;
					}

					if (((projector.getDirection().ordinal() != 0) && (projector.getDirection().ordinal() != 1)) || (((tpx == 0) && (tpz != 0)) || ((tpz == 0) && (tpx != 0)) || ((tpz == 0) && (tpx == 0)) || (((projector.getDirection().ordinal() != 2) && (projector.getDirection().ordinal() != 3)) || (((tpx == 0) && (tpy != 0)) || ((tpy == 0) && (tpx != 0)) || ((tpy == 0) && (tpx == 0)) || (((projector.getDirection().ordinal() == 4) || (projector.getDirection().ordinal() == 5)) && (((tpz == 0) && (tpy != 0)) || ((tpy == 0) && (tpz != 0)) || ((tpy == 0) && (tpz == 0))))))))
					{
						ffLocs.add(new PointXYZ(tpx, tpy, tpz, 0));
					}
				}
			}
		}
	}

	public static boolean supportsOption(ItemOptionBase item)
	{
		if ((item instanceof ItemOptionCutter))
		{
			return true;
		}
		if ((item instanceof ItemOptionCamoflage))
		{
			return true;
		}
		if ((item instanceof ItemOptionShock))
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean supportsOption(Item item)
	{
		if ((item instanceof ItemOptionCutter))
		{
			return true;
		}
		if ((item instanceof ItemOptionCamoflage))
		{
			return true;
		}
		if ((item instanceof ItemOptionShock))
		{
			return true;
		}

		return false;
	}
}