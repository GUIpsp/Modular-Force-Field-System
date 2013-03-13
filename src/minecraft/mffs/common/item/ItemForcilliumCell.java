package mffs.common.item;

import java.util.List;

import mffs.common.ModularForceFieldSystem;
import mffs.common.NBTTagCompoundHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemForcilliumCell extends ItemMFFS
{
	private boolean isAbsorbingFocillium = false;

	public ItemForcilliumCell(int id)
	{
		super(id, "forcilliumCell");
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
		this.setNoRepair();
	}

	public int getItemDamage(ItemStack itemStack)
	{
		return 101 - getForceciumlevel(itemStack) * 100 / getMaxForceciumlevel();
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
	{
		if (!world.isRemote)
		{
			if (this.isAbsorbingFocillium)
			{
				if (getForceciumlevel(itemStack) < getMaxForceciumlevel())
				{
					if ((entity instanceof EntityPlayer))
					{
						List<Slot> slots = ((EntityPlayer) entity).inventoryContainer.inventorySlots;
						for (Slot slot : slots)
						{
							if ((slot.getStack() != null) && (slot.getStack().getItem() == ModularForceFieldSystem.itemForcillium))
							{
								setForceciumlevel(itemStack, getForceciumlevel(itemStack) + 1);

								if (slot.getStack().stackSize > 1)
								{
									ItemStack forcecium = new ItemStack(ModularForceFieldSystem.itemForcillium, slot.getStack().stackSize - 1);
									slot.putStack(forcecium);
									break;
								}
								slot.putStack(null);

								break;
							}

						}

					}

				}

				itemStack.setItemDamage(getItemDamage(itemStack));
			}
		}
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		String tooltip = String.format("%d / %d  Forcillium", new Object[] { Integer.valueOf(getForceciumlevel(itemStack)), Integer.valueOf(getMaxForceciumlevel()) });
		info.add(tooltip);
	}

	public boolean useForcecium(int count, ItemStack itemstack)
	{
		if (count > getForceciumlevel(itemstack))
		{
			return false;
		}

		setForceciumlevel(itemstack, getForceciumlevel(itemstack) - count);
		return true;
	}

	public int getMaxForceciumlevel()
	{
		return 1000;
	}

	public void setForceciumlevel(ItemStack itemStack, int Forceciumlevel)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		nbtTagCompound.setInteger("Forcilliumlevel", Forceciumlevel);
	}

	public int getForceciumlevel(ItemStack itemstack)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null)
		{
			return nbtTagCompound.getInteger("Forcilliumlevel");
		}
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (!world.isRemote)
		{
			if (!this.isAbsorbingFocillium)
			{
				this.isAbsorbingFocillium = true;
				entityplayer.addChatMessage("Cell Active");
			}
			else
			{
				this.isAbsorbingFocillium = false;
				entityplayer.addChatMessage("Cell Inactive");
			}

		}

		return itemstack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int i, CreativeTabs tabs, List itemList)
	{
		ItemStack charged = new ItemStack(this, 1);
		charged.setItemDamage(1);
		setForceciumlevel(charged, getMaxForceciumlevel());
		itemList.add(charged);

		ItemStack empty = new ItemStack(this, 1);
		empty.setItemDamage(100);
		setForceciumlevel(empty, 0);
		itemList.add(empty);
	}
}