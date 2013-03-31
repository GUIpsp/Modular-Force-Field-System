package mffs.it.muo.fangyingji;

import java.util.Map;
import java.util.Set;

import mffs.LiGuanLi;
import mffs.api.IProjector;
import mffs.api.fortron.IFortronFrequency;
import mffs.it.muo.ItM;
import mffs.jiqi.t.TFangYingJi;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleFusion extends ItM
{
	public ItemModuleFusion(int i)
	{
		super(i, "moduleFusion");
	}

	@Override
	public boolean canProject(IProjector projector, Vector3 position)
	{
		Set<IFortronFrequency> machines = LiGuanLi.INSTANCE.get(((IFortronFrequency) projector).getFrequency());

		for (IFortronFrequency machine : machines)
		{
			if (machine instanceof IProjector && machine != projector)
			{
				if (((IProjector) machine).getInteriorPoints().contains(position))
				{
					return false;
				}
			}
		}

		return true;
	}
}