package mffs.common.container;

import mffs.common.SlotFrequency;
import mffs.common.SlotHelper;
import mffs.common.tileentity.TFangYu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class CFangYu extends ContainerMFFS
{
	public CFangYu(EntityPlayer player, TFangYu tileEntity)
	{
		super(tileEntity);

		/**
		 * Frequency Card
		 */
		this.addSlotToContainer(new SlotHelper(tileEntity, 0, 76, 89));

		this.addSlotToContainer(new SlotHelper(tileEntity, 1, 97, 27));

		/**
		 * Item filter slots.
		 */
		for (int var3 = 0; var3 < 2; var3++)
		{
			for (int var4 = 0; var4 < 9; var4++)
			{
				this.addSlotToContainer(new SlotHelper(tileEntity, var4 + var3 * 9 + 1, 9 + var4 * 18, 51 + var3 * 18));
			}
		}

		this.addPlayerInventory(player);
	}
}