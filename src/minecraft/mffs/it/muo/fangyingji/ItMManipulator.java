package mffs.it.muo.fangyingji;

import mffs.api.IProjector;
import mffs.it.muo.ItM;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;

public class ItMManipulator extends ItM
{
	public ItMManipulator(int i)
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