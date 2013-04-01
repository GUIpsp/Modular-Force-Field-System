package mffs.it.muo.fangyingji;

import mffs.it.muo.ItM;

public class ItemModuleDisintegration extends ItM
{
	public ItemModuleDisintegration(int i)
	{
		super(i, "moduleDisintegration");
		this.setMaxStackSize(1);
	}

	@Override
	public float getFortronCost()
	{
		return 2f;
	}
}