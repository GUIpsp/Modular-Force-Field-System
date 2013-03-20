package mffs.common.card;

import java.util.List;

import mffs.common.NBTTagCompoundHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemCardPersonalID extends ItemCard
{
	public ItemCardPersonalID(int i)
	{
		super(i, "cardPersonalID");
	}

	public ItemCardPersonalID(int i, String name)
	{
		super(i, name);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		info.add("Owner: " + this.getUsername(itemStack));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World par2World, EntityPlayer entityPlayer)
	{
		setUsername(itemStack, entityPlayer.username);
		return itemStack;
	}

	public static void setUsername(ItemStack itemStack, String username)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		nbtTagCompound.setString("name", username);
	}

	public String getUsername(ItemStack itemStack)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);

		if (nbtTagCompound != null)
		{
			return nbtTagCompound.getString("name");
		}

		return "Unknown";
	}
}