package mffs.common.module;

import mffs.api.PointXYZ;
import mffs.common.MFFSConfiguration;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.world.World;

public class ItemOptionSponge extends ItemOptionBase implements IInteriorCheck
{

	public ItemOptionSponge(int i)
	{
		super(i, "optionSponge");
		setIconIndex(35);
	}

	@Override
	public void checkInteriorBlock(PointXYZ png, World world, TileEntityProjector Projector)
	{
		if (world.getBlockMaterial(png.X, png.Y, png.Z).isLiquid())
		{
			if (!MFFSConfiguration.forcefieldremoveonlywaterandlava)
			{
				world.setBlockWithNotify(png.X, png.Y, png.Z, 0);
			}
			else if ((world.getBlockId(png.X, png.Y, png.Z) == 8) || (world.getBlockId(png.X, png.Y, png.Z) == 9) || (world.getBlockId(png.X, png.Y, png.Z) == 10) || (world.getBlockId(png.X, png.Y, png.Z) == 11))
			{
				world.setBlockWithNotify(png.X, png.Y, png.Z, 0);
			}
		}
	}
}