package mffs.common.card;

import java.util.List;

import mffs.api.PointXYZ;
import mffs.common.NBTTagCompoundHelper;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

public abstract class ItemCard extends ItemMFFS
{
	public ItemCard(int id, String name)
	{
		super(id, name);
		this.setMaxStackSize(1);
	}

	public static String getforAreaname(ItemStack itemstack)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null)
		{
			return nbtTagCompound.getString("Areaname");
		}
		return "not set";
	}

	public boolean isvalid(ItemStack itemStack)
	{
		NBTTagCompound tag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		if (tag.hasKey("valid"))
		{
			return tag.getBoolean("valid");
		}
		return false;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		NBTTagCompound tag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
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
			info.add(tag.getBoolean("valid") ? "\u00a72Valid" : "\u00a74Invalid");
		}
	}

	public void setInformation(ItemStack itemStack, PointXYZ png, String key, int value)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);

		nbtTagCompound.setInteger(key, value);
		nbtTagCompound.setString("worldname", DimensionManager.getWorld(png.dimensionId).getWorldInfo().getWorldName());
		nbtTagCompound.setTag("linkTarget", png.asNBT());
		nbtTagCompound.setBoolean("valid", true);
	}

	public int getValue(String key, ItemStack itemStack)
	{
		NBTTagCompound tag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);

		if (tag.hasKey(key))
		{
			return tag.getInteger(key);
		}

		return 0;
	}

	public PointXYZ getCardTargetPoint(ItemStack itemStack)
	{
		NBTTagCompound tag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		if (tag.hasKey("linkTarget"))
		{
			return new PointXYZ(tag.getCompoundTag("linkTarget"));
		}
		tag.setBoolean("valid", false);

		return null;
	}
}