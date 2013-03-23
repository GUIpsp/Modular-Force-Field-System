package mffs.common.item;

import java.util.List;

import mffs.api.IItemFortronStorage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.item.ElectricItemHelper;

public abstract class ItemFortron extends ItemMFFS implements IItemFortronStorage
{
	public ItemFortron(int id, String name)
	{
		super(id, name);
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
		this.setNoRepair();
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
	{
		String color = "";
		double joules = this.getFortronEnergy(itemStack);

		if (joules <= this.getFortronCapacity(itemStack) / 3)
		{
			color = "\u00a74";
		}
		else if (joules > this.getFortronCapacity(itemStack) * 2 / 3)
		{
			color = "\u00a72";
		}
		else
		{
			color = "\u00a76";
		}

		list.add(color + ElectricityDisplay.getDisplay(joules, ElectricUnit.JOULES) + "/" + ElectricityDisplay.getDisplay(this.getFortronCapacity(itemStack), ElectricUnit.JOULES));
	}

	/**
	 * Makes sure the item is uncharged when it is crafted and not charged. Change this if you do
	 * not want this to happen!
	 */
	@Override
	public void onCreated(ItemStack itemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		itemStack = ElectricItemHelper.getUncharged(itemStack);
	}

	/**
	 * This function sets the electricity. Do not directly call this function. Try to use
	 * onReceiveElectricity or onUseElectricity instead.
	 * 
	 * @param joules - The amount of electricity in joules
	 */
	@Override
	public void setFortronEnergy(int joules, ItemStack itemStack)
	{
		// Saves the frequency in the ItemStack
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		int stored = Math.max(Math.min(joules, this.getFortronCapacity(itemStack)), 0);
		itemStack.getTagCompound().setInteger("energy", stored);

		/**
		 * Sets the damage as a percentage to render the bar properly.
		 */
		itemStack.setItemDamage((int) (100 - (stored / getFortronCapacity(itemStack)) * 100));
	}

	/**
	 * This function is called to get the electricity stored in this item
	 * 
	 * @return - The amount of electricity stored in watts
	 */
	@Override
	public int getFortronEnergy(ItemStack itemStack)
	{
		if (itemStack.getTagCompound() == null)
		{
			return 0;
		}

		int stored = itemStack.getTagCompound().getInteger("energy");

		/**
		 * Sets the damage as a percentage to render the bar properly.
		 */
		itemStack.setItemDamage((int) (100 - (stored / getFortronCapacity(itemStack)) * 100));
		return stored;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		// Add an uncharged version of the electric item
		ItemStack itemStack = new ItemStack(this);
		this.setFortronEnergy(0, itemStack);
		par3List.add(itemStack);
		// Add an electric item to the creative list that is fully charged
		ItemStack chargedItem = new ItemStack(this);
		this.setFortronEnergy(this.getFortronCapacity(chargedItem), chargedItem);
		par3List.add(chargedItem);
	}
}