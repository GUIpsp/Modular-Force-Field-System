package mffs.common.card;

import java.util.List;

import mffs.common.NBTTagCompoundHelper;
import mffs.common.SecurityRight;
import mffs.common.tileentity.TAnQuan;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

public class ItemAccessCard extends ItemCardPersonalID
{
	public ItemAccessCard(int i)
	{
		super(i, "cardAccess");
		this.setMaxStackSize(1);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
	{/*
	 * if (this.Tick > 1200) { if (getvalidity(itemStack) > 0) { setvalidity(itemStack,
	 * getvalidity(itemStack) - 1);
	 * 
	 * int SEC_ID = getlinkID(itemStack); if (SEC_ID != 0) { TileEntitySecurityStation sec =
	 * (TileEntitySecurityStation)
	 * FrequencyGridOld.getWorldMap(world).getSecStation().get(Integer.valueOf(SEC_ID)); if (sec !=
	 * null) {
	 * 
	 * if (!sec.getDeviceName().equals(getforAreaname(itemStack))) { setforArea(itemStack, sec); }
	 * 
	 * // TODO: REMOVED NAME } } }
	 * 
	 * this.Tick = 0; }
	 */
	}

	public static void setvalidity(ItemStack itemStack, int min)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		nbtTagCompound.setInteger("validity", min);
	}

	public static int getvalidity(ItemStack itemStack)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		if (nbtTagCompound != null)
		{
			return nbtTagCompound.getInteger("validity");
		}
		return 0;
	}

	public static int getlinkID(ItemStack itemstack)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null)
		{
			return nbtTagCompound.getInteger("linkID");
		}
		return 0;
	}

	public static void setlinkID(ItemStack itemStack, TAnQuan sec)
	{
		if (sec != null)
		{
			NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		}
	}

	public static void setforArea(ItemStack itemStack, TAnQuan sec)
	{
		if (sec != null)
		{
			NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
			// nbtTagCompound.setString("Areaname", sec.getDeviceName());
			// TODO: REMOVED NAME
		}
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

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		String SecurityArea = String.format("Security Area: %s ", new Object[] { getforAreaname(itemStack) });
		info.add(SecurityArea);

		String validity = String.format("period of validity: %s min", new Object[] { Integer.valueOf(getvalidity(itemStack)) });
		info.add(validity);
		NBTTagCompound rightsTag;
		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)))
		{
			rightsTag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getCompoundTag("rights");
			info.add("Rights:");
			for (SecurityRight sr : SecurityRight.rights.values())
			{
				if (rightsTag.getBoolean(sr.rightKey))
				{
					info.add("-" + sr.name);
				}
			}
		}
		else
		{
			info.add("Rights: (Hold Shift)");
		}
	}
}