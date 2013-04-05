package mffs.item.module.projector;

import java.util.Set;

import mffs.FortronGrid;
import mffs.api.IProjector;
import mffs.api.fortron.IFortronFrequency;
import mffs.item.module.ItemModule;
import universalelectricity.core.vector.Vector3;

public class ItemModuleFusion extends ItemModule
{
	public ItemModuleFusion(int i)
	{
		super(i, "moduleFusion");
		this.setMaxStackSize(1);

	}

	@Override
	public boolean canProject(IProjector projector, Vector3 position)
	{
		Set<IFortronFrequency> machines = FortronGrid.instance().get(((IFortronFrequency) projector).getFrequency());

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