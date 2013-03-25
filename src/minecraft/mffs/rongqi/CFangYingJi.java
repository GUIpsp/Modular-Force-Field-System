package mffs.rongqi;

import mffs.SBangZhu;
import mffs.it.ka.SKa;
import mffs.jiqi.t.TFangYingJi;
import net.minecraft.entity.player.EntityPlayer;

public class CFangYingJi extends ContainerMFFS
{
	public CFangYingJi(EntityPlayer player, TFangYingJi tileEntity)
	{
		super(tileEntity);

		/**
		 * Frequency Card
		 */
		this.addSlotToContainer(new SKa(tileEntity, 0, 10, 89));
		this.addSlotToContainer(new SKa(tileEntity, 1, 28, 89));

		/**
		 * Force Field Manipulation Matrix. Center slot is the mode.
		 */
		// Mode
		this.addSlotToContainer(new SBangZhu(tileEntity, 2, 118, 45));

		int i = 3;
		// Misc Modules
		for (int xSlot = 0; xSlot < 4; xSlot++)
		{
			for (int ySlot = 0; ySlot < 4; ySlot++)
			{
				if (!(xSlot == 1 && ySlot == 1) && !(xSlot == 2 && ySlot == 2) && !(xSlot == 1 && ySlot == 2) && !(xSlot == 2 && ySlot == 1))
				{
					this.addSlotToContainer(new SBangZhu(tileEntity, i, 91 + 18 * xSlot, 18 + 18 * ySlot));
					i++;
				}
			}
		}

		// Misc Modules
		for (int xSlot = 0; xSlot < 3; xSlot++)
		{
			for (int ySlot = 0; ySlot < 2; ySlot++)
			{
				this.addSlotToContainer(new SBangZhu(tileEntity, i, 19 + 18 * xSlot, 36 + 18 * ySlot));
				i++;
			}
		}

		this.addPlayerInventory(player);
	}
}