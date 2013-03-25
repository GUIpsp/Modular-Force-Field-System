package mffs.rongqi;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMFFS extends Container
{
	protected int slotCount = 0;
	private IInventory inventory;

	public ContainerMFFS(IInventory inventory)
	{
		this.inventory = inventory;
		this.slotCount = inventory.getSizeInventory();
		this.inventory.openChest();
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
	{
		this.inventory.closeChest();
	}

	public void addPlayerInventory(EntityPlayer player)
	{
		for (int var3 = 0; var3 < 3; var3++)
		{
			for (int var4 = 0; var4 < 9; var4++)
			{
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 135 + var3 * 18));
			}

		}

		for (int var3 = 0; var3 < 9; var3++)
		{
			this.addSlotToContainer(new Slot(player.inventory, var3, 8 + var3 * 18, 193));
		}
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		ItemStack var2 = null;
		try
		{
			Slot var3 = (Slot) this.inventorySlots.get(par1);

			if (var3 != null && var3.getHasStack())
			{
				ItemStack itemStack = var3.getStack();
				var2 = itemStack.copy();

				if (par1 >= this.slotCount)
				{
					// PLayer Inventory, Try to place into slot.
					boolean didTry = false;

					for (int i = 0; i < this.slotCount; i++)
					{
						if (this.getSlot(i).isItemValid(itemStack))
						{
							didTry = true;

							if (!this.mergeItemStack(itemStack, i, i + 1, false))
							{
							}

						}
					}

					if (!didTry)
					{
						if (par1 < 27 + this.slotCount)
						{
							if (!this.mergeItemStack(itemStack, 27 + this.slotCount, 36 + this.slotCount, false))
							{
								return null;
							}
						}
						else if (par1 >= 27 + this.slotCount && par1 < 36 + this.slotCount && !this.mergeItemStack(itemStack, slotCount, 27 + slotCount, false))
						{
							return null;
						}
					}
				}
				else if (!this.mergeItemStack(itemStack, this.slotCount, 36 + this.slotCount, false))
				{
					return null;
				}

				if (itemStack.stackSize == 0)
				{
					var3.putStack((ItemStack) null);
				}
				else
				{
					var3.onSlotChanged();
				}

				if (itemStack.stackSize == var2.stackSize)
				{
					return null;
				}

				var3.onPickupFromSlot(par1EntityPlayer, itemStack);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return var2;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return this.inventory.isUseableByPlayer(entityplayer);
	}
}
