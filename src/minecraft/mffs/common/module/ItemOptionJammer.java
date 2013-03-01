package mffs.common.module;

import java.util.Map;

import mffs.api.PointXYZ;
import mffs.common.FrequencyGridOld;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemOptionJammer extends ItemModule implements IChecksOnAll
{

	public ItemOptionJammer(int i)
	{
		super(i, "optionJammer");
		setIconIndex(41);
	}

	public boolean CheckJammerinfluence(Vector3 point, World world, TileEntityProjector Projector)
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
						Projector.projectorBurnout();
						return true;
					}
				}
			}
		}

		return false;
	}
}