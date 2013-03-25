package mffs.it.gongju;

import java.util.List;

import mffs.ZhuYao;
import mffs.it.ItemMFFS;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMultitool extends ItemMFFS
{

	public ItemMultitool(int id)
	{
		super(id, "itemMultiTool");
		setMaxStackSize(1);
		setMaxDamage(0);
		this.hasSubtypes = true;

		ToolRegistry.appendMode(new MultitoolSwitch());
		ToolRegistry.appendMode(new MultitoolWrench());
		ToolRegistry.appendMode(new MultitoolReader());
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		return ToolRegistry.getMode(stack.getItemDamage()).onLeftClickEntity(stack, player, entity);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return ToolRegistry.getMode(stack.getItemDamage()).onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
	{

		if (player.isSneaking() && ToolRegistry.getMode(itemstack.getItemDamage() + 1) != null)
		{
			ItemStack newStack = itemstack.copy();
			newStack.setItemDamage(itemstack.getItemDamage() + 1);
			return newStack;
		}
		else if (player.isSneaking())
		{
			ItemStack newStack = itemstack.copy();
			newStack.setItemDamage(0);
			return newStack;
		}
		else
		{
			ToolRegistry.getMode(itemstack.getItemDamage()).onItemRightClick(itemstack, world, player);
			return player.getCurrentEquippedItem();
		}

	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		info.add("Mode: " + ToolRegistry.getMode(itemStack.getItemDamage()).getName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int i, CreativeTabs tabs, List itemList)
	{
		itemList.add(new ItemStack(ZhuYao.itemMultiTool, 1, 0));
	}

}