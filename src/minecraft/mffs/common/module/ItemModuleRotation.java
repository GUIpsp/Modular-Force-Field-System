package mffs.common.module;

public class ItemModuleRotation extends ItM
{
	public ItemModuleRotation(int id)
	{
		super(id, "moduleRotation");
	}

	@Override
	public float getFortronCost()
	{
		return 0.1f;
	}
}