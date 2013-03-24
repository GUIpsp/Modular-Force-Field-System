package mffs.common.module;

public class ItemModuleScale extends ItM
{
	public ItemModuleScale(int i)
	{
		super(i, "moduleScale");
	}

	@Override
	public float getFortronCost()
	{
		return 0.8f;
	}
}