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
		this.setItemName(name);
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
		this.setTextureFile(ModularForceFieldSystem.ITEM_TEXTURE_FILE);
                this.setMaxStackSize(64);
                this.setNoRepair();
	}
}