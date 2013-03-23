package mffs.common.module;

import java.util.List;

import mffs.common.ZhuYao;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.TranslationHelper;

public abstract class ItemModule extends ItemMFFS implements IModule
{
	public ItemModule(int id, String name)
	{
		super(id, name);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		String tooltip = TranslationHelper.getLocal(this.getUnlocalizedName() + ".tooltip");

		if (tooltip != null && tooltip.length() > 0)
		{
			info.addAll(ZhuYao.splitStringPerWord(tooltip, 5));
		}
	}

	@Override
	public float getFortronCost()
	{
		return 1;
	}

}