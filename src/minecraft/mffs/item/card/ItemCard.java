package mffs.item.card;

import mffs.api.card.ICard;
import mffs.item.ItemBase;

public abstract class ItemCard extends ItemBase implements ICard
{
	public ItemCard(int id, String name)
	{
		super(id, name);
		this.setMaxStackSize(1);
	}
}