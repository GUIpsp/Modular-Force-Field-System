package mffs.common.card;

import java.util.List;

import mffs.api.IIdentificationCard;
import mffs.api.SecurityPermission;
import mffs.common.NBTTagCompoundHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItCardIdentification extends ItemCard implements IIdentificationCard
{
	public ItCardIdentification(int i)
	{
		super(i, "cardIdentification");
	}

	public ItCardIdentification(int i, String name)
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

	@Override
	public void setUsername(ItemStack itemStack, String username)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		nbtTagCompound.setString("name", username);
	}

	@Override
	public String getUsername(ItemStack itemStack)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);

		if (nbtTagCompound != null)
		{
			return nbtTagCompound.getString("name");
		}

		return "Unknown";
	}

	@Override
	public boolean hasPermission(ItemStack itemStack, SecurityPermission permission)
	{
		return false;
	}

	@Override
	public boolean addPermission(ItemStack itemStack, SecurityPermission permission)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removePermission(ItemStack itemStack, SecurityPermission permission)
	{
		// TODO Auto-generated method stub
		return false;
	}
}