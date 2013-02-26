package mffs.common.multitool;

import ic2.api.IWrenchable;
import mffs.common.Functions;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import railcraft.common.api.core.items.ICrowbar;
import universalelectricity.prefab.implement.IToolConfigurator;
import buildcraft.api.tools.IToolWrench;

public class ItemMultiToolWrench extends ItemMultitool implements IToolConfigurator, IToolWrench, ICrowbar
{

	public ItemMultiToolWrench(int id)
	{
		super(id, 0, "multitoolWrench");
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return false;
		}

		TileEntity tileentity = world.getBlockTileEntity(x, y, z);

		if ((tileentity instanceof TileEntityProjector))
		{
			if (((TileEntityProjector) tileentity).isBurnout())
			{
				if (consumePower(stack, 10000, true))
				{
					consumePower(stack, 10000, false);
					((TileEntityProjector) tileentity).setBurnedOut(false);
					Functions.ChattoPlayer(player, "[Multi-Tool] Projector repaired.");
					return true;
				}

				Functions.ChattoPlayer(player, "[Multi-Tool] Not enough Fortron!");
				return false;
			}

		}

		if (((tileentity instanceof IWrenchable)))
		{
			if (consumePower(stack, 1000, true))
			{
				if (((IWrenchable) tileentity).wrenchCanSetFacing(player, side))
				{
					((IWrenchable) tileentity).setFacing((short) side);
					consumePower(stack, 1000, false);
					return true;
				}

			}
			else
			{
				Functions.ChattoPlayer(player, "[Multi-Tool] Not enough Fortron!");
			}

			if (consumePower(stack, 25000, true))
			{
				if (((IWrenchable) tileentity).wrenchCanRemove(player))
				{
					world.spawnEntityInWorld(new EntityItem(world, x, y, z, ((IWrenchable) tileentity).getWrenchDrop(player)));
					world.setBlockWithNotify(x, y, z, 0);
					consumePower(stack, 25000, false);
				}

			}
			else
			{
				Functions.ChattoPlayer(player, "[Multi-Tool] Not enough Fortron!");
			}

		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		return super.onItemRightClick(itemstack, world, entityplayer);
	}

	@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z)
	{
		if (consumePower(player.inventory.getCurrentItem(), 1000, true))
		{
			return true;
		}
		return false;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z)
	{
		consumePower(player.inventory.getCurrentItem(), 1000, false);
	}
}