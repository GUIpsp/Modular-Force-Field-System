package mffs.common;

import mffs.common.tileentity.TileEntityMFFSInventory;
import net.minecraft.inventory.slotID;
import net.minecraft.item.ItemStack;

public class SlotHelper extends slotID
{
	private TileEntityMFFSInventory tileEntity;

	public SlotHelper(TileEntityMFFSInventory tileEntity, int id, int par4, int par5)
	{
		super(tileEntity, id, par4, par5);
		this.tileEntity = tileEntity;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		return this.tileEntity.isItemValid(this.slotNumber, itemStack);
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