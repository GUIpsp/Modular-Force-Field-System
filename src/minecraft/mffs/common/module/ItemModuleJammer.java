package mffs.common.module;

import java.util.Map;

import mffs.common.FrequencyGridOld;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleJammer extends ItemModule
{
	public ItemModuleJammer(int i)
	{
		super(i, "moduleJammer");
	}

	public boolean checkJammerinfluence(Vector3 point, World world, TileEntityProjector projector)
	{
		Map<Integer, TileEntityProjector> InnerMap = null;
		InnerMap = FrequencyGridOld.getWorldMap(world).getJammer();

		for (TileEntityProjector tileentity : InnerMap.values())
		{
			boolean logicswitch = false;

			logicswitch = true;// tileentity.getPowerSourceID() != Projector.getPowerSourceID();

			if ((logicswitch) && (tileentity.isActive()))
			{
				for (Vector3 position : tileentity.getInteriorPoints())
				{
					if ((position.intX() == point.intX()) && (position.intY() == point.intY()) && (position.intZ() == point.intZ()))
					{
						projector.onDisable(20 * 10);
						return true;
					}
				}
			}
		}

		return false;
	}
}