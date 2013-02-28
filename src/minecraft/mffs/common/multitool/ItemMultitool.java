package mffs.common.multitool;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import mffs.api.IForceEnergyItems;
import mffs.common.ModularForceFieldSystem;
import mffs.common.item.ItemMFFS;

public class ItemMultitool extends ItemMFFS {

	public ItemMultitool(int id) {
		super(id, "itemMultiTool");
		setIconIndex(1);
		setMaxStackSize(1);
		setMaxDamage(100);
		this.hasSubtypes = true;
		
		ToolRegistry.appendMode(new MultitoolSwitch());
	}
	
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return ToolRegistry.getMode(stack.getItemDamage()).onLeftClickEntity(stack, player, entity);
	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return ToolRegistry.getMode(stack.getItemDamage()).onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {

		if (player.isSneaking() && ToolRegistry.getMode(itemstack.getItemDamage()) != null) {
			ItemStack newStack = itemstack.copy();
			newStack.setItemDamage(itemstack.getItemDamage() + 1);
			return newStack;
		}else{
			ToolRegistry.getMode(itemstack.getItemDamage()).onItemRightClick(itemstack, world, player);
			return player.getCurrentEquippedItem();
		}
		
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {
		info.add("Mode: " + ToolRegistry.getMode(itemStack.getItemDamage()).getName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int i, CreativeTabs tabs, List itemList) {
		itemList.add(new ItemStack(ModularForceFieldSystem.itemMultiTool, 1, 0));
	}
	
}