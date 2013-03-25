package mffs.rongqi;

import mffs.SBangZhu;
import mffs.it.ka.SKa;
import mffs.jiqi.t.TChouQi;
import net.minecraft.entity.player.EntityPlayer;

public class CChouQi extends ContainerMFFS
{
	public CChouQi(EntityPlayer player, TChouQi tileEntity)
	{
		super(tileEntity);

		/**
		 * Focillium Input
		 */
		this.addSlotToContainer(new SBangZhu(tileEntity, 0, 9, 83));

		/**
		 * Frequency Card
		 */
		this.addSlotToContainer(new SKa(tileEntity, 1, 9, 41));

		/**
		 * Upgrades
		 */
		this.addSlotToContainer(new SBangZhu(tileEntity, 2, 154, 67));
		this.addSlotToContainer(new SBangZhu(tileEntity, 3, 154, 87));
		this.addSlotToContainer(new SBangZhu(tileEntity, 4, 154, 47));

		this.addPlayerInventory(player);
	}
}