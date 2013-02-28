package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TileEntityCapacitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerCapacitor extends ContainerMFFS
{
	private TileEntityCapacitor tileEntity;

	public ContainerCapacitor(EntityPlayer player, TileEntityCapacitor tileEntity)
	{
		super(tileEntity);
		this.tileEntity = tileEntity;

		this.addSlotToContainer(new SlotHelper(this.tileEntity, 0, 154, 47));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 1, 154, 67));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 2, 9, 74));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 4, 154, 87));

		this.addPlayerInventory(player);
	}
}