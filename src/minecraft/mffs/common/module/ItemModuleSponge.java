package mffs.common.module;

import mffs.common.MFFSConfiguration;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleSponge extends ItemModule implements IInteriorCheck
{
	public ItemModuleSponge(int i)
	{
		super(i, "moduleSponge");
	}

	@Override
	public void checkInteriorBlock(Vector3 position, World world, TFangYingJi Projector)
	{
		if (world.getBlockMaterial(position.intX(), position.intY(), position.intZ()).isLiquid())
		{
			if (!MFFSConfiguration.forcefieldremoveonlywaterandlava)
			{
				world.setBlock(position.intX(), position.intY(), position.intZ(), 0, 0, 2);
			}
			else if ((world.getBlockId(position.intX(), position.intY(), position.intZ()) == 8) || (world.getBlockId(position.intX(), position.intY(), position.intZ()) == 9) || (world.getBlockId(position.intX(), position.intY(), position.intZ()) == 10) || (world.getBlockId(position.intX(), position.intY(), position.intZ()) == 11))
			{
				world.setBlock(position.intX(), position.intY(), position.intZ(), 0, 0, 2);
			}
		}
	}
}