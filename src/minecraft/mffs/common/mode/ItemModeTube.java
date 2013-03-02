package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.api.PointXYZ;
import mffs.common.ModularForceFieldSystem;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemOptionCamoflage;
import mffs.common.module.ItemOptionCutter;
import mffs.common.module.ItemOptionFieldFusion;
import mffs.common.module.ItemOptionFieldManipulator;
import mffs.common.module.ItemOptionJammer;
import mffs.common.module.ItemOptionShock;
import mffs.common.module.ItemOptionSponge;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.item.Item;

public class ItemModeTube extends ItemProjectorMode
{

	public ItemModeTube(int i)
	{
		super(i, "moduleTube");
		setIconIndex(51);
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
		int tpx = 0;
		int tpy = 0;
		int tpz = 0;
		int x_offset_s = 0;
		int y_offset_s = 0;
		int z_offset_s = 0;
		int x_offset_e = 0;
		int y_offset_e = 0;
		int z_offset_e = 0;

		int distance = projector.countItemsInSlot(IProjector.Slots.Distance) + 2;
		int Strength = projector.countItemsInSlot(IProjector.Slots.Strength);

		if ((projector.getDirection().ordinal() == 0) || (projector.getDirection().ordinal() == 1))
		{
			tpy = Strength;
			tpx = distance;
			tpz = distance;

			y_offset_s = Strength - Strength;
			if (((TileEntityProjector) projector).hasModule(ModularForceFieldSystem.itemOptionFieldManipulator, true))
			{
				if (projector.getDirection().ordinal() == 0)
				{
					y_offset_e = Strength;
				}
				if (projector.getDirection().ordinal() == 1)
				{
					y_offset_s = Strength;
				}
			}
		}

		if ((projector.getDirection().ordinal() == 2) || (projector.getDirection().ordinal() == 3))
		{
			tpy = distance;
			tpz = Strength;
			tpx = distance;

			z_offset_s = Strength - Strength;
			if (((TileEntityProjector) projector).hasModule(ModularForceFieldSystem.itemOptionFieldManipulator, true))
			{
				if (projector.getDirection().ordinal() == 2)
				{
					z_offset_e = Strength;
				}
				if (projector.getDirection().ordinal() == 3)
				{
					z_offset_s = Strength;
				}
			}
		}
		if ((projector.getDirection().ordinal() == 4) || (projector.getDirection().ordinal() == 5))
		{
			tpy = distance;
			tpz = distance;
			tpx = Strength;

			x_offset_s = Strength - Strength;
			if (((TileEntityProjector) projector).hasModule(ModularForceFieldSystem.itemOptionFieldManipulator, true))
			{
				if (projector.getDirection().ordinal() == 4)
				{
					x_offset_e = Strength;
				}
				if (projector.getDirection().ordinal() == 5)
				{
					x_offset_s = Strength;
				}
			}

		}

		for (int z1 = 0 - tpz + z_offset_s; z1 <= tpz - z_offset_e; z1++)
		{
			for (int x1 = 0 - tpx + x_offset_s; x1 <= tpx - x_offset_e; x1++)
			{
				for (int y1 = 0 - tpy + y_offset_s; y1 <= tpy - y_offset_e; y1++)
				{
					int tpx_temp = tpx;
					int tpy_temp = tpy;
					int tpz_temp = tpz;

					if ((tpx == Strength) && ((projector.getDirection().ordinal() == 4) || (projector.getDirection().ordinal() == 5)))
					{
						tpx_temp++;
					}
					if ((tpy == Strength) && ((projector.getDirection().ordinal() == 0) || (projector.getDirection().ordinal() == 1)))
					{
						tpy_temp++;
					}
					if ((tpz == Strength) && ((projector.getDirection().ordinal() == 2) || (projector.getDirection().ordinal() == 3)))
					{
						tpz_temp++;
					}

					if (((x1 == 0 - tpx_temp) || (x1 == tpx_temp) || (y1 == 0 - tpy_temp) || (y1 == tpy_temp) || (z1 == 0 - tpz_temp) || (z1 == tpz_temp)) && (((TileEntityProjector) projector).yCoord + y1 >= 0))
					{
						ffLocs.add(new PointXYZ(x1, y1, z1, 0));
					}
					else
					{
						ffInterior.add(new PointXYZ(x1, y1, z1, 0));
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
		if ((item instanceof ItemOptionSponge))
		{
			return true;
		}
		if ((item instanceof ItemOptionCutter))
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
		if ((item instanceof ItemOptionCamoflage))
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
		if ((item instanceof ItemOptionSponge))
		{
			return true;
		}
		if ((item instanceof ItemOptionCutter))
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