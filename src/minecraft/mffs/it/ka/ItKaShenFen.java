package mffs.it.ka;

import java.util.List;

import mffs.ZhuYao;
import mffs.api.SecurityPermission;
import mffs.api.card.ICardIdentification;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.prefab.TranslationHelper;

public class ItKaShenFen extends ItKa implements ICardIdentification
{
	private static final String NBT_PREFIX = "mffs_permission_";

	public ItKaShenFen(int i)
	{
		super(i, "cardIdentification");
	}

	public ItKaShenFen(int i, String name)
	{
		super(i, name);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		if (this.getUsername(itemStack) != null)
		{
			info.add("Username: " + this.getUsername(itemStack));
		}
		else
		{
			info.add("Unspecified");
		}

		String tooltip = "";

		for (SecurityPermission permission : SecurityPermission.values())
		{
			if (this.hasPermission(itemStack, permission))
			{
				if (permission != SecurityPermission.values()[0])
				{
					tooltip += ", ";
				}

				tooltip += "\u00a72" + TranslationHelper.getLocal("gui." + permission.name + ".name");
			}
		}
		if (tooltip != null && tooltip.length() > 0)
		{
			info.addAll(ZhuYao.splitStringPerWord(tooltip, 5));
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World par2World, EntityPlayer entityPlayer)
	{
		this.setUsername(itemStack, entityPlayer.username);
		return itemStack;
	}

	@Override
	public void setUsername(ItemStack itemStack, String username)
	{
		NBTTagCompound nbtTagCompound = ZhuYao.getNBTTagCompound(itemStack);
		nbtTagCompound.setString("name", username);
	}

	@Override
	public String getUsername(ItemStack itemStack)
	{
		NBTTagCompound nbtTagCompound = ZhuYao.getNBTTagCompound(itemStack);

		if (nbtTagCompound != null)
		{
			if (nbtTagCompound.getString("name") != "")
			{
				return nbtTagCompound.getString("name");
			}
		}

		return null;
	}

	@Override
	public boolean hasPermission(ItemStack itemStack, SecurityPermission permission)
	{
		NBTTagCompound nbt = ZhuYao.getNBTTagCompound(itemStack);
		return nbt.getBoolean(NBT_PREFIX + permission.ordinal());
	}

	@Override
	public boolean addPermission(ItemStack itemStack, SecurityPermission permission)
	{
		NBTTagCompound nbt = ZhuYao.getNBTTagCompound(itemStack);
		nbt.setBoolean(NBT_PREFIX + permission.ordinal(), true);
		return false;
	}

	@Override
	public boolean removePermission(ItemStack itemStack, SecurityPermission permission)
	{
		NBTTagCompound nbt = ZhuYao.getNBTTagCompound(itemStack);
		nbt.setBoolean(NBT_PREFIX + permission.ordinal(), false);
		return false;
	}
}