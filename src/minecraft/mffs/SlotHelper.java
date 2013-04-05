package mffs;

import mffs.machine.tile.TileInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotHelper extends Slot
{
	protected TileInventory tileEntity;

	public SlotHelper(TileInventory tileEntity, int id, int par4, int par5)
	{
		super(tileEntity, id, par4, par5);
		this.tileEntity = tileEntity;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		return this.tileEntity.isStackValidForSlot(this.slotNumber, itemStack);
	}

	@Override
	public int getSlotStackLimit()
	{
		ItemStack itemStack = this.tileEntity.getStackInSlot(this.slotNumber);

		if (itemStack != null)
		{
			return itemStack.getMaxStackSize();
		}

		return this.tileEntity.getInventoryStackLimit();
	}
}