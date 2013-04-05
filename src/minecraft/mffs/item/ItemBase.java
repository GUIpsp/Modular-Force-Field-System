package mffs.item;

import mffs.MFFSConfiguration;
import mffs.MFFSCreativeTab;
import mffs.ModularForceFieldSystem;
import net.minecraft.item.Item;

public class ItemBase extends Item
{
	public ItemBase(int id, String name)
	{
		super(MFFSConfiguration.CONFIGURATION.getItem(name, id).getInt(id));
		this.setUnlocalizedName(ModularForceFieldSystem.PREFIX + name);
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
		this.setNoRepair();
	}
}