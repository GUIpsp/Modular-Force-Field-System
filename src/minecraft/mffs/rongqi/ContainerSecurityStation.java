package mffs.rongqi;

import mffs.SlotHelper;
import mffs.item.card.SlotCard;
import mffs.machine.tile.TileSecurityStation;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSecurityStation extends ContainerMFFS
{
	public ContainerSecurityStation(EntityPlayer player, TileSecurityStation tileentity)
	{
		super(tileentity);

		this.addSlotToContainer(new SlotCard(tileentity, 0, 88, 91));

		this.addSlotToContainer(new SlotHelper(tileentity, 1, 8, 35));
		this.addSlotToContainer(new SlotHelper(tileentity, 2, 8, 91));

		for (int var4 = 0; var4 < 9; var4++)
		{
			this.addSlotToContainer(new SlotHelper(tileentity, 3 + var4, 8 + var4 * 18, 111));
		}

		this.addPlayerInventory(player);
	}

}