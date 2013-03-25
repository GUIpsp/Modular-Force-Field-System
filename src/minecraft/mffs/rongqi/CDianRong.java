package mffs.rongqi;

import mffs.SBangZhu;
import mffs.it.ka.SKa;
import mffs.jiqi.t.TDianRong;
import net.minecraft.entity.player.EntityPlayer;

public class CDianRong extends ContainerMFFS
{
	private TDianRong tileEntity;

	public CDianRong(EntityPlayer player, TDianRong tileEntity)
	{
		super(tileEntity);
		this.tileEntity = tileEntity;

		// Frequency Card
		this.addSlotToContainer(new SKa(this.tileEntity, 0, 9, 74));
		this.addSlotToContainer(new SKa(this.tileEntity, 1, 27, 74));

		// Upgrades
		this.addSlotToContainer(new SBangZhu(this.tileEntity, 2, 154, 47));
		this.addSlotToContainer(new SBangZhu(this.tileEntity, 3, 154, 67));
		this.addSlotToContainer(new SBangZhu(this.tileEntity, 4, 154, 87));

		this.addPlayerInventory(player);
	}
}