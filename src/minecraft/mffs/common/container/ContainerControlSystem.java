package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TileEntityControlSystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerControlSystem extends ContainerMFFS
{

	private EntityPlayer player;
	private TileEntityControlSystem tileEntity;

	public ContainerControlSystem(EntityPlayer player, TileEntityControlSystem tileEntity)
	{
		super(tileEntity);
		this.tileEntity = tileEntity;
		this.player = player;

		addSlotToContainer(new SlotHelper(this.tileEntity, 0, 236, 4));
		addSlotToContainer(new SlotHelper(this.tileEntity, 1, 203, 30));
		int var3 = 0;
		for (var3 = 0; var3 < 7; var3++)
		{
			for (int var4 = 0; var4 < 4; var4++)
			{
				addSlotToContainer(new SlotHelper(this.tileEntity, var4 + var3 * 4 + 4, 176 + var4 * 18, 80 + var3 * 18));
			}

		}

		for (var3 = 0; var3 < 3; var3++)
		{
			for (int var4 = 0; var4 < 9; var4++)
			{
				addSlotToContainer(new Slot(player.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 134 + var3 * 18));
			}

		}

		for (var3 = 0; var3 < 9; var3++)
		{
			addSlotToContainer(new Slot(player.inventory, var3, 8 + var3 * 18, 192));
		}
	}

	public EntityPlayer getPlayer()
	{
		return this.player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return this.tileEntity.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int i)
	{
		return null;
	}
}