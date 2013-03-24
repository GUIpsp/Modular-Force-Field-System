package mffs.common.container;

import mffs.common.SlotCard;
import mffs.common.SlotHelper;
import mffs.common.tileentity.TDianRong;
import net.minecraft.entity.player.EntityPlayer;

public class CDianRong extends ContainerMFFS
{
	private TDianRong tileEntity;

	public CDianRong(EntityPlayer player, TDianRong tileEntity)
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