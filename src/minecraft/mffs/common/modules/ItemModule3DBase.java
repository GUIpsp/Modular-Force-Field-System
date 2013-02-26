package mffs.common.modules;

import java.util.Set;

import mffs.common.ForceFieldTyps;
import mffs.common.IModularProjector;

public abstract class ItemModule3DBase extends ItemModuleBase {

	public ItemModule3DBase(int i, String name) {
		super(i, name);
	}

	@Override
	public void calculateField(IModularProjector projector, Set points) {
	}

	public abstract void calculateField(
			IModularProjector paramIModularProjector, Set paramSet1,
			Set paramSet2);

	@Override
	public ForceFieldTyps getForceFieldTyps() {
		if ((this instanceof ItemModuleContainment)) {
			return ForceFieldTyps.Containment;
		}
		return ForceFieldTyps.Area;
	}
}