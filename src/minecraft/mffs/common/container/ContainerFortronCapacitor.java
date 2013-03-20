package mffs.common.container;

import mffs.common.SlotFrequency;
import mffs.common.SlotHelper;
import mffs.common.tileentity.TDianRong;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerFortronCapacitor extends ContainerMFFS
{
	private TDianRong tileEntity;

	public ContainerFortronCapacitor(EntityPlayer player, TDianRong tileEntity)
	{
		super(tileEntity);
		this.tileEntity = tileEntity;

		// Frequency Card
		this.addSlotToContainer(new SlotFrequency(this.tileEntity, 0, 9, 74));

		// Upgrades
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 1, 154, 47));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 2, 154, 67));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 3, 154, 87));

		this.addPlayerInventory(player);
	}
}