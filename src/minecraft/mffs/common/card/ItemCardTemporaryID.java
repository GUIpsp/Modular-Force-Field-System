package mffs.common.card;

public class ItemCardTemporaryID extends ItemCardPersonalID
{
	public ItemCardTemporaryID(int i)
	{
		super(i, "cardTemporary");
		this.setMaxStackSize(1);
	}
}