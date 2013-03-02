package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.common.ModularForceFieldSystem;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemModuleAntibiotic;
import mffs.common.module.ItemModuleCamoflage;
import mffs.common.module.ItemModuleDisintegration;
import mffs.common.module.ItemModuleDefenseStation;
import mffs.common.module.ItemModuleFusion;
import mffs.common.module.ItemModuleManipulator;
import mffs.common.module.ItemModuleJammer;
import mffs.common.module.ItemModuleSponge;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;

public class ItemModeCube extends ItemProjectorMode
{
	public ItemModeCube(int i)
	{
		super(i, "modeCube");
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

		int zDisplaceNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(Vector3.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zDisplacePos = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(Vector3.getOrientationFromSide(direction, ForgeDirection.SOUTH)));
		
		int xDisplaceNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(Vector3.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xDisplacePos = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(Vector3.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yDisplacePos = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yDisplaceNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

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