package mffs.container;

import mffs.SlotHelper;
import mffs.item.card.SlotCard;
import mffs.machine.tile.TileExtractor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerExtractor extends ContainerMFFS
{
	public ContainerExtractor(EntityPlayer player, TileExtractor tileEntity)
	{
		super(tileEntity);

		/**
		 * Fortronite Input
		 */
		this.addSlotToContainer(new SlotHelper(tileEntity, 0, 9, 83));

		/**
		 * Frequency Card
		 */
		this.addSlotToContainer(new SlotCard(tileEntity, 1, 9, 41));

		/**
		 * Upgrades
		 */
		this.addSlotToContainer(new SlotHelper(tileEntity, 2, 154, 67));
		this.addSlotToContainer(new SlotHelper(tileEntity, 3, 154, 87));
		this.addSlotToContainer(new SlotHelper(tileEntity, 4, 154, 47));

		this.addPlayerInventory(player);
	}
}