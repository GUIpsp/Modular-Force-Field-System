package mffs.common.module;

import mffs.common.MFFSConfiguration;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleSponge extends ItemModule implements IInteriorCheck
{
	public ItemModuleSponge(int i)
	{
		super(i, "moduleSponge");
		setIconIndex(35);
	}

	@Override
	public void checkInteriorBlock(Vector3 position, World world, TileEntityProjector Projector)
	{
		if (world.getBlockMaterial(position.intX(), position.intY(), position.intZ()).isLiquid())
		{
			if (!MFFSConfiguration.forcefieldremoveonlywaterandlava)
			{
				world.setBlockWithNotify(position.intX(), position.intY(), position.intZ(), 0);
			}
			else if ((world.getBlockId(position.intX(), position.intY(), position.intZ()) == 8) || (world.getBlockId(position.intX(), position.intY(), position.intZ()) == 9) || (world.getBlockId(position.intX(), position.intY(), position.intZ()) == 10) || (world.getBlockId(position.intX(), position.intY(), position.intZ()) == 11))
			{
				world.setBlockWithNotify(position.intX(), position.intY(), position.intZ(), 0);
			}
		}
	}
}