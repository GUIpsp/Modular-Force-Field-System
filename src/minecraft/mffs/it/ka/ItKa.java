package mffs.it.ka;

import mffs.api.card.ICard;
import mffs.it.ItB;

public abstract class ItKa extends ItB implements ICard
{
	public ItKa(int id, String name)
	{
		super(id, name);
		this.setMaxStackSize(1);
	}
}