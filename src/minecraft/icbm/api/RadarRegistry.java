package icbm.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector2;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.vector.Region2;

/**
 * This class allows you to register TileEntities and Entities to be detectable by the ICBM radar.
 * 
 * Make sure you unregister your object when it invalidates!
 * 
 * @author Calclavia
 * 
 */
public class RadarRegistry
{
	private static List<TileEntity> detectableTileEntities = new ArrayList<TileEntity>();
	private static List<Entity> detectableEntities = new ArrayList<Entity>();

	public static void register(TileEntity tileEntity)
	{
		if (!detectableTileEntities.contains(tileEntity))
		{
			detectableTileEntities.add(tileEntity);
		}
	}

	public static void unregister(TileEntity tileEntity)
	{
		if (detectableTileEntities.contains(tileEntity))
		{
			detectableTileEntities.remove(tileEntity);
		}
	}

	public static void register(Entity entity)
	{
		if (!detectableEntities.contains(entity))
		{
			detectableEntities.add(entity);
		}
	}

	public static void unregister(Entity entity)
	{
		if (detectableEntities.contains(entity))
		{
			detectableEntities.remove(entity);
		}
	}

	public static List<TileEntity> getTileEntitiesInArea(Vector2 minVector, Vector2 maxVector)
	{
		cleanUpArray();
		List<TileEntity> returnArray = new ArrayList<TileEntity>();

		for (TileEntity tileEntity : detectableTileEntities)
		{
			if (new Region2(minVector, maxVector).isIn(new Vector3(tileEntity).toVector2()))
			{
				returnArray.add(tileEntity);
			}
		}

		return returnArray;
	}

	public static List<Entity> getEntitiesWithinRadius(Vector2 vector, int radius)
	{
		cleanUpArray();
		List<Entity> returnArray = new ArrayList<Entity>();

		for (Entity entity : detectableEntities)
		{
			if (Vector2.distance(vector, new Vector3(entity).toVector2()) <= radius)
			{
				returnArray.add(entity);
			}
		}

		return returnArray;
	}

	public static List<TileEntity> getTileEntities()
	{
		cleanUpArray();
		return detectableTileEntities;
	}

	public static List<Entity> getEntities()
	{
		cleanUpArray();
		return detectableEntities;
	}

	public static void cleanUpArray()
	{
		try
		{
			Iterator it = detectableTileEntities.iterator();

			while (it.hasNext())
			{
				TileEntity tileEntity = (TileEntity) it.next();

				if (tileEntity == null)
				{
					it.remove();
				}
				else if (tileEntity.isInvalid())
				{
					it.remove();
				}
				else if (tileEntity.worldObj.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) != tileEntity)
				{
					it.remove();
				}
			}

			Iterator it2 = detectableEntities.iterator();

			while (it2.hasNext())
			{
				Entity entity = (Entity) it2.next();

				if (entity == null)
				{
					it2.remove();
				}
				else if (entity.isDead)
				{
					it2.remove();
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Failed to clean up radar list properly.");
			e.printStackTrace();
		}
	}
}
