package mffs.common;

import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotHelper extends Slot
{

	private TileEntityMFFS tileEntity;

	public SlotHelper(IInventory par2IInventory, int id, int par4, int par5)
	{
		super(par2IInventory, id, par4, par5);
		this.tileEntity = ((TileEntityMFFS) par2IInventory);
	}

	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		return this.tileEntity.isItemValid(itemStack, this.slotNumber);
	}

	@Override
	public int getSlotStackLimit()
	{
		return this.tileEntity.getSlotStackLimit(this.slotNumber);
	}
}