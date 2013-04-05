package mffs.item.module.projector;

import mffs.item.module.ItemModule;

public class ItemModuleDisintegration extends ItemModule
{
	public ItemModuleDisintegration(int i)
	{
		super(i, "moduleDisintegration");
		this.setMaxStackSize(1);
		this.setCost(2f);
	}
}