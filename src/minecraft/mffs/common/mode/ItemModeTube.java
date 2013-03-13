package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.api.PointXYZ;
import mffs.common.ZhuYao;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemModuleCamoflage;
import mffs.common.module.ItemModuleDisintegration;
import mffs.common.module.ItemModuleFusion;
import mffs.common.module.ItemModuleJammer;
import mffs.common.module.ItemModuleManipulator;
import mffs.common.module.ItemModuleShock;
import mffs.common.module.ItemModuleSponge;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

public class ItemModeTube extends ItemProjectorMode
{
	public ItemModeTube(int i)
	{
		super(i, "modeTube");
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

		int scale = projector.getModuleCount(ZhuYao.itemModuleScale) + 2;
		int translation = projector.getModuleCount(ZhuYao.itemModuleTranslation);

		if ((projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 0) || (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 1))
		{
			tpy = translation;
			tpx = scale;
			tpz = scale;

			y_offset_s = translation - translation;
			if (projector.getModuleCount(ZhuYao.itemModuleManipulator) > 0)
			{
				if (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 0)
				{
					y_offset_e = translation;
				}
				if (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 1)
				{
					y_offset_s = translation;
				}
			}
		}

		if ((projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 2) || (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 3))
		{
			tpy = scale;
			tpz = translation;
			tpx = scale;

			z_offset_s = translation - translation;
			if (projector.getModuleCount(ZhuYao.itemModuleManipulator) > 0)
			{
				if (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 2)
				{
					z_offset_e = translation;
				}
				if (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 3)
				{
					z_offset_s = translation;
				}
			}
		}
		if ((projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 4) || (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 5))
		{
			tpy = scale;
			tpz = scale;
			tpx = translation;

			x_offset_s = translation - translation;
			if (projector.getModuleCount(ZhuYao.itemModuleManipulator) > 0)
			{
				if (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 4)
				{
					x_offset_e = translation;
				}
				if (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 5)
				{
					x_offset_s = translation;
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

					if ((tpx == translation) && ((projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 4) || (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 5)))
					{
						tpx_temp++;
					}
					if ((tpy == translation) && ((projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 0) || (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 1)))
					{
						tpy_temp++;
					}
					if ((tpz == translation) && ((projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 2) || (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 3)))
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
		if ((item instanceof ItemModuleCamoflage))
		{
			return true;
		}
		if ((item instanceof ItemModuleFusion))
		{
			return true;
		}
		if ((item instanceof ItemModuleManipulator))
		{
			return true;
		}
		if ((item instanceof ItemModuleJammer))
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
		if ((item instanceof ItemModuleShock))
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
		if ((item instanceof ItemModuleFusion))
		{
			return true;
		}
		if ((item instanceof ItemModuleManipulator))
		{
			return true;
		}
		if ((item instanceof ItemModuleJammer))
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
		if ((item instanceof ItemModuleShock))
		{
			return true;
		}

		return false;
	}
}