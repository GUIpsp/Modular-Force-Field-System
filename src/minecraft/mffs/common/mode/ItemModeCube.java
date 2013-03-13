
package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.common.ZhuYao;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemModuleAntibiotic;
import mffs.common.module.ItemModuleCamoflage;
import mffs.common.module.ItemModuleDefenseStation;
import mffs.common.module.ItemModuleDisintegration;
import mffs.common.module.ItemModuleFusion;
import mffs.common.module.ItemModuleJammer;
import mffs.common.module.ItemModuleManipulator;
import mffs.common.module.ItemModuleSponge;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

public class ItemModeCube extends ItemProjectorMode
{
	public ItemModeCube(int i)
	{
		super(i, "modeCube");
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
		ForgeDirection direction = projector.getDirection(((TileEntity) projector).worldObj, ((TileEntity) projector).xCoord, ((TileEntity) projector).yCoord, ((TileEntity) projector).zCoord);

		int zScaleNeg = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zScalePos = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));

		int xScaleNeg = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xScalePos = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yScalePos = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yScaleNeg = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		int overAllIncrease = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getModuleSlots());
		zScaleNeg += overAllIncrease;
		zScalePos += overAllIncrease;
		
		xScaleNeg += overAllIncrease;
		xScalePos += overAllIncrease;
		
		yScalePos += overAllIncrease;
		yScaleNeg += overAllIncrease;
		
		int zTranslationNeg = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zTranslationPos = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));

		int xTranslationNeg = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xTranslationPos = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yTranslationPos = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yTranslationNeg = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		Vector3 translation = new Vector3(xTranslationPos - xTranslationNeg, yTranslationPos - yTranslationNeg, zTranslationPos - zTranslationNeg);

		for (int x = -xScaleNeg; x < xScalePos + 1; x++)
		{
			for (int z = -zScaleNeg; z < zScalePos + 1; z++)
			{
				for (int y = -yScaleNeg; y <= yScalePos; y++)
				{
					if (y == -yScaleNeg || y == yScalePos || x == -xScaleNeg || x == xScalePos || z == -zScaleNeg || z == zScalePos)
					{
						blockDef.add(Vector3.add(translation, new Vector3(x, y, z)));
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
		if ((item instanceof ItemModuleDefenseStation))
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
		if ((item instanceof ItemModuleManipulator))
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