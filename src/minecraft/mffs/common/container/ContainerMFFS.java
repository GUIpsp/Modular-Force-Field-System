package mffs.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerMFFS extends Container
{
	private IInventory inventory;

	public ContainerMFFS(IInventory inventory)
	{
		this.inventory = inventory;
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

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return this.inventory.isUseableByPlayer(entityplayer);
	}

}
