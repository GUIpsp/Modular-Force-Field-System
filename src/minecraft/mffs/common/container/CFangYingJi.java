package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.entity.player.EntityPlayer;

public class CFangYingJi extends ContainerMFFS
{
	public CFangYingJi(EntityPlayer player, TFangYingJi tileEntity)
	{
		super(tileEntity);

		/**
		 * Frequency Card
		 */
		addSlotToContainer(new SlotHelper(tileEntity, 0, 76, 89));

		/**
		 * Force Field Manipulation Matrix. Center slot is the mode.
		 */
		// Up
		addSlotToContainer(new SlotHelper(tileEntity, 1, 18 + 81, 21));
		addSlotToContainer(new SlotHelper(tileEntity, 2, 18 + 101, 21));

		// Left
		addSlotToContainer(new SlotHelper(tileEntity, 3, 18 + 61, 36));
		addSlotToContainer(new SlotHelper(tileEntity, 4, 18 + 61, 56));

		// Mode
		addSlotToContainer(new SlotHelper(tileEntity, 5, 18 + 91, 18 + 26));

		// Right
		addSlotToContainer(new SlotHelper(tileEntity, 6, 139, 36));
		addSlotToContainer(new SlotHelper(tileEntity, 7, 139, 56));

		// Down
		addSlotToContainer(new SlotHelper(tileEntity, 7, 18 + 81, 67));
		addSlotToContainer(new SlotHelper(tileEntity, 8, 18 + 101, 67));

		/**
		 * Y Axis
		 */
		addSlotToContainer(new SlotHelper(tileEntity, 10, 56, 41));
		addSlotToContainer(new SlotHelper(tileEntity, 11, 38, 41));
		addSlotToContainer(new SlotHelper(tileEntity, 12, 56, 61));
		addSlotToContainer(new SlotHelper(tileEntity, 13, 38, 61));

		/**
		 * Misc
		 */
		addSlotToContainer(new SlotHelper(tileEntity, 14, 18, 41));
		addSlotToContainer(new SlotHelper(tileEntity, 15, 18, 61));

		addPlayerInventory(player);
	}
}