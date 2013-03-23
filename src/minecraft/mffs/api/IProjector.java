package mffs.api;

import java.util.Set;

import mffs.common.module.IModule;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.implement.IDisableable;
import universalelectricity.prefab.implement.IRotatable;

public abstract interface IProjector extends IInventory, IModuleAcceptor, IRotatable, IDisableable, IFortronFrequency
{
	public static enum Slots
	{

		Linkcard(0), TypeMod(1), Option1(2), Option2(3), Option3(4), Distance(5), Strength(6),
		FocusUp(7), FocusDown(8), FocusRight(9), FocusLeft(10), Centerslot(11), SecCard(12);
		public int slot;

		private Slots(int num)
		{
			this.slot = num;
		}
	}

	public Set getInteriorPoints();

	public boolean isActive();

	/**
	 * Gets the mode of the projector, mainly the shape and size of it.
	 */
	public IProjectorMode getMode();

	public ItemStack getModeStack();

	public void projectField();

	public void destroyField();

	/**
	 * Gets the unspecified, direction-unspecific module slots on the left side of the GUI.
	 */
	public int[] getModuleSlots();

	public int getSidedModuleCount(IModule module, ForgeDirection... direction);

	/**
	 * Gets the slot IDs based on the direction given.
	 */
	public int[] getSlotsBasedOnDirection(ForgeDirection direction);

	/**
	 * The amount of fortron being used every tick.
	 * 
	 * @return
	 */
	public int getFortronCost();

}