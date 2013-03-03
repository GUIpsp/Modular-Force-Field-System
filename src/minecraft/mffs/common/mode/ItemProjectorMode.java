package mffs.common.mode;

import mffs.api.IProjectorMode;
import mffs.common.block.BlockForceField.ForceFieldType;
import mffs.common.item.ItemMFFS;

public abstract class ItemProjectorMode extends ItemMFFS implements IProjectorMode
{
	public ItemProjectorMode(int i, String name)
	{
		super(i, name);
		this.setMaxStackSize(1);
	}

	@Override
	public ForceFieldType getForceFieldType()
	{
		return ForceFieldType.Default;
	}
}