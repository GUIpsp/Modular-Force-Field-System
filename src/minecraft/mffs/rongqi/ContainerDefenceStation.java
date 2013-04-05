package mffs.rongqi;

import mffs.SlotHelper;
import mffs.item.card.SlotCard;
import mffs.machine.tile.TileDefenceStation;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerDefenceStation extends ContainerMFFS
{
	public ContainerDefenceStation(EntityPlayer player, TileDefenceStation tileEntity)
	{
		super(tileEntity);

		/**
		 * Frequency Card
		 */
		this.addSlotToContainer(new SlotCard(tileEntity, 0, 87, 89));
		this.addSlotToContainer(new SlotHelper(tileEntity, 1, 69, 89));

		/**
		 * Module slots.
		 */
		for (int var3 = 0; var3 < 2; var3++)
		{
			for (int var4 = 0; var4 < 4; var4++)
			{
				this.addSlotToContainer(new SlotHelper(tileEntity, var4 + var3 * 4 + 2, 99 + var4 * 18, 31 + var3 * 18));
			}
		}

		/**
		 * Item filter slots.
		 */
		for (int var4 = 0; var4 < 9; var4++)
		{
			this.addSlotToContainer(new SlotHelper(tileEntity, var4 + 8 + 2, 9 + var4 * 18, 69));
		}

		this.addPlayerInventory(player);
	}
}