package mffs.api;

public interface IFortronStorage
{
	/**
	 * Sets the amount of fortron energy.
	 * 
	 * @param joules
	 */
	public void setFortronEnergy(int joules);

	/**
	 * @return Gets the amount of fortron stored.
	 */
	public int getFortronEnergy();

	/**
	 * 
	 * @return Gets the maximum possible amount of fortron that can be stored.
	 */
	public int getFortronCapacity();

	/**
	 * Called to use and consume fortron energy from this storage unit.
	 * 
	 * @param joules - Amount of fortron energy to use.
	 * @param doUse - True if actually using, false if just simulating.
	 */
	public int consumeFortron(int joules, boolean doUse);
}
