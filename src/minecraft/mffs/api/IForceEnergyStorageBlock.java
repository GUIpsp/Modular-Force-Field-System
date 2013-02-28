package mffs.api;

//TODO: Remove this
@Deprecated
public abstract interface IForceEnergyStorageBlock
{
	public abstract int getPercentageStorageCapacity();

	public abstract int getPowerStorageID();

	public abstract int getStorageMaxPower();

	public abstract int getStorageAvailablePower();

	public abstract boolean consumePowerFromStorage(int paramInt, boolean paramBoolean);

	public abstract boolean insertPowerToStorage(int paramInt, boolean paramBoolean);

	public abstract int getFreeStorageAmount();
}