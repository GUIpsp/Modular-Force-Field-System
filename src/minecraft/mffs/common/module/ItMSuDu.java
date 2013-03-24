package mffs.common.module;

public class ItMSuDu extends ItM
{
	public ItMSuDu(int i)
	{
		super(i, "moduleSpeed");
	}

	@Override
	public float getFortronCost()
	{
		return 0.2f;
	}
}