package mffs.common.block;

import mffs.common.tileentity.TileEntityForcilliumExtractor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockForcilliumExtractor extends BlockMFFSMachine
{

	public BlockForcilliumExtractor(int i)
	{
		super(i, "forcilliumExtractor");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityForcilliumExtractor();
	}
}