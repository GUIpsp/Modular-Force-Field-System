package mffs.it.muo;

import java.util.List;

import mffs.ZhuYao;
import mffs.api.modules.IModule;
import mffs.it.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.TranslationHelper;

public abstract class ItM extends ItemMFFS implements IModule
{
	public ItM(int id, String name)
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
		return 0.5f;
	}

}