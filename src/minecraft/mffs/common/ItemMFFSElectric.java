package mffs.common;

import mffs.api.IForceEnergyItems;
import mffs.common.item.ItemMFFS;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemMFFSElectric extends ItemMFFS implements
		IForceEnergyItems {

	public ItemMFFSElectric(int i, String name) {
		super(i, name);
	}

	@Override
	public boolean consumePower(ItemStack itemStack, int amount,
			boolean simulation) {
		if ((itemStack.getItem() instanceof IForceEnergyItems)) {
			if (getAvailablePower(itemStack) >= amount) {
				if (!simulation) {
					setAvailablePower(itemStack, getAvailablePower(itemStack)
							- amount);
				}
				return true;
			}
		}
		return false;
	}

	public boolean chargeItem(ItemStack itemStack, int amount,
			boolean simulation) {
		if ((itemStack.getItem() instanceof IForceEnergyItems)) {
			if (getAvailablePower(itemStack) + amount <= getMaximumPower(itemStack)) {
				if (!simulation) {
					setAvailablePower(itemStack, getAvailablePower(itemStack)
							+ amount);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void setAvailablePower(ItemStack itemStack, int ForceEnergy) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setInteger("ForceEnergy", ForceEnergy);
	}

	@Override
	public int getAvailablePower(ItemStack itemstack) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getInteger("ForceEnergy");
		}
		return 0;
	}
}