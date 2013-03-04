package mffs.api;

import java.util.List;
import java.util.Set;

import mffs.common.module.IModule;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.implement.IDisableable;
import universalelectricity.prefab.implement.IRotatable;

public abstract interface IProjector extends IInventory, IRotatable, IDisableable
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

	public int getDeviceID();

	public Set getInteriorPoints();

	public boolean isActive();

	/**
	 * Gets the mode of the projector, mainly the shape and size of it.
	 */
	public IProjectorMode getMode();

	public ItemStack getModeStack();

	public ItemStack getModule(IModule module);

	public int getModuleCount(IModule module, int... slots);

	public int getSidedModuleCount(IModule module, ForgeDirection... direction);

	public List<IModule> getModules();

	public void projectField();

	public void destroyField();

	public List<ItemStack> getModuleStacks();

	/**
	 * Gets the slot IDs based on the direction given.
	 */
	public int[] getSlotsBasedOnDirection(ForgeDirection direction);
	
	/**
	 * Gets the unspecified, direction-unspecific module slots on the left side of the GUI.
	 */
	public int[] getModuleSlots();

}