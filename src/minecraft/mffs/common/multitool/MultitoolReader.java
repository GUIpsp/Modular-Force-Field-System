package mffs.common.multitool;

import mffs.common.tileentity.TDianRong;
import mffs.common.tileentity.TChouQi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MultitoolReader implements IMultiTool
{

	@Override
	public String getName()
	{
		return "Reader";
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		return null;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{

		if (!player.isSneaking() && world.getBlockTileEntity(x, y, z) instanceof TChouQi)
		{
			TChouQi tile = (TChouQi) world.getBlockTileEntity(x, y, z);
			player.sendChatToPlayer("[Multi-Tool] Forcillium Extractor:");
			player.sendChatToPlayer("Process: " + (int) (100 - ((float) tile.processTime / (float) tile.REQUIRED_TIME) * 100) + "%");
			player.sendChatToPlayer("Fortrons: " + tile.getFortronEnergy());
			player.sendChatToPlayer("Capacity: " + tile.getFortronCapacity());
		}
		if (!player.isSneaking() && world.getBlockTileEntity(x, y, z) instanceof TDianRong)
		{
			TDianRong tile = (TDianRong) world.getBlockTileEntity(x, y, z);
			player.sendChatToPlayer("[Multi-Tool] Fortron Capacitor:");
			player.sendChatToPlayer("Capacity: " + String.valueOf(tile.getFortronEnergy()).concat(" % "));
			player.sendChatToPlayer("Range: " + String.valueOf(tile.getTransmitRange()));
			player.sendChatToPlayer("Linked Devices: ");
		}

		if (player.isSneaking())
			return false;
		else
			return true;
	}

}
