package mffs.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemForcillium extends ItemBase
{
	public ItemForcillium(int i)
	{
		super(i, "forcillium");
		this.setMaxStackSize(64);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer entityPlayer, List par3List, boolean par4)
	{
		if (entityPlayer != null)
		{
			if (entityPlayer.username.equalsIgnoreCase("direwolf20"))
			{
				par3List.add("Direwolf20,");
				par3List.add("Please do not read this item wrong!");
				par3List.add("-- Calclavia");
			}
		}
	}

}