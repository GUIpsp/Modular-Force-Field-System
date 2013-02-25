package mffs.common.item;

public class ItemForcicium extends ItemMFFS
{
	public ItemForcicium(int i)
	{
		super(i, "forcicium");
		this.setIconIndex(97);
		this.setMaxStackSize(64);
	}

        @Override
	public boolean isRepairable()
	{
		return false;
	}
}