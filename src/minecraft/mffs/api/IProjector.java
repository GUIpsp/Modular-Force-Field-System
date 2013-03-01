package mffs.api;

import java.util.List;
import java.util.Set;

import mffs.common.module.IModule;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.implement.IRotatable;

public abstract interface IProjector extends IInventory, IRotatable
{
	public int countItemsInSlot(Slots paramSlots);

	public int getDeviceID();

	public Set getInteriorPoints();

	public void setBurnedOut(boolean paramBoolean);

	public boolean isActive();

	/**
	 * Gets the mode of the projector, mainly the shape and size of it.
	 */
	public IProjectorMode getMode();

	public ItemStack getModeStack();

	public ItemStack getModule(IModule module);

	public List<IModule> getModules();

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
}