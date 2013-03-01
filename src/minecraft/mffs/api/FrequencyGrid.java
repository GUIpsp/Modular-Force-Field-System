package mffs.api;

import icbm.api.IBlockFrequency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

/**
 * A grid MFFS uses to search for machines with frequencies that can be linked.
 * 
 * @author Calclavia
 * 
 */
public class FrequencyGrid
{
	public static final FrequencyGrid INSTANCE = new FrequencyGrid();

	private final HashMap<TileEntity, Integer> frequencyGrid = new HashMap<TileEntity, Integer>();

	public void register(TileEntity tileEntity, int frequency)
	{
		this.frequencyGrid.put(tileEntity, frequency);
	}

	public void unregister(TileEntity tileEntity)
	{
		this.frequencyGrid.remove(tileEntity);
	}

	/**
	 * Gets the frequency of this specific TileEntity.
	 * 
	 * @param tiletntity
	 * @return
	 */
	public int getFrequency(TileEntity tiletntity)
	{
		return this.frequencyGrid.get(tiletntity);
	}

	/**
	 * Gets a list of TileEntities that have a specific frequency.
	 * 
	 * @param frequency - The Frequency
	 * */
	public Set<TileEntity> getTileEntities(int frequency)
	{
		return new HashSet<TileEntity>();
	}

	public static Set<TileEntity> get(World world, Vector3 position, int radius, int frequency)
	{
		Set<TileEntity> set = new HashSet<TileEntity>();

		for (int x = -radius; x < radius; x++)
		{
			for (int y = -radius; y < radius; y++)
			{
				for (int z = -radius; z < radius; z++)
				{
					Vector3 checkPosition = Vector3.add(new Vector3(x, y, z), position.clone());

					TileEntity tileEntity = checkPosition.getTileEntity(world);
					
					if (tileEntity instanceof IBlockFrequency)
					{
						set.add(tileEntity);
					}
				}
			}
		}

		return set;
	}
}
