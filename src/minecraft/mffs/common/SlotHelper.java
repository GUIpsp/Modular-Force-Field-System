package mffs.common;

import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotHelper extends Slot
{
	private TileEntityMFFS tileEntity;

	public SlotHelper(TileEntityMFFS tileEntity, int id, int par4, int par5)
	{
		super(tileEntity, id, par4, par5);
		this.tileEntity = tileEntity;
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