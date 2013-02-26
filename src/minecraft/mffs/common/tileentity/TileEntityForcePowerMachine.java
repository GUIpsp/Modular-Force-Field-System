package mffs.common.tileentity;

import mffs.api.IForcePower;
import mffs.api.IPowerLinkItem;
import net.minecraft.item.ItemStack;

public abstract class TileEntityForcePowerMachine extends TileEntityMFFS
		implements IForcePower {

	public abstract ItemStack getPowerLinkStack();

	public abstract int getPowerLinkSlot();

	@Override
	public int getPercentageCapacity() {
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()) {
			return ((IPowerLinkItem) linkCard.getItem()).getPercentageCapacity(
					linkCard, this, this.worldObj);
		}
		return 0;
	}

	@Override
	public boolean hasPowerSource() {
		ItemStack linkCard = getPowerLinkStack();
		if ((linkCard != null)
				&& ((linkCard.getItem() instanceof IPowerLinkItem))) {
			return true;
		}
		return false;
	}

	@Override
	public double getForcePower() {
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()) {
			return ((IPowerLinkItem) linkCard.getItem()).getAvailablePower(
					linkCard, this, this.worldObj);
		}

		return 0;
	}

	@Override
	public void setForcePower(double joules) {
	}

	public int getPowerSourceID() {
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()) {
			return ((IPowerLinkItem) linkCard.getItem()).getPowersourceID(
					linkCard, this, this.worldObj);
		}
		return 0;
	}

	public int getMaximumPower() {
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()) {
			return ((IPowerLinkItem) linkCard.getItem()).getMaximumPower(
					linkCard, this, this.worldObj);
		}
		return 0;
	}

	public boolean consumePower(int powerAmount, boolean simulation) {
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()) {
			return ((IPowerLinkItem) linkCard.getItem()).consumePower(linkCard,
					powerAmount, simulation, this, this.worldObj);
		}
		return false;
	}

	public boolean emitPower(int powerAmount, boolean simulation) {
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()) {
			return ((IPowerLinkItem) linkCard.getItem()).insertPower(linkCard,
					powerAmount, simulation, this, this.worldObj);
		}

		return false;
	}

	public boolean isPowersourceItem() {
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()) {
			return ((IPowerLinkItem) linkCard.getItem()).isPowersourceItem();
		}
		return false;
	}
}