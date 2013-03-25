package mffs;

import java.util.HashSet;
import java.util.Set;

import mffs.api.fortron.IFortronFrequency;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

/**
 * A grid MFFS uses to search for machines with frequencies that can be linked and spread Fortron
 * energy.
 * 
 * @author Calclavia
 * 
 */
public class LiGuanLi
{
	public static LiGuanLi INSTANCE = new LiGuanLi();

	private final Set<IFortronFrequency> frequencyGrid = new HashSet<IFortronFrequency>();

	public void register(IFortronFrequency tileEntity)
	{
		if (!((TileEntity) tileEntity).worldObj.isRemote)
		{
			this.frequencyGrid.add(tileEntity);
		}
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
			if (tile != null && !((TileEntity) tile).isInvalid())
			{
				if (tile.getFrequency() == frequency)
				{
					set.add(tile);
				}
			}
		}

		return set;
	}

	public Set<IFortronFrequency> get(World world, Vector3 position, int radius, int frequency)
	{
		Set<IFortronFrequency> set = new HashSet<IFortronFrequency>();

		for (IFortronFrequency tileEntity : this.get(frequency))
		{
			if (Vector3.distance(new Vector3((TileEntity) tileEntity), position) <= radius)
			{
				set.add(tileEntity);
			}
		}
		return set;

	}
}
