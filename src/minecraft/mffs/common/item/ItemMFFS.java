package mffs.common.item;

import mffs.common.MFFSConfiguration;
import mffs.common.MFFSCreativeTab;
import mffs.common.ModularForceFieldSystem;
import net.minecraft.item.Item;

public class ItemMFFS extends Item
{
	public ItemMFFS(int id, String name)
	{
		super(MFFSConfiguration.CONFIGURATION.getItem(name, id).getInt(id));
		this.setUnlocalizedName(ModularForceFieldSystem.PREFIX + name);
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
		this.setNoRepair();
	}
}