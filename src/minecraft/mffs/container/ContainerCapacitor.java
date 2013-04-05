package mffs.container;

import mffs.SlotHelper;
import mffs.item.card.SlotCard;
import mffs.machine.tile.TileCapacitor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCapacitor extends ContainerMFFS
{
	private TileCapacitor tileEntity;

	public ContainerCapacitor(EntityPlayer player, TileCapacitor tileEntity)
	{
		super(tileEntity);
		this.tileEntity = tileEntity;

		// Frequency Card
		this.addSlotToContainer(new SlotCard(this.tileEntity, 0, 9, 74));
		this.addSlotToContainer(new SlotCard(this.tileEntity, 1, 27, 74));

		// Upgrades
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 2, 154, 47));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 3, 154, 67));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 4, 154, 87));

		this.addPlayerInventory(player);
	}
}