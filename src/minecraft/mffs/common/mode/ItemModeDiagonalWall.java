package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.api.PointXYZ;

public class ItemModeDiagonalWall extends ItemModeWall
{
	public ItemModeDiagonalWall(int i)
	{
		super(i, "modeDiagonalWall");
		setIconIndex(56);
	}

	@Override
	public void calculateField(IProjector projector, Set ffLocs,Set interior)
	{/*
		int tpx = 0;
		int tpy = 0;
		int tpz = 0;

		int xstart = 0;
		int xend = 0;

		int zstart = 0;
		int zend = 0;

		if (projector.countItemsInSlot(IProjector.Slots.FocusUp) > 0)
		{
			xend = Math.max(xend, Math.max(projector.countItemsInSlot(IProjector.Slots.FocusUp), projector.countItemsInSlot(IProjector.Slots.FocusRight)));
			zend = Math.max(zend, Math.max(projector.countItemsInSlot(IProjector.Slots.FocusUp), projector.countItemsInSlot(IProjector.Slots.FocusRight)));
		}

		if (projector.countItemsInSlot(IProjector.Slots.FocusDown) > 0)
		{
			xstart = Math.max(xend, Math.max(projector.countItemsInSlot(IProjector.Slots.FocusDown), projector.countItemsInSlot(IProjector.Slots.FocusLeft)));
			zstart = Math.max(zend, Math.max(projector.countItemsInSlot(IProjector.Slots.FocusDown), projector.countItemsInSlot(IProjector.Slots.FocusLeft)));
		}

		if (projector.countItemsInSlot(IProjector.Slots.FocusLeft) > 0)
		{
			xend = Math.max(xend, Math.max(projector.countItemsInSlot(IProjector.Slots.FocusUp), projector.countItemsInSlot(IProjector.Slots.FocusLeft)));
			zstart = Math.max(zstart, Math.max(projector.countItemsInSlot(IProjector.Slots.FocusUp), projector.countItemsInSlot(IProjector.Slots.FocusLeft)));
		}

		if (projector.countItemsInSlot(IProjector.Slots.FocusRight) > 0)
		{
			zend = Math.max(zend, Math.max(projector.countItemsInSlot(IProjector.Slots.FocusDown), projector.countItemsInSlot(IProjector.Slots.FocusRight)));
			xstart = Math.max(xstart, Math.max(projector.countItemsInSlot(IProjector.Slots.FocusDown), projector.countItemsInSlot(IProjector.Slots.FocusRight)));
		}

		for (int x1 = 0 - zstart; x1 < zend + 1; x1++)
		{
			for (int z1 = 0 - xstart; z1 < xend + 1; z1++)
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

					if (((projector.getDirection().ordinal() != 0) && (projector.getDirection().ordinal() != 1)) || (((Math.abs(tpx) == Math.abs(tpz)) && (((tpx == 0) || (tpz == 0)) && ((tpx == 0) && (tpz == 0) && (((projector.countItemsInSlot(IProjector.Slots.FocusUp) != 0) && (tpx >= 0) && (tpz <= 0) && (tpx <= projector.countItemsInSlot(IProjector.Slots.FocusUp)) && (tpz <= projector.countItemsInSlot(IProjector.Slots.FocusUp))) || ((projector.countItemsInSlot(IProjector.Slots.FocusDown) != 0) && (tpx <= 0) && (tpz >= 0) && (tpx <= projector.countItemsInSlot(IProjector.Slots.FocusDown)) && (tpz <= projector.countItemsInSlot(IProjector.Slots.FocusDown))) || ((projector.countItemsInSlot(IProjector.Slots.FocusRight) != 0) && (tpx >= 0) && (tpz >= 0) && (tpx <= projector.countItemsInSlot(IProjector.Slots.FocusRight)) && (tpz <= projector.countItemsInSlot(IProjector.Slots.FocusRight))) || ((projector.countItemsInSlot(IProjector.Slots.FocusLeft) != 0) && (tpx <= 0) && (tpz <= 0) && (tpx <= projector.countItemsInSlot(IProjector.Slots.FocusLeft)) && (tpz <= projector.countItemsInSlot(IProjector.Slots.FocusLeft))))))) || (((projector.getDirection().ordinal() != 2) && (projector.getDirection().ordinal() != 3)) || (((Math.abs(tpx) == Math.abs(tpy)) && (((tpx == 0) || (tpy == 0)) && ((tpx == 0) && (tpy == 0) && (((projector.countItemsInSlot(IProjector.Slots.FocusUp) != 0) && (tpx >= 0) && (tpy >= 0) && (tpx <= projector.countItemsInSlot(IProjector.Slots.FocusUp)) && (tpy <= projector.countItemsInSlot(IProjector.Slots.FocusUp))) || ((projector.countItemsInSlot(IProjector.Slots.FocusDown) != 0) && (tpx <= 0) && (tpy <= 0) && (tpx <= projector.countItemsInSlot(IProjector.Slots.FocusDown)) && (tpy <= projector.countItemsInSlot(IProjector.Slots.FocusDown))) || ((projector.countItemsInSlot(IProjector.Slots.FocusRight) != 0) && (tpx >= 0) && (tpy <= 0) && (tpx <= projector.countItemsInSlot(IProjector.Slots.FocusRight)) && (tpy <= projector.countItemsInSlot(IProjector.Slots.FocusRight))) || ((projector.countItemsInSlot(IProjector.Slots.FocusLeft) != 0) && (tpx <= 0) && (tpy >= 0) && (tpx <= projector.countItemsInSlot(IProjector.Slots.FocusLeft)) && (tpy <= projector.countItemsInSlot(IProjector.Slots.FocusLeft))))))) || (((projector.getDirection().ordinal() == 4) || (projector.getDirection().ordinal() == 5)) && (Math.abs(tpz) == Math.abs(tpy)) && (((tpx != 0) && (tpy != 0)) || ((tpz == 0) && (tpy == 0) && (((projector.countItemsInSlot(IProjector.Slots.FocusUp) != 0) && (tpz >= 0) && (tpy >= 0) && (tpz <= projector.countItemsInSlot(IProjector.Slots.FocusUp)) && (tpy <= projector.countItemsInSlot(IProjector.Slots.FocusUp))) || ((projector.countItemsInSlot(IProjector.Slots.FocusDown) != 0) && (tpz <= 0) && (tpy <= 0) && (tpz <= projector.countItemsInSlot(IProjector.Slots.FocusDown)) && (tpy <= projector.countItemsInSlot(IProjector.Slots.FocusDown))) || ((projector.countItemsInSlot(IProjector.Slots.FocusRight) != 0) && (tpz >= 0) && (tpy <= 0) && (tpz <= projector.countItemsInSlot(IProjector.Slots.FocusRight)) && (tpy <= projector.countItemsInSlot(IProjector.Slots.FocusRight))) || ((projector.countItemsInSlot(IProjector.Slots.FocusLeft) != 0) && (tpz <= 0) && (tpy >= 0) && (tpz <= projector.countItemsInSlot(IProjector.Slots.FocusLeft)) && (tpy <= projector.countItemsInSlot(IProjector.Slots.FocusLeft)))))))))))
					{
						ffLocs.add(new PointXYZ(tpx, tpy, tpz, 0));
					}
				}
			}
		}*/
	}
}