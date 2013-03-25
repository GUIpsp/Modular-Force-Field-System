package mffs;

import mffs.jiqi.t.TileEntityMFFSInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SBangZhu extends Slot
{
	protected TileEntityMFFSInventory tileEntity;

	public SBangZhu(TileEntityMFFSInventory tileEntity, int id, int par4, int par5)
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