package mffs.api;

public interface IDefenseStation
{

	/**
	 * The range in which the defense station starts warning the player.
	 * 
	 * @return
	 */
	public int getWarningRange();

	/**
	 * The range in which the defense station has an effect on.
	 * 
	 * @return
	 */
	public int getActionRange();
}
