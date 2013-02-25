package mffs.common.options;

import java.util.Map;

import mffs.api.PointXYZ;
import mffs.common.ForceFieldBlockStack;
import mffs.common.FrequencyGrid;
import mffs.common.ModularForceFieldSystem;
import mffs.common.WorldMap;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.world.World;

public class ItemOptionFieldFusion extends ItemOptionBase implements IInteriorCheck
{

    public ItemOptionFieldFusion(int i)
    {
        super(i, "optionFieldFusion");
        setIconIndex(43);
    }

    public boolean checkFieldFusioninfluence(PointXYZ png, World world, TileEntityProjector Proj)
    {
        Map<Integer, TileEntityProjector> InnerMap = null;
        InnerMap = FrequencyGrid.getWorldMap(world).getFieldFusion();
        for (TileEntityProjector tileentity : InnerMap.values())
        {
            boolean logicswitch = false;
            if (!Proj.isPowersourceItem())
            {
                logicswitch = (tileentity.getPowerSourceID() == Proj.getPowerSourceID()) && (tileentity.getDeviceID() != Proj.getDeviceID());
            }
            if ((logicswitch) && (tileentity.isActive()))
            {
                for (PointXYZ tpng : tileentity.getInteriorPoints())
                {
                    if ((tpng.X == png.X) && (tpng.Y == png.Y) && (tpng.Z == png.Z))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void checkInteriorBlock(PointXYZ png, World world, TileEntityProjector Proj)
    {
        ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world).getorcreateFFStackMap(png.X, png.Y, png.Z, world);

        if (!ffworldmap.isEmpty())
        {
            if (ffworldmap.getGenratorID() == Proj.getPowerSourceID())
            {
                TileEntityProjector Projector = (TileEntityProjector) FrequencyGrid.getWorldMap(world).getProjector().get(Integer.valueOf(ffworldmap.getProjectorID()));

                if (Projector != null)
                {
                    if (Projector.hasOption(ModularForceFieldSystem.itemOptionFieldFusion, true))
                    {
                        Projector.getfield_queue().remove(png);
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