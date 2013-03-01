package mffs.common.mode;

import mffs.common.block.BlockForceField.ForceFieldType;

public abstract class ItemMode3D extends ItemProjectorMode
{
	public ItemMode3D(int i, String name)
	{
		super(i, name);
	}
	
	@Override
	public ForceFieldType getForceFieldType()
	{
		if ((this instanceof ItemModeContainment))
		{
			return ForceFieldType.Containment;
		}
		return ForceFieldType.Area;
	}
}