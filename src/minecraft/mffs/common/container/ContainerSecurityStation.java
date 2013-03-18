package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TAnQuan;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSecurityStation extends ContainerMFFS
{

	private TAnQuan secStation;

	public ContainerSecurityStation(EntityPlayer player, TAnQuan tileentity)
	{

		super(tileentity);
		this.secStation = tileentity;

		this.addSlotToContainer(new SlotHelper(tileentity, 0, 8, 31));
		this.addSlotToContainer(new SlotHelper(tileentity, 1, 8, 91));

		for (int var4 = 0; var4 < 9; var4++)
		{
			this.addSlotToContainer(new SlotHelper(tileentity, 2 + var4, 8 + var4 * 18, 111));
		}

		this.addPlayerInventory(player);

	}

}