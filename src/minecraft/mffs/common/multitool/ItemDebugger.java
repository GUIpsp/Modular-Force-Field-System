package mffs.common.multitool;

import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemDebugger extends ItemMultitool
{
	protected StringBuffer info = new StringBuffer();

	public ItemDebugger(int i)
	{
		super(i, 3, false);
	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (world.isRemote)
		{
			if ((tileEntity instanceof TileEntityMFFS))
			{
				System.out.println("client" + ((TileEntityMFFS) tileEntity).isActive());
			}

		}
		else if ((tileEntity instanceof TileEntityMFFS))
		{
			System.out.println("server" + ((TileEntityMFFS) tileEntity).isActive());
		}

		return false;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		return itemstack;
	}
}