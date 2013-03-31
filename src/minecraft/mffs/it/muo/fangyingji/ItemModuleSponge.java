package mffs.it.muo.fangyingji;

import mffs.MFFSConfiguration;
import mffs.api.IProjector;
import mffs.it.muo.ItM;
import mffs.jiqi.t.TFangYingJi;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleSponge extends ItM
{
	public ItemModuleSponge(int i)
	{
		super(i, "moduleSponge");
	}

	@Override
	public void onProject(IProjector projector, Vector3 position)
	{/*
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
		}*/
	}
}