package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TileEntityFortronCapacitor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerFortronCapacitor extends ContainerMFFS
{
	private TileEntityFortronCapacitor tileEntity;

	public ContainerFortronCapacitor(EntityPlayer player, TileEntityFortronCapacitor tileEntity)
	{
		super(tileEntity);
		this.tileEntity = tileEntity;

		// Frequency Card
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 0, 9, 74));

		// Upgrades
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 1, 154, 47));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 2, 154, 67));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 3, 154, 87));

		this.addPlayerInventory(player);
	}
}