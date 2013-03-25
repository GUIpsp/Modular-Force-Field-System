package mffs.it.ka;

import icbm.api.IBlockFrequency;
import mffs.SBangZhu;
import mffs.jiqi.t.TShengBuo;
import net.minecraft.item.ItemStack;

public class SKa extends SBangZhu
{
	public SKa(TShengBuo tileEntity, int id, int par4, int par5)
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
