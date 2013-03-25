package mffs.rongqi;

import mffs.SBangZhu;
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
		addSlotToContainer(new SBangZhu(tileEntity, 0, 76, 89));

		/**
		 * Force Field Manipulation Matrix. Center slot is the mode.
		 */
		// Up
		addSlotToContainer(new SBangZhu(tileEntity, 1, 18 + 81, 21));
		addSlotToContainer(new SBangZhu(tileEntity, 2, 18 + 101, 21));

		// Left
		addSlotToContainer(new SBangZhu(tileEntity, 3, 18 + 61, 36));
		addSlotToContainer(new SBangZhu(tileEntity, 4, 18 + 61, 56));

		// Mode
		addSlotToContainer(new SBangZhu(tileEntity, 5, 18 + 91, 18 + 26));

		// Right
		addSlotToContainer(new SBangZhu(tileEntity, 6, 139, 36));
		addSlotToContainer(new SBangZhu(tileEntity, 7, 139, 56));

		// Down
		addSlotToContainer(new SBangZhu(tileEntity, 7, 18 + 81, 67));
		addSlotToContainer(new SBangZhu(tileEntity, 8, 18 + 101, 67));

		/**
		 * Y Axis
		 */
		addSlotToContainer(new SBangZhu(tileEntity, 10, 56, 41));
		addSlotToContainer(new SBangZhu(tileEntity, 11, 38, 41));
		addSlotToContainer(new SBangZhu(tileEntity, 12, 56, 61));
		addSlotToContainer(new SBangZhu(tileEntity, 13, 38, 61));

		/**
		 * Misc
		 */
		addSlotToContainer(new SBangZhu(tileEntity, 14, 18, 41));
		addSlotToContainer(new SBangZhu(tileEntity, 15, 18, 61));

		addPlayerInventory(player);
	}
}