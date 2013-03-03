package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemModuleAntibiotic;
import mffs.common.module.ItemModuleCamoflage;
import mffs.common.module.ItemModuleDefenseStation;
import mffs.common.module.ItemModuleDisintegration;
import mffs.common.module.ItemModuleFusion;
import mffs.common.module.ItemModuleJammer;
import mffs.common.module.ItemModuleSponge;
import net.minecraft.item.Item;

public class ItemModeContainment extends ItemProjectorMode
{
	public ItemModeContainment(int i)
	{
		super(i, "modeContainment");
		this.setIconIndex(54);
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
	public void calculateField(IProjector projector, Set ffLocs, Set ffInterior)
	{/*
		int tpx = 0;
		int tpy = 0;
		int tpz = 0;

		int xMout = projector.countItemsInSlot(IProjector.Slots.FocusLeft);
		int xPout = projector.countItemsInSlot(IProjector.Slots.FocusRight);
		int zMout = projector.countItemsInSlot(IProjector.Slots.FocusDown);
		int zPout = projector.countItemsInSlot(IProjector.Slots.FocusUp);
		int distance = projector.countItemsInSlot(IProjector.Slots.Distance);
		int Strength = projector.countItemsInSlot(IProjector.Slots.Strength) + 1;

		for (int y1 = 0; y1 <= Strength; y1++)
		{
			for (int x1 = 0 - xMout; x1 < xPout + 1; x1++)
			{
				for (int z1 = 0 - zPout; z1 < zMout + 1; z1++)
				{
					if (((TileEntityProjector) projector).getDirection().ordinal() == 0)
					{
						tpy = y1 - y1 - y1 - distance - 1;
						tpx = x1;
						tpz = z1;
					}

					if (((TileEntityProjector) projector).getDirection().ordinal() == 1)
					{
						tpy = y1 + distance + 1;
						tpx = x1;
						tpz = z1;
					}

					if (((TileEntityProjector) projector).getDirection().ordinal() == 2)
					{
						tpz = y1 - y1 - y1 - distance - 1;
						tpy = z1 - z1 - z1;
						tpx = x1 - x1 - x1;
					}

					if (((TileEntityProjector) projector).getDirection().ordinal() == 3)
					{
						tpz = y1 + distance + 1;
						tpy = z1 - z1 - z1;
						tpx = x1;
					}

					if (((TileEntityProjector) projector).getDirection().ordinal() == 4)
					{
						tpx = y1 - y1 - y1 - distance - 1;
						tpy = z1 - z1 - z1;
						tpz = x1;
					}
					if (((TileEntityProjector) projector).getDirection().ordinal() == 5)
					{
						tpx = y1 + distance + 1;
						tpy = z1 - z1 - z1;
						tpz = x1 - x1 - x1;
					}

					if ((y1 == 0) || (y1 == Strength) || (x1 == 0 - xMout) || (x1 == xPout) || (z1 == 0 - zPout) || (z1 == zMout))
					{
						ffLocs.add(new PointXYZ(tpx, tpy, tpz, 0));
					}
					else
					{
						ffInterior.add(new PointXYZ(tpx, tpy, tpz, 0));
					}
				}
			}
		}*/
	}

	public static boolean supportsOption(ItemModule item)
	{
		if ((item instanceof ItemModuleCamoflage))
		{
			return true;
		}
		if ((item instanceof ItemModuleDefenseStation))
		{
			return true;
		}
		if ((item instanceof ItemModuleFusion))
		{
			return true;
		}
		if ((item instanceof ItemModuleJammer))
		{
			return true;
		}
		if ((item instanceof ItemModuleAntibiotic))
		{
			return true;
		}
		if ((item instanceof ItemModuleSponge))
		{
			return true;
		}
		if ((item instanceof ItemModuleDisintegration))
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean supportsOption(Item item)
	{
		if ((item instanceof ItemModuleCamoflage))
		{
			return true;
		}
		if ((item instanceof ItemModuleDefenseStation))
		{
			return true;
		}
		if ((item instanceof ItemModuleFusion))
		{
			return true;
		}
		if ((item instanceof ItemModuleJammer))
		{
			return true;
		}
		if ((item instanceof ItemModuleAntibiotic))
		{
			return true;
		}
		if ((item instanceof ItemModuleSponge))
		{
			return true;
		}
		if ((item instanceof ItemModuleDisintegration))
		{
			return true;
		}

		return false;
	}
}