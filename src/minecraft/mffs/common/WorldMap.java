package mffs.common;

import java.util.HashMap;
import java.util.Map;

import mffs.api.PointXYZ;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

import com.google.common.collect.MapMaker;

public final class WorldMap
{

	private static Map ForceFieldWorlds = new MapMaker().weakKeys().makeMap();

	public static ForceFieldWorld getForceFieldWorld(World world)
	{
		if (world != null)
		{
			if (!ForceFieldWorlds.containsKey(world))
			{
				ForceFieldWorlds.put(world, new ForceFieldWorld());
			}
			return (ForceFieldWorld) ForceFieldWorlds.get(world);
		}

		return null;
	}

	public static class ForceFieldWorld
	{

		private static Map ForceFieldStackMap = new HashMap();

		public ForceFieldBlockStack getorcreateFFStackMap(int x, int y, int z, World world)
		{
			Vector3 vector = new Vector3(x, y, z);
			if (ForceFieldStackMap.get(Integer.valueOf(vector.hashCode())) == null)
			{
				ForceFieldStackMap.put(Integer.valueOf(vector.hashCode()), new ForceFieldBlockStack(vector));
			}
			return (ForceFieldBlockStack) ForceFieldStackMap.get(Integer.valueOf(vector.hashCode()));
		}

		public ForceFieldBlockStack getForceFieldStackMap(Integer hasher)
		{
			return (ForceFieldBlockStack) ForceFieldStackMap.get(hasher);
		}

		public ForceFieldBlockStack getForceFieldStackMap(PointXYZ png)
		{
			return (ForceFieldBlockStack) ForceFieldStackMap.get(Integer.valueOf(png.hashCode()));
		}

		public int isExistForceFieldStackMap(int x, int y, int z, int counter, int typ, World world)
		{
			switch (typ)
			{
				case 0:
					y += counter;
					break;
				case 1:
					y -= counter;
					break;
				case 2:
					z += counter;
					break;
				case 3:
					z -= counter;
					break;
				case 4:
					x += counter;
					break;
				case 5:
					x -= counter;
			}

			ForceFieldBlockStack Map = (ForceFieldBlockStack) ForceFieldStackMap.get(Integer.valueOf(new PointXYZ(x, y, z, world).hashCode()));

			if (Map == null)
			{
				return 0;
			}
			if (Map.isEmpty())
			{
				return 0;
			}

			return Map.getGenratorID();
		}
	}
}