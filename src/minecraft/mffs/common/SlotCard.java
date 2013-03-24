package mffs.common;

import icbm.api.IBlockFrequency;
import mffs.common.card.ItKaShengBuo;
import mffs.common.tileentity.TShengBuo;
import net.minecraft.item.ItemStack;

public class SlotCard extends SlotHelper
{
	public SlotCard(TShengBuo tileEntity, int id, int par4, int par5)
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
			if (itemStack.getItem() instanceof ItKaShengBuo)
			{
				((ItKaShengBuo) itemStack.getItem()).setFrequency(((IBlockFrequency) this.tileEntity).getFrequency(), itemStack);
			}
		}
	}
}
