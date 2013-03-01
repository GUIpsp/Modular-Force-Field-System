package mffs.common.module;

import java.util.Map;
import java.util.Set;

import mffs.api.PointXYZ;
import mffs.common.ForceFieldBlockStack;
import mffs.common.FrequencyGridOld;
import mffs.common.ModularForceFieldSystem;
import mffs.common.WorldMap;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemOptionFieldFusion extends ItemModule implements IInteriorCheck
{
	public ItemOptionFieldFusion(int i)
	{
		super(i, "optionFieldFusion");
		setIconIndex(43);
	}

	public boolean checkFieldFusioninfluence(Vector3 point, World world, TileEntityProjector Proj)
	{
		Map<Integer, TileEntityProjector> InnerMap = null;
		InnerMap = FrequencyGridOld.getWorldMap(world).getFieldFusion();
		for (TileEntityProjector tileentity : InnerMap.values())
		{
			boolean logicswitch = false;
			/*
			 * if (!Proj.isPowersourceItem()) { logicswitch = (tileentity.getPowerSourceID() ==
			 * Proj.getPowerSourceID()) && (tileentity.getDeviceID() != Proj.getDeviceID()); }
			 */
			if ((logicswitch) && (tileentity.isActive()))
			{
				for (PointXYZ tpng : tileentity.getInteriorPoints())
				{
					if ((tpng.X == point.intX()) && (tpng.Y == point.intY()) && (tpng.Z == point.intZ()))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void checkInteriorBlock(Vector3 png, World world, TileEntityProjector Proj,Set interior)
	{
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world).getorcreateFFStackMap(png.X, png.Y, png.Z, world);

		if (!ffworldmap.isEmpty())
		{
			// if (ffworldmap.getGenratorID() == Proj.getPowerSourceID())
			{
				TileEntityProjector Projector = (TileEntityProjector) FrequencyGridOld.getWorldMap(world).getProjector().get(Integer.valueOf(ffworldmap.getProjectorID()));

				if (Projector != null)
				{
					if (Projector.hasModule(ModularForceFieldSystem.itemOptionFieldFusion, true))
					{
						Projector.getFieldQueue().remove(png);
						ffworldmap.removebyProjector(Projector.getDeviceID());

						PointXYZ ffpng = ffworldmap.getPoint();

						if (world.getBlockId(ffpng.X, ffpng.Y, ffpng.Z) == ModularForceFieldSystem.blockForceField.blockID)
						{
							world.removeBlockTileEntity(ffpng.X, ffpng.Y, ffpng.Z);
							world.setBlockWithNotify(ffpng.X, ffpng.Y, ffpng.Z, 0);
						}
					}
				}
			}
		}
	}
}