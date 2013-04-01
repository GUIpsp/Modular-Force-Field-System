package mffs.it;

import mffs.MFFSConfiguration;
import mffs.MFFSCreativeTab;
import mffs.ZhuYao;
import net.minecraft.item.Item;

public class ItB extends Item
{
	public ItB(int id, String name)
	{
		super(MFFSConfiguration.CONFIGURATION.getItem(name, id).getInt(id));
		this.setUnlocalizedName(ZhuYao.PREFIX + name);
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
		this.setNoRepair();
	}
}