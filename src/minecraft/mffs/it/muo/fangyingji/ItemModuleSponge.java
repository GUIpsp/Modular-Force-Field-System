package mffs.it.muo.fangyingji;

import mffs.api.IProjector;
import mffs.it.muo.ItM;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleSponge extends ItM
{
	public ItemModuleSponge(int i)
	{
		super(i, "moduleSponge");
	}

	@Override
	public boolean onProject(IProjector projector)
	{
		World world = ((TileEntity) projector).worldObj;

		for (Vector3 points : projector.getInteriorPoints())
		{
			if (Block.blocksList[points.getBlockID(world)] instanceof BlockFluid)
			{
				points.setBlock(world, 0);
			}
		}

		return super.onProject(projector);
	}

	@Override
	public float getFortronCost(int amplifier)
	{
		return super.getFortronCost(amplifier) * Math.max(Math.min((amplifier / 500), 1000), 1);
	}
}