package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.slotID;
import net.minecraft.item.ItemStack;

public class ContainerProjector extends Container
{

	private TileEntityProjector projectorentity;
	private int linkPower = -1;
	private int accesstyp = -1;
	private int capacity = -1;
	private EntityPlayer player;

	public ContainerProjector(EntityPlayer player, TileEntityProjector tileentity)
	{
		this.player = player;
		this.projectorentity = tileentity;

		addSlotToContainer(new SlotHelper(this.projectorentity, 0, 11, 61));
		addSlotToContainer(new SlotHelper(this.projectorentity, 1, 11, 38));

		addSlotToContainer(new SlotHelper(this.projectorentity, 2, 120, 82));
		addSlotToContainer(new SlotHelper(this.projectorentity, 3, 138, 82));
		addSlotToContainer(new SlotHelper(this.projectorentity, 4, 156, 82));

		addSlotToContainer(new SlotHelper(this.projectorentity, 6, 155, 64));
		addSlotToContainer(new SlotHelper(this.projectorentity, 5, 119, 64));

		addSlotToContainer(new SlotHelper(this.projectorentity, 7, 137, 28));
		addSlotToContainer(new SlotHelper(this.projectorentity, 8, 137, 62));
		addSlotToContainer(new SlotHelper(this.projectorentity, 9, 154, 45));
		addSlotToContainer(new SlotHelper(this.projectorentity, 10, 120, 45));

		addSlotToContainer(new SlotHelper(this.projectorentity, 11, 137, 45));

		addSlotToContainer(new SlotHelper(this.projectorentity, 12, 92, 38));
		int var3 = 0;
		for (var3 = 0; var3 < 3; var3++)
		{
			for (int var4 = 0; var4 < 9; var4++)
			{
				addSlotToContainer(new slotID(player.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 104 + var3 * 18));
			}

		}

		for (var3 = 0; var3 < 9; var3++)
		{
			addSlotToContainer(new slotID(player.inventory, var3, 8 + var3 * 18, 162));
		}
	}

	public EntityPlayer getPlayer()
	{
		return this.player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return this.projectorentity.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int i)
	{
		ItemStack itemstack = null;
		slotID slot = (slotID) this.inventorySlots.get(i);
		if ((slot != null) && (slot.getHasStack()))
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (itemstack1.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize != itemstack.stackSize)
			{
				slot.onSlotChanged();
			}
			else
			{
				return null;
			}
		}
		return itemstack;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); i++)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (this.linkPower != this.projectorentity.getLinkPower())
			{
				icrafting.sendProgressBarUpdate(this, 0, this.projectorentity.getLinkPower() & 0xFFFF);

				icrafting.sendProgressBarUpdate(this, 1, this.projectorentity.getLinkPower() >>> 16);
			}

			if (this.capacity != this.projectorentity.getPercentageCapacity())
			{
				icrafting.sendProgressBarUpdate(this, 2, this.projectorentity.getPercentageCapacity());
			}

			if (this.accesstyp != this.projectorentity.getAccessType())
			{
				icrafting.sendProgressBarUpdate(this, 4, this.projectorentity.getAccessType());
			}

		}

		this.linkPower = this.projectorentity.getLinkPower();
		this.accesstyp = this.projectorentity.getAccessType();
		this.capacity = this.projectorentity.getPercentageCapacity();
	}

	@Override
	public void updateProgressBar(int i, int j)
	{
		switch (i)
		{
			case 0:
				this.projectorentity.setLinkPower(this.projectorentity.getLinkPower() & 0xFFFF0000 | j);

				break;
			case 1:
				this.projectorentity.setLinkPower(this.projectorentity.getLinkPower() & 0xFFFF | j << 16);

				break;
			case 2:
				this.projectorentity.setCapacity(j);
				break;
			case 4:
				this.projectorentity.setAccessType(j);
			case 3:
		}
	}
}