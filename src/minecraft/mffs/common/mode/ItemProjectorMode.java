package mffs.common.mode;

import mffs.api.IProjector;
import mffs.api.IProjectorMode;
import mffs.common.item.ItemMFFS;

public abstract class ItemProjectorMode extends ItemMFFS implements IProjectorMode
{
	public ItemProjectorMode(int i, String name)
	{
		super(i, name);
		this.setMaxStackSize(1);
	}

	@Override
	public void render(IProjector projector, double x, double y, double z, float f, long ticks)
	{

	}
}