package mffs.item.module.projector;

import mffs.api.IProjector;
import mffs.item.module.ItemModule;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;

public class ItemModuleManipulator extends ItemModule
{
	public ItemModuleManipulator(int i)
	{
		super(i, "moduleManipulator");
	}

	@Override
	public boolean onCalculate(IProjector projector, Vector3 position)
	{
		if (position.y < ((TileEntity) projector).yCoord)
		{
			return false;
		}

		return true;
	}

}