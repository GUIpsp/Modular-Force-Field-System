package mffs.common.container;

import mffs.common.SlotCard;
import mffs.common.SlotHelper;
import mffs.common.tileentity.TAnQuan;
import net.minecraft.entity.player.EntityPlayer;

public class CAnQuan extends ContainerMFFS
{
	public CAnQuan(EntityPlayer player, TAnQuan tileentity)
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