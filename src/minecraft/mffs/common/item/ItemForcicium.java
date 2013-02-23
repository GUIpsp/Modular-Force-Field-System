package mffs.common.item;

public class ItemForcicium extends ItemMFFSBase
{
	public ItemForcicium(int i)
	{
		super(i);
		this.setIconIndex(97);
		this.setMaxStackSize(64);
	}

	public boolean isRepairable()
	{
		return false;
	}
}