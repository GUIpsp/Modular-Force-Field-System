package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerProjector extends ContainerMFFS
{
	private TileEntityProjector tileEntity;

	public ContainerProjector(EntityPlayer player, TileEntityProjector tileEntity)
	{
		super(tileEntity);
		this.tileEntity = tileEntity;

		/**
		 * Frequency Card
		 */
		addSlotToContainer(new SlotHelper(this.tileEntity, 0, 11, 61));

		/**
		 * Matrix & Direction
		 */
		addSlotToContainer(new SlotHelper(this.tileEntity, 1, 11, 38));
		addSlotToContainer(new SlotHelper(this.tileEntity, 2, 120, 82));
		addSlotToContainer(new SlotHelper(this.tileEntity, 3, 138, 82));
		addSlotToContainer(new SlotHelper(this.tileEntity, 4, 156, 82));

		/**
		 * Upgrades
		 */
		addSlotToContainer(new SlotHelper(this.tileEntity, 5, 119, 64));
		addSlotToContainer(new SlotHelper(this.tileEntity, 6, 155, 64));
		addSlotToContainer(new SlotHelper(this.tileEntity, 7, 137, 28));
		addSlotToContainer(new SlotHelper(this.tileEntity, 8, 137, 62));
		addSlotToContainer(new SlotHelper(this.tileEntity, 9, 154, 45));
		addSlotToContainer(new SlotHelper(this.tileEntity, 10, 120, 45));
		addSlotToContainer(new SlotHelper(this.tileEntity, 11, 137, 45));
		addSlotToContainer(new SlotHelper(this.tileEntity, 12, 92, 38));
		addSlotToContainer(new SlotHelper(this.tileEntity, 13, 92, 38));

		this.addPlayerInventory(player);
	}
}