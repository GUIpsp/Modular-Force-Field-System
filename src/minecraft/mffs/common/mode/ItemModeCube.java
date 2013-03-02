package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
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
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;

public class ItemModeCube extends ItemProjectorMode
{
	public ItemModeCube(int i)
	{
		super(i, "moduleCube");
		this.setIconIndex(53);
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
		TileEntityProjector tileEntity = (TileEntityProjector) projector;

		ForgeDirection direction = tileEntity.getDirection();

		int zDisplaceNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, TileEntityProjector.getSlotsBasedOnDirection(Vector3.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zDisplacePos = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, TileEntityProjector.getSlotsBasedOnDirection(Vector3.getOrientationFromSide(direction, ForgeDirection.SOUTH)));
		
		int xDisplaceNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, TileEntityProjector.getSlotsBasedOnDirection(Vector3.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xDisplacePos = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, TileEntityProjector.getSlotsBasedOnDirection(Vector3.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yDisplacePos = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, TileEntityProjector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yDisplaceNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleDistance, TileEntityProjector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		for (int x = -xDisplaceNeg; x < xDisplacePos + 1; x++)
		{
			for (int z = -zDisplaceNeg; z < zDisplacePos + 1; z++)
			{
				for (int y = -yDisplaceNeg; y <= yDisplacePos; y++)
				{
					if (y == -yDisplaceNeg || y == yDisplacePos || x == -xDisplaceNeg || x == xDisplacePos || z == -zDisplaceNeg || z == zDisplacePos)
					{
						blockDef.add(new Vector3(x, y, z));
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