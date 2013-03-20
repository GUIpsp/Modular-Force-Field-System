package mffs.common.card;

import java.util.List;

import mffs.api.PointXYZ;
import mffs.common.MachineTypes;
import mffs.common.NBTTagCompoundHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class ItemCardDataLink extends ItemCard
{

	public ItemCardDataLink(int id)
	{
		super(id, "cardDataLink");
		setMaxStackSize(1);
	}

	public void setInformation(ItemStack itemStack, PointXYZ png, String key, int value, TileEntity tileentity)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		nbtTagCompound.setString("displayName", MachineTypes.fromTE(tileentity).getName());
		super.setInformation(itemStack, png, key, value);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		NBTTagCompound tag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);

		info.add("Device Type: " + getDeviceTyp(itemStack));
		info.add("Device Name: " + getforAreaname(itemStack));

		if (tag.hasKey("worldname"))
		{
			info.add("World: " + tag.getString("worldname"));
		}
		if (tag.hasKey("linkTarget"))
		{
			info.add("Coords: " + new PointXYZ(tag.getCompoundTag("linkTarget")).toString());
		}
		if (tag.hasKey("valid"))
		{
			info.add(tag.getBoolean("valid") ? "§2Valid" : "§4Invalid");
		}
	}

	public static String getDeviceTyp(ItemStack itemstack)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null)
		{
			return nbtTagCompound.getString("displayName");
		}
		return "-";
	}
}