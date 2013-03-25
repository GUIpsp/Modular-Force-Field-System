package mffs.it.ka;

import mffs.api.card.ICard;
import mffs.it.ItemMFFS;

public abstract class ItKa extends ItemMFFS implements ICard
{
	public ItKa(int id, String name)
	{
		super(id, name);
		this.setMaxStackSize(1);
	}
}