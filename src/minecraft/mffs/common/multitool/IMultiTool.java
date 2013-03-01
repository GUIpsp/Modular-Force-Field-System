package mffs.common.multitool;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IMultiTool
{

	public String getName();

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player);

	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity);

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);

}
