package mffs.common.module;

import java.util.Map;

import mffs.api.PointXYZ;
import mffs.common.FrequencyGridOld;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.world.World;

public class ItemOptionJammer extends ItemOptionBase implements IChecksOnAll
{

	public ItemOptionJammer(int i)
	{
		super(i, "optionJammer");
		setIconIndex(41);
	}

	public boolean CheckJammerinfluence(PointXYZ png, World world, TileEntityProjector Projector)
	{
		Map<Integer, TileEntityProjector> InnerMap = null;
		InnerMap = FrequencyGridOld.getWorldMap(world).getJammer();

		for (TileEntityProjector tileentity : InnerMap.values())
		{
			boolean logicswitch = false;

			logicswitch = true;// tileentity.getPowerSourceID() != Projector.getPowerSourceID();

			if ((logicswitch) && (tileentity.isActive()))
			{
				for (PointXYZ tpng : tileentity.getInteriorPoints())
				{
					if ((tpng.X == png.X) && (tpng.Y == png.Y) && (tpng.Z == png.Z))
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