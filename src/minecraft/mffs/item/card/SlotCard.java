package mffs.item.card;

import icbm.api.IBlockFrequency;
import mffs.SlotHelper;
import mffs.machine.tile.TileFrequency;
import net.minecraft.item.ItemStack;

public class SlotCard extends SlotHelper
{
	public SlotCard(TileFrequency tileEntity, int id, int par4, int par5)
	{
		super(tileEntity, id, par4, par5);
	}

	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();
		ItemStack itemStack = this.getStack();

		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof ItemCardFrequency)
			{
				((ItemCardFrequency) itemStack.getItem()).setFrequency(((IBlockFrequency) this.tileEntity).getFrequency(), itemStack);
			}
		}
	}
}
