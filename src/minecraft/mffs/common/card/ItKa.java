package mffs.common.card;

import mffs.api.ICard;
import mffs.common.item.ItemMFFS;

public abstract class ItKa extends ItemMFFS implements ICard
{
	public ItKa(int id, String name)
	{
		super(id, name);
		this.setMaxStackSize(1);
	}
}