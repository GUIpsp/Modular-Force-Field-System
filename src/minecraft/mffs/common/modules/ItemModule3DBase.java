package mffs.common.modules;

import java.util.Set;

import mffs.common.ForceFieldTyps;
import mffs.common.IModularProjector;

public abstract class ItemModule3DBase extends ItemModuleBase
{
	public ItemModule3DBase(int i)
	{
		super(i);
	}

	public void calculateField(IModularProjector projector, Set points)
	{
	}

	public abstract void calculateField(IModularProjector paramIModularProjector, Set paramSet1, Set paramSet2);

	public ForceFieldTyps getForceFieldTyps()
	{
		if ((this instanceof ItemProjectorModuleContainment))
		{
			return ForceFieldTyps.Containment;
		}
		return ForceFieldTyps.Area;
	}
}