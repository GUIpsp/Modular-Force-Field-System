package mffs.rongqi;

import mffs.SBangZhu;
import mffs.it.ka.SKa;
import mffs.jiqi.t.TFangYu;
import net.minecraft.entity.player.EntityPlayer;

public class CFangYu extends ContainerMFFS
{
	public CFangYu(EntityPlayer player, TFangYu tileEntity)
	{
		super(tileEntity);

		/**
		 * Frequency Card
		 */
		this.addSlotToContainer(new SKa(tileEntity, 0, 87, 89));
		this.addSlotToContainer(new SBangZhu(tileEntity, 1, 69, 89));

		/**
		 * Module slots.
		 */
		for (int var3 = 0; var3 < 2; var3++)
		{
			for (int var4 = 0; var4 < 4; var4++)
			{
				this.addSlotToContainer(new SBangZhu(tileEntity, var4 + var3 * 4 + 2, 99 + var4 * 18, 31 + var3 * 18));
			}
		}

		/**
		 * Item filter slots.
		 */
		for (int var4 = 0; var4 < 9; var4++)
		{
			this.addSlotToContainer(new SBangZhu(tileEntity, var4 + 8 + 2, 9 + var4 * 18, 69));
		}

		this.addPlayerInventory(player);
	}
}