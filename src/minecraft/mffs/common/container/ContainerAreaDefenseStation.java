package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TileEntityDefenseStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAreaDefenseStation extends ContainerMFFS
{
	public ContainerAreaDefenseStation(EntityPlayer player, TileEntityDefenseStation tileentity)
	{
		super(tileentity);

		this.addSlotToContainer(new SlotHelper(tileentity, 0, 13, 27));
		this.addSlotToContainer(new SlotHelper(tileentity, 1, 97, 27));

		this.addSlotToContainer(new SlotHelper(tileentity, 2, 14, 51));
		this.addSlotToContainer(new SlotHelper(tileentity, 3, 14, 88));

		for (int var3 = 0; var3 < 2; var3++)
		{
			for (int var4 = 0; var4 < 4; var4++)
			{
				this.addSlotToContainer(new SlotHelper(tileentity, var4 + var3 * 4 + 5, 176 + var4 * 18, 26 + var3 * 18));
			}
		}

		for (int var3 = 0; var3 < 5; var3++)
		{
			for (int var4 = 0; var4 < 4; var4++)
			{
				this.addSlotToContainer(new SlotHelper(tileentity, var4 + var3 * 4 + 15, 176 + var4 * 18, 98 + var3 * 18));
			}
		}

		for (int var3 = 0; var3 < 3; var3++)
		{
			for (int var4 = 0; var4 < 9; var4++)
			{
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 134 + var3 * 18));
			}
		}

		for (int var3 = 0; var3 < 9; var3++)
		{
			this.addSlotToContainer(new Slot(player.inventory, var3, 8 + var3 * 18, 192));
		}
	}
}