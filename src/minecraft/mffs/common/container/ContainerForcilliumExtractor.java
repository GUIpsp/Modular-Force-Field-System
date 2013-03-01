package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TileEntityForcilliumExtractor;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerForcilliumExtractor extends ContainerMFFS
{
	private TileEntityForcilliumExtractor tileEntity;
	private int workCylce;
	private int workdone;
	private int forceEnergybuffer;

	public ContainerForcilliumExtractor(EntityPlayer player, TileEntityForcilliumExtractor tileentity)
	{
		super(tileentity);
		this.tileEntity = tileentity;
		this.workCylce = -1;
		this.workdone = -1;
		this.forceEnergybuffer = -1;

		/**
		 * Focillium Input
		 */
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 0, 9, 83));

		/**
		 * Frequency Card
		 */
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 1, 9, 41));

		/**
		 * Upgrades
		 */
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 2, 154, 67));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 3, 154, 87));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 4, 154, 47));

		this.slotCount = 5;

		this.addPlayerInventory(player);
	}
}