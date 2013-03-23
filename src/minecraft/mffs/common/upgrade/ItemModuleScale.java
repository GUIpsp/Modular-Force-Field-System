package mffs.common.upgrade;

import mffs.common.module.ItemModule;

public class ItemModuleScale extends ItemModule
{
	public ItemModuleScale(int i)
	{
		super(i, "moduleScale");
	}

	@Override
	public float getFortronCost()
	{
		return 5;
	}
}