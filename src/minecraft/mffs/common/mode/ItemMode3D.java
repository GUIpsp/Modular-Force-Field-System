package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.common.ForceFieldType;

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