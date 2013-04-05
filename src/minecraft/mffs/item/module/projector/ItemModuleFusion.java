package mffs.item.module.projector;

import java.util.Iterator;
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
	public void onCalculate(IProjector projector, Set<Vector3> fieldDefinition, Set<Vector3> fieldInterior)
	{
		Set<IFortronFrequency> machines = FortronGrid.instance().get(((IFortronFrequency) projector).getFrequency());

		for (IFortronFrequency machine : machines)
		{
			if (machine instanceof IProjector && machine != projector)
			{
				Iterator<Vector3> it = fieldDefinition.iterator();

				while (it.hasNext())
				{
					Vector3 position = it.next();

					if (((IProjector) machine).getInteriorPoints().contains(position))
					{
						it.remove();
					}
				}
			}
		}
	}
}