package mffs.api;

import icbm.api.IBlockFrequency;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

/**
 * A grid MFFS uses to search for machines with frequencies that can be linked and spread fortron
 * energy.
 * 
 * @author Calclavia
 * 
 */
public class FortronGrid
{
	public static final FortronGrid INSTANCE = new FortronGrid();

	private final HashSet<IFortronFrequency> frequencyGrid = new HashSet<IFortronFrequency>();

	public void register(IFortronFrequency tileEntity)
	{
		this.frequencyGrid.add(tileEntity);
	}

	public void unregister(IFortronFrequency tileEntity)
	{
		this.frequencyGrid.remove(tileEntity);
	}

	public Set<IFortronFrequency> get()
	{
		return this.frequencyGrid;
	}

	/**
	 * Gets a list of TileEntities that has a specific frequency.
	 * 
	 * @param frequency - The Frequency
	 * */
	public Set<IFortronFrequency> get(int frequency)
	{
		Set<IFortronFrequency> set = new HashSet<IFortronFrequency>();

		for (IFortronFrequency tile : this.get())
		{
			if (tile.getFrequency() == frequency)
			{
				set.add(tile);
			}
		}

		return set;
	}

	public Set<IFortronFrequency> get(World world, Vector3 position, int radius, int frequency)
	{
		Set<IFortronFrequency> set = new HashSet<IFortronFrequency>();

		for (IFortronFrequency tile : this.get(frequency))
		{
			if (Vector3.distance(new Vector3((TileEntity) tile), position) <= radius)
			{
				set.add(tile);
			}
		}
		return set;

	}
}
