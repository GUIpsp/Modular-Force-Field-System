package mffs.common.card;

import mffs.common.item.ItemMFFS;

public abstract class ItKa extends ItemMFFS
{
	public ItKa(int id, String name)
	{
		super(id, name);
		this.setMaxStackSize(1);
	}
}