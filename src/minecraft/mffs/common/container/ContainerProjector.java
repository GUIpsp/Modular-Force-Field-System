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
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 0, 11, 61));

		/**
		 * Force Field Manipulation Matrix. Center slot is the module.
		 */
		for (int drawY = 0; drawY < 3; drawY++)
		{
			for (int drawX = 0; drawX < 3; drawX++)
			{
				this.addSlotToContainer(new SlotHelper(this.tileEntity, drawX + drawY * 3 + 1, drawX * 18 + 116, drawY * 18 + 31));
			}
		}

		this.addPlayerInventory(player);
	}
}