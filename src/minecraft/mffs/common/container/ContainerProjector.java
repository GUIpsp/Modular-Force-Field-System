package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerProjector extends ContainerMFFS
{
	private TFangYingJi tileEntity;

	public ContainerProjector(EntityPlayer player, TFangYingJi tileEntity)
	{
		super(tileEntity);
		this.tileEntity = tileEntity;

		/**
		 * Frequency Card
		 */
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 0, 76, 89));

		/**
		 * Force Field Manipulation Matrix. Center slot is the mode.
		 */
		// Up
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 1, 18 + 81, 21));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 2, 18 + 101, 21));

		// Left
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 3, 18 + 61, 36));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 4, 18 + 61, 56));

		// Mode
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 5, 18 + 91, 18 + 26));

		// Right
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 6, 139, 36));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 7, 139, 56));

		// Down
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 7, 18 + 81, 67));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 8, 18 + 101, 67));

		/**
		 * Y Axis
		 */
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 10, 56, 41));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 11, 38, 41));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 12, 56, 61));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 13, 38, 61));

		/**
		 * Misc
		 */
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 14, 18, 41));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 15, 18, 61));

		this.addPlayerInventory(player);
	}
}