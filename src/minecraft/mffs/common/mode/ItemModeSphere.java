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
import universalelectricity.core.vector.Vector3;

public class ItemModeSphere extends ItemProjectorMode
{
	public ItemModeSphere(int i)
	{
		super(i, "moduleSphere");
		this.setIconIndex(52);
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
	public void calculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior)
	{
		int radius = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale) + 4;

		int yDown = radius;

		if (projector.getModuleCount(ModularForceFieldSystem.itemModuleManipulator) > 0)
		{
			yDown = 0;
		}

		for (int x = -radius; x <= radius; x++)
		{
			for (int z = -radius; z <= radius; z++)
			{
				for (int y = -yDown; y <= radius; y++)
				{

					Vector3 checkPosition = new Vector3(x, y, z);
					double distance = Vector3.distance(new Vector3(), checkPosition);

					if (distance <= radius && distance > radius - 1)
					{
						blockDef.add(checkPosition);
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