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

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); i++)
		{
			ICrafting icrafting = (ICrafting) crafters.get(i);
			icrafting.sendProgressBarUpdate(this, 1, tileEntity.getLinkedProjector());
			icrafting.sendProgressBarUpdate(this, 2, tileEntity.getStorageAvailablePower() & 0xffff);
			icrafting.sendProgressBarUpdate(this, 3, tileEntity.getStorageAvailablePower() >>> 16);
			icrafting.sendProgressBarUpdate(this, 4, tileEntity.getPercentageStorageCapacity());
			icrafting.sendProgressBarUpdate(this, 6, tileEntity.getPowerLinkMode());

		}
	}

	@Override
	public void updateProgressBar(int i, int j)
	{
		switch (i)
		{
			case 1:
				tileEntity.setLinketprojektor((short) j);
				break;
			case 2:
				tileEntity.setForcePower((tileEntity.getStorageAvailablePower() & 0xffff0000) | j);
				break;
			case 3:
				tileEntity.setForcePower((tileEntity.getStorageAvailablePower() & 0xffff) | (j << 16));
				break;
			case 4:
				tileEntity.setCapacity(j);
				break;
			case 6:
				tileEntity.setPowerLinkMode(j);
				break;
		}
	}
}