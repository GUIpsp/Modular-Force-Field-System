package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TileEntityCapacitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCapacitor extends ContainerMFFS
{
	private TileEntityCapacitor tileEntity;

	public ContainerCapacitor(EntityPlayer player, TileEntityCapacitor tileEntity)
	{
		super(tileEntity);
		this.tileEntity = tileEntity;

		this.addSlotToContainer(new SlotHelper(this.tileEntity, 0, 154, 47));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 1, 154, 67));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 2, 9, 74));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 4, 154, 87));

		this.addPlayerInventory(player);
	}

        @Override
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotID)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotID);
		
		if ((slot != null) && (slot.getHasStack()))
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (itemstack1.stackSize == 0)
				slot.putStack(null);
			else
			{
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize != itemstack.stackSize)
				slot.onSlotChanged();
			else
			{
				return null;
			}
		}
		
		return itemstack;
	}
}