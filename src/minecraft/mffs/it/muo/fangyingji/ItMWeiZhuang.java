package mffs.it.muo.fangyingji;

import mffs.it.muo.ItM;

public class ItMWeiZhuang extends ItM
{
	public ItMWeiZhuang(int i)
	{
		super(i, "moduleCamouflage");
		this.setMaxDamage(1);
	}

	@Override
	public float getFortronCost()
	{
		return 1.5f;
	}
}