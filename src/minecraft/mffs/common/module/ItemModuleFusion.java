package mffs.common.module;

import java.util.Map;

import mffs.common.ForceFieldBlockStack;
import mffs.common.FrequencyGridOld;
import mffs.common.ModularForceFieldSystem;
import mffs.common.WorldMap;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleFusion extends ItemModule implements IInteriorCheck
{
	public ItemModuleFusion(int i)
	{
		super(i, "moduleFusion");
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
				for (Vector3 position : tileentity.getInteriorPoints())
				{
					if ((position.intX() == point.intX()) && (position.intY() == point.intY()) && (position.intZ() == point.intZ()))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void checkInteriorBlock(Vector3 position, World world, TileEntityProjector Proj)
	{
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world).getorcreateFFStackMap(position.intX(), position.intY(), position.intZ(), world);

		if (!ffworldmap.isEmpty())
		{
			// if (ffworldmap.getGenratorID() == Proj.getPowerSourceID())
			{
				TileEntityProjector Projector = (TileEntityProjector) FrequencyGridOld.getWorldMap(world).getProjector().get(Integer.valueOf(ffworldmap.getProjectorID()));

				if (Projector != null)
				{
					if (Projector.hasModule(ModularForceFieldSystem.itemOptionFieldFusion, true))
					{
						Projector.getFieldQueue().remove(position);
						ffworldmap.removebyProjector(Projector.getDeviceID());

						Vector3 point = ffworldmap.getPoint();

						if (world.getBlockId(position.intX(), position.intY(), position.intZ()) == ModularForceFieldSystem.blockForceField.blockID)
						{
							world.removeBlockTileEntity(position.intX(), position.intY(), position.intZ());
							world.setBlockWithNotify(position.intX(), position.intY(), position.intZ(), 0);
						}
					}
				}
			}
		}
	}
}