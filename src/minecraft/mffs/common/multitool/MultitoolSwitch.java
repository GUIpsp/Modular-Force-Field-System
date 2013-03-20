package mffs.common.multitool;

import mffs.api.IStatusToggle;
import mffs.common.Functions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MultitoolSwitch implements IMultiTool
{

	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return false;
		}

		TileEntity tileentity = world.getBlockTileEntity(x, y, z);

		if ((tileentity instanceof IStatusToggle))
		{
			// if (SecurityHelper.isAccessGranted(tileentity, entityplayer, world,
			// SecurityRight.EB))
			{
				if (((IStatusToggle) tileentity).canToggle())
				{
					((IStatusToggle) tileentity).onToggle();
					return true;
				}

				Functions.ChattoPlayer(entityplayer, "[Multi-Tool] Machine not set to accept Switch!");
				return false;
			}

		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "Switch";
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		return false;
	}

}