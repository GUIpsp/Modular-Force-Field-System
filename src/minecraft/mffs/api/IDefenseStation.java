package mffs.api;

import java.util.Set;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IDefenseStation extends IInventory, IFortronFrequency, IModuleAcceptor
{

	/**
	 * The range in which the defense station starts warning the player.
	 * 
	 * @return
	 */
	public int getWarningRange();

	/**
	 * The range in which the defense station has an effect on.
	 * 
	 * @return
	 */
	public int getActionRange();

	public boolean mergeIntoInventory(ItemStack itemStack);

	public Set<ItemStack> getFilteredItems();

	/**
	 * 
	 * @return True if the filtering is on ban mode. False if it is on allow-only mode.
	 */
	public boolean getFilterMode();

	int getFortronCost();

}
